package com.castmart.firebaseauth

import com.castmart.firebaseauth.config.security.model.SecurityProperties
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ApiController(private val securityProperties: SecurityProperties) {

    @GetMapping("/api/protected")
    fun getProperties(): ResponseEntity<Any> {
        return ResponseEntity.ok(securityProperties)
    }

    @GetMapping("/public/data")
    fun getPublicProperties(): ResponseEntity<Any> {
        return ResponseEntity.ok(securityProperties)
    }
}