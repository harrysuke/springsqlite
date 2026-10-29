package com.example.springjte.service

import com.example.springjte.dto.UserRequest
import com.example.springjte.repository.InMemoryUserRepository
import com.example.springjte.repository.InMemoryWebhookRepository
import com.example.springjte.repository.InMemoryWebhookDeliveryRepository
import tools.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.server.ResponseStatusException

class UserServiceTest {

    private val repo = InMemoryUserRepository()
    private val encoder = BCryptPasswordEncoder()
    private val webhookRepo = InMemoryWebhookRepository()
    private val deliveryRepo = InMemoryWebhookDeliveryRepository()
    private val objectMapper = ObjectMapper()
    private val webhookDeliveryService = WebhookDeliveryService(webhookRepo, deliveryRepo, objectMapper)
    private val service = UserService(repo, encoder, webhookDeliveryService)

    @Test
    fun `create hashes the password and persists the user`() {
        val result = service.create(UserRequest(name = "Alice", email = "A@B.c", password = "secret12"))

        val saved = repo.findById(result.id)!!
        assertEquals(1L, result.id)
        assertEquals("a@b.c", result.email)
        assertEquals("a@b.c", saved.email)
        assertTrue(saved.password != "secret12")
        assertTrue(encoder.matches("secret12", saved.password))
    }

    @Test
    fun `create requires a password`() {
        assertThrows(ResponseStatusException::class.java) {
            service.create(UserRequest(name = "Alice", email = "a@b.c", password = ""))
        }
    }

    @Test
    fun `create rejects a duplicate email`() {
        repo.save(com.example.springjte.model.User(name = "Existing", email = "a@b.c", password = "x"))

        assertThrows(ResponseStatusException::class.java) {
            service.create(UserRequest(name = "Alice", email = "a@b.c", password = "secret12"))
        }
    }

    @Test
    fun `update keeps the existing password when blank`() {
        repo.save(com.example.springjte.model.User(id = 7L, name = "Old", email = "o@x.c", password = "oldHash"))

        service.update(7L, UserRequest(name = "New", email = "o@x.c", password = null))

        val updated = repo.findById(7L)!!
        assertEquals("New", updated.name)
        assertEquals("oldHash", updated.password)
    }

    @Test
    fun `delete of a missing user throws 404 and does not delete`() {
        assertThrows(ResponseStatusException::class.java) { service.delete(999L) }

        assertTrue(repo.findById(999L) == null)
    }
}
