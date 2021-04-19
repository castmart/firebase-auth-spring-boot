package com.castmart.firebaseauth.config.security.firebase

import com.castmart.firebaseauth.config.security.model.SecurityProperties
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.io.ClassPathResource
import java.io.IOException

@Configuration
class FirebaseConfig(val securityProperties: SecurityProperties) {

    @Primary
    @Bean
    fun firebaseInit() {
       try {
           val inputStream = ClassPathResource("firebase_config.json").inputStream
           val firebaseOptions = FirebaseOptions
               .Builder()
               .setCredentials(GoogleCredentials.fromStream(inputStream))
               .build()
           if(FirebaseApp.getApps().isEmpty()) {
               FirebaseApp.initializeApp(firebaseOptions)
           }
           println("Firebase initialized...")
       } catch (ioException: IOException) {
           println(ioException)
       }
    }
}