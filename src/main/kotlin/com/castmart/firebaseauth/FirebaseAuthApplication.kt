package com.castmart.firebaseauth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class FirebaseAuthApplication

fun main(args: Array<String>) {
    runApplication<FirebaseAuthApplication>(*args)
}
