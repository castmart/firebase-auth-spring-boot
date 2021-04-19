package com.castmart.firebaseauth

import com.castmart.firebaseauth.config.security.model.SecurityProperties
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class ApiController(private val securityProperties: SecurityProperties) {

    @GetMapping
    fun getProperties(): ResponseEntity<Any> {

        return ResponseEntity.ok(securityProperties)
    }
}