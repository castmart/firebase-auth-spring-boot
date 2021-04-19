package com.castmart.firebaseauth.config.security

import com.castmart.firebaseauth.config.security.model.SecurityProperties
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.sql.Timestamp
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
class SecurityConfig(
    val objectMapper: ObjectMapper, val securityProperties: SecurityProperties, val securityFilter: SecurityFilter
    ): WebSecurityConfigurerAdapter() {

    @Bean
    fun restAuthenticationEntryPoint(): AuthenticationEntryPoint? {
        return AuthenticationEntryPoint { httpServletRequest: HttpServletRequest?, httpServletResponse: HttpServletResponse, e: AuthenticationException? ->
            val errorObject: MutableMap<String, Any> = HashMap()
            val errorCode = 401
            errorObject["message"] = "Unauthorized access of protected resource, invalid credentials"
            errorObject["error"] = HttpStatus.UNAUTHORIZED
            errorObject["code"] = errorCode
            errorObject["timestamp"] = Timestamp(Date().getTime())
            httpServletResponse.contentType = "application/json;charset=UTF-8"
            httpServletResponse.status = errorCode
            httpServletResponse.writer.write(objectMapper.writeValueAsString(errorObject))
        }
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource? {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = securityProperties.allowedOrigins
        configuration.allowedMethods = securityProperties.allowedMethods
        configuration.allowedHeaders = securityProperties.allowedHeaders
        configuration.allowCredentials = securityProperties.allowCredentials
        configuration.exposedHeaders = securityProperties.exposedHeaders
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
    override fun configure(http: HttpSecurity?) {
        http!!.cors().configurationSource(corsConfigurationSource()).and().csrf().disable().formLogin().disable()
            .httpBasic().disable().exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint())
            .and().authorizeRequests()
            .antMatchers(*securityProperties.allowedPublicApis.toTypedArray()).permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll().anyRequest().authenticated().and()
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter::class.java)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }
}