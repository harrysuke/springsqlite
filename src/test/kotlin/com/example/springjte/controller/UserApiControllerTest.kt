package com.example.springjte.controller

import com.example.springjte.repository.InMemoryApiKeyRepository
import com.example.springjte.repository.InMemoryAuditLogRepository
import com.example.springjte.repository.InMemoryUsageEventRepository
import com.example.springjte.repository.InMemoryUserRepository
import com.example.springjte.repository.InMemoryWebhookRepository
import com.example.springjte.repository.InMemoryWebhookDeliveryRepository
import com.example.springjte.repository.UserRepository
import com.example.springjte.service.AuditService
import com.example.springjte.service.ApiKeyService
import com.example.springjte.service.UsageService
import com.example.springjte.service.UserService
import com.example.springjte.service.WebhookDeliveryService
import tools.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UserApiController::class)
@AutoConfigureMockMvc(addFilters = false)
@Import(UserApiControllerTest.TestConfig::class)
class UserApiControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var userRepository: InMemoryUserRepository

    @BeforeEach
    fun resetRepository() {
        userRepository.clear()
    }

    @Test
    fun `create returns 201 with the created user`() {
        mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"Alice","email":"a@b.c","password":"secret12"}""")
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Alice"))
            .andExpect(jsonPath("$.email").value("a@b.c"))
            .andExpect(jsonPath("$.password").doesNotExist())
    }

    @Test
    fun `invalid payload returns 400 with field errors`() {
        mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"A","email":"not-an-email","password":"secret12"}""")
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("Validation failed"))
    }

    @Test
    fun `list returns created users`() {
        mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"Bob","email":"b@c.d","password":"secret12"}""")
        )
            .andExpect(status().isCreated)

        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("Bob"))
            .andExpect(jsonPath("$[0].password").doesNotExist())
    }

    @TestConfiguration
    class TestConfig {
        @Bean
        fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

        @Bean
        fun userRepository(): InMemoryUserRepository = InMemoryUserRepository()

        @Bean
        fun webhookRepository(): InMemoryWebhookRepository = InMemoryWebhookRepository()

        @Bean
        fun webhookDeliveryRepository(): InMemoryWebhookDeliveryRepository = InMemoryWebhookDeliveryRepository()

        @Bean
        fun apiKeyRepository(): InMemoryApiKeyRepository = InMemoryApiKeyRepository()

        @Bean
        fun auditLogRepository(): InMemoryAuditLogRepository = InMemoryAuditLogRepository()

        @Bean
        fun usageEventRepository(): InMemoryUsageEventRepository = InMemoryUsageEventRepository()

        @Bean
        fun objectMapper(): ObjectMapper = ObjectMapper()

        @Bean
        fun webhookDeliveryService(
            webhookRepo: InMemoryWebhookRepository,
            deliveryRepo: InMemoryWebhookDeliveryRepository,
            objectMapper: ObjectMapper
        ): WebhookDeliveryService = WebhookDeliveryService(webhookRepo, deliveryRepo, objectMapper)

        @Bean
        fun apiKeyService(apiKeyRepo: InMemoryApiKeyRepository): ApiKeyService = ApiKeyService(apiKeyRepo)

        @Bean
        fun auditService(auditRepo: InMemoryAuditLogRepository): AuditService = AuditService(auditRepo)

        @Bean
        fun usageService(usageRepo: InMemoryUsageEventRepository): UsageService = UsageService(usageRepo)

        @Bean
        fun saasInterceptor(auditService: AuditService, usageService: UsageService) =
            com.example.springjte.web.SaasInterceptor(auditService, usageService)

        @Bean
        fun userService(
            repo: UserRepository,
            encoder: PasswordEncoder,
            webhookDeliveryService: WebhookDeliveryService
        ): UserService = UserService(repo, encoder, webhookDeliveryService)
    }
}
