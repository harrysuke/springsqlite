package com.example.springjte.service

import com.example.springjte.dto.WebhookRequest
import com.example.springjte.dto.WebhookResponse
import com.example.springjte.model.Webhook
import com.example.springjte.repository.WebhookRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class WebhookService(private val repo: WebhookRepository) {

    @Transactional
    fun create(request: WebhookRequest): WebhookResponse {
        val webhook = Webhook(
            url = request.url.trim(),
            description = request.description?.trim(),
            eventTypes = request.eventTypes.trim().takeIf { it.isNotEmpty() } ?: "*",
            secret = request.secret
        )
        val id = repo.save(webhook)
        return toResponse(requireNotNull(repo.findById(id)))
    }

    fun findAll(): List<WebhookResponse> = repo.findAll().map { toResponse(it) }

    fun findById(id: Long): WebhookResponse {
        val webhook = repo.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Webhook not found")
        return toResponse(webhook)
    }

    @Transactional
    fun update(id: Long, request: WebhookRequest): WebhookResponse {
        val existing = repo.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Webhook not found")
        existing.url = request.url.trim()
        existing.description = request.description?.trim()
        existing.eventTypes = request.eventTypes.trim().takeIf { it.isNotEmpty() } ?: "*"
        existing.secret = request.secret
        repo.update(existing)
        return toResponse(requireNotNull(repo.findById(id)))
    }

    @Transactional
    fun toggleActive(id: Long): WebhookResponse {
        val webhook = repo.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Webhook not found")
        webhook.active = !webhook.active
        repo.update(webhook)
        return toResponse(requireNotNull(repo.findById(id)))
    }

    @Transactional
    fun delete(id: Long) {
        if (repo.findById(id) == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Webhook not found")
        }
        repo.deleteById(id)
    }

    fun findForEventType(eventType: String): List<Webhook> = repo.findByEventTypesContaining(eventType)

    fun countActive(): Long = repo.countActive()

    private fun toResponse(webhook: Webhook): WebhookResponse = WebhookResponse(
        id = webhook.id ?: 0L,
        url = webhook.url,
        description = webhook.description,
        eventTypes = webhook.eventTypes,
        active = webhook.active,
        secret = webhook.secret,
        createdAt = webhook.createdAt
    )
}
