package com.castmart.firebaseauth.config.security

import com.castmart.firebaseauth.config.security.model.Credentials
import com.castmart.firebaseauth.config.security.model.SecurityProperties
import com.castmart.firebaseauth.config.security.model.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import javax.servlet.http.HttpServletRequest


@Service
class SecurityService(
    var httpServletRequest: HttpServletRequest? = null, var cookieUtils: CookieUtil? = null,
    var securityProps: SecurityProperties? = null
) {

    fun getUser(): User? {
        var userPrincipal: User? = null
        val securityContext = SecurityContextHolder.getContext()
        val principal = securityContext.authentication.principal
        if (principal is User) {
            userPrincipal = principal as User
        }
        return userPrincipal
    }

    fun getCredentials(): Credentials? {
        val securityContext = SecurityContextHolder.getContext()
        return securityContext.authentication.credentials as Credentials
    }

    fun isPublic(): Boolean {
        return securityProps!!.allowedPublicApis!!.contains(httpServletRequest!!.requestURI)
    }

    fun getBearerToken(request: HttpServletRequest): String? {
        var bearerToken: String? = null
        val authorization = request.getHeader("Authorization")
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            bearerToken = authorization.substring(7)
        }
        return bearerToken
    }
}