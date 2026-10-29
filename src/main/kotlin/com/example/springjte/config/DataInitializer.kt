package com.example.springjte.config

import com.example.springjte.model.ApiKey
import com.example.springjte.model.User
import com.example.springjte.model.Webhook
import com.example.springjte.repository.ApiKeyRepository
import com.example.springjte.repository.UserRepository
import com.example.springjte.repository.WebhookRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class DataInitializer(
    private val repo: UserRepository,
    private val apiKeyRepo: ApiKeyRepository,
    private val webhookRepo: WebhookRepository,
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

        // Seed a default API key if none exist
        if (apiKeyRepo.count() == 0L) {
            apiKeyRepo.save(
                ApiKey(
                    keyValue = "sk_demo_key_1234567890abcdef",
                    name = "Demo API Key",
                    userEmail = "admin@example.com"
                )
            )
        }

        // Seed a sample webhook if none exist
        if (webhookRepo.findAll().isEmpty()) {
            webhookRepo.save(
                Webhook(
                    url = "https://hooks.example.com/incoming",
                    description = "Sample webhook — replace with your endpoint",
                    eventTypes = "*",
                    active = false
                )
            )
        }
    }
}
