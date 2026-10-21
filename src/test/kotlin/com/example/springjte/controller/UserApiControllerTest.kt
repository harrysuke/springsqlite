package com.example.springjte.controller

import com.example.springjte.repository.InMemoryUserRepository
import com.example.springjte.repository.UserRepository
import com.example.springjte.service.UserService
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
@Import(UserService::class)
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
    }
}