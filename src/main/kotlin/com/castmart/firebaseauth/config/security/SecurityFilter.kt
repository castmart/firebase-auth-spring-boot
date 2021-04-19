package com.castmart.firebaseauth.config.security

import com.castmart.firebaseauth.config.security.model.CredentialType
import com.castmart.firebaseauth.config.security.model.Credentials
import com.castmart.firebaseauth.config.security.model.SecurityProperties
import com.castmart.firebaseauth.config.security.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class SecurityFilter(val securityService: SecurityService,
                     val cookieUtils: CookieUtil,
                     val securityProps: SecurityProperties): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        verifyToken(request)
        filterChain.doFilter(request, response)
    }

    fun verifyToken(request: HttpServletRequest) {
        var session: String? = null
        var decodedToken: FirebaseToken? = null
        var type: CredentialType? = null
        val strictServerSessionEnabled: Boolean = securityProps!!.firebaseProps!!.enableStrictServerSession
        val sessionCookie: Cookie? = cookieUtils.getCookie("session")
        val token = securityService.getBearerToken(request)
//        logger.info(token)
        try {
            if (sessionCookie != null) {
                session = sessionCookie.getValue()
                decodedToken = FirebaseAuth.getInstance().verifySessionCookie(
                    session,
                    securityProps!!.firebaseProps!!.enableCheckSessionRevoked
                )
                type = CredentialType.SESSION
            } else if (!strictServerSessionEnabled) {
                if (token != null && !token.equals("undefined", ignoreCase = true)) {
                    decodedToken = FirebaseAuth.getInstance().verifyIdToken(token)
                    type = CredentialType.ID_TOKEN
                }
            }
        } catch (e: FirebaseAuthException) {
            e.printStackTrace()

        }
        val user: User? = firebaseTokenToUserDto(decodedToken)
        if (user != null) {
            val authentication = UsernamePasswordAuthenticationToken(
                user,
                Credentials(type, decodedToken, token, session), null
            )
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authentication
        }
    }

    private fun firebaseTokenToUserDto(decodedToken: FirebaseToken?): User? {
        var user: User? = null
        if (decodedToken != null) {
            user = User(
                uid = decodedToken.uid, name = decodedToken.name, email = decodedToken.email,
                picture = decodedToken.picture, issuer = decodedToken.issuer,
                isEmailVerified = decodedToken.isEmailVerified)
        }
        return user
    }
}