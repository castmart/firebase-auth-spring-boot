package com.castmart.firebaseauth.config.security

import com.google.firebase.auth.FirebaseToken
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SecurityFilter(//val securityService: SecurityService,
                     val restSecurityProperties: SecurityProperties,
                     //val cookieUtils: CookieUtils,
                     val securityProperties: SecurityProperties): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        verifyToken(request)
        filterChain.doFilter(request, response)
    }

    fun verifyToken(request: HttpServletRequest) {
        var session: String
        var firebaseToken: FirebaseToken
    }
}