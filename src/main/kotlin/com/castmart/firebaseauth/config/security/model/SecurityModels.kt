package com.castmart.firebaseauth.config.security.model

import com.google.firebase.auth.FirebaseToken
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.stereotype.Component

@ConstructorBinding
@ConfigurationProperties("security.cookie-props")
data class CookieProps(
    var domain: String = "", var path: String = "", var httpOnly: Boolean = false, var secure: Boolean = false, var maxAgeInMinutes: Int
    )

enum class CredentialType {
    ID_TOKEN, SESSION
}

data class Credentials(
    var credentialType: CredentialType, var firebaseToke: FirebaseToken, var idToken: String, var session: String = ""
    )

@ConstructorBinding
@ConfigurationProperties("security.firebase-props")
data class FirebaseProps(
    var sessionExpiryInDays: Int = 1, var databaseUrl: String = "", var enableStrictServerSession: Boolean = true,
    var enableCheckSessionRevoked: Boolean, var enableLogoutEverywhere: Boolean
    )

@ConstructorBinding
@ConfigurationProperties(prefix="security")
data class SecurityProperties(
    var cookieProps: CookieProps? = null,  var firebaseProps: FirebaseProps? = null,
    var allowCredentials: Boolean = true, var allowedOrigins: List<String>, var exposedHeaders: List<String>,
    var allowedMethods: List<String>, var allowedPublicApis: List<String>
)