package com.example.springjte.service

import com.example.springjte.dto.ApiKeyRequest
import com.example.springjte.dto.ApiKeyResponse
import com.example.springjte.model.ApiKey
import com.example.springjte.repository.ApiKeyRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.security.SecureRandom
import java.util.Base64

@Service
class ApiKeyService(private val repo: ApiKeyRepository) {

    @Transactional
    fun create(request: ApiKeyRequest, creatorEmail: String): ApiKeyResponse {
        val keyValue = generateApiKey()
        val apiKey = ApiKey(
            keyValue = keyValue,
            name = request.name.trim(),
            userEmail = creatorEmail
        )
        val id = repo.save(apiKey)
        return toResponse(requireNotNull(repo.findById(id)))
    }

    fun findAll(): List<ApiKeyResponse> = repo.findAll().map { toResponse(it) }

    fun findById(id: Long): ApiKeyResponse {
        val apiKey = repo.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "API key not found")
        return toResponse(apiKey)
    }

    @Transactional
    fun toggleActive(id: Long): ApiKeyResponse {
        val apiKey = repo.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "API key not found")
        apiKey.active = !apiKey.active
        repo.update(apiKey)
        return toResponse(requireNotNull(repo.findById(id)))
    }

    @Transactional
    fun delete(id: Long) {
        if (repo.findById(id) == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "API key not found")
        }
        repo.deleteById(id)
    }

    /**
     * Validates an API key value. Returns the ApiKey if valid and active, null otherwise.
     * Also updates the last_used_at timestamp.
     */
    fun validate(keyValue: String): ApiKey? {
        val apiKey = repo.findByKeyValue(keyValue) ?: return null
        repo.updateLastUsed(apiKey.id!!)
        return apiKey
    }

    fun count(): Long = repo.count()

    private fun generateApiKey(): String {
        val random = SecureRandom()
        val bytes = ByteArray(32)
        random.nextBytes(bytes)
        return "sk_" + Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

    private fun toResponse(apiKey: ApiKey): ApiKeyResponse = ApiKeyResponse(
        id = apiKey.id ?: 0L,
        name = apiKey.name,
        keyValue = apiKey.keyValue,
        userEmail = apiKey.userEmail,
        active = apiKey.active,
        createdAt = apiKey.createdAt,
        lastUsedAt = apiKey.lastUsedAt
    )
}
