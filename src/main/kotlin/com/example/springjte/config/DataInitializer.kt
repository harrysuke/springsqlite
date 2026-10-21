package com.example.springjte.config

import com.example.springjte.model.User
import com.example.springjte.repository.UserRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class DataInitializer(
    private val repo: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Bean
    fun seedDefaultAdmin(): ApplicationRunner = ApplicationRunner {
        if (repo.findByEmail("admin@example.com") == null) {
            repo.save(
                User(
                    name = "Administrator",
                    email = "admin@example.com",
                    password = passwordEncoder.encode("admin123")!!,
                    role = "ROLE_ADMIN"
                )
            )
        }
    }
}