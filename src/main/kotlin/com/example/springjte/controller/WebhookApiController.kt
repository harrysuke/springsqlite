package com.example.springjte.controller

import com.example.springjte.dto.WebhookDeliveryResponse
import com.example.springjte.dto.WebhookRequest
import com.example.springjte.dto.WebhookResponse
import com.example.springjte.service.WebhookDeliveryService
import com.example.springjte.service.WebhookService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/webhooks")
class WebhookApiController(
    private val service: WebhookService,
    private val deliveryService: WebhookDeliveryService
) {

    @GetMapping
    fun list(): List<WebhookResponse> = service.findAll()

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): WebhookResponse = service.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: WebhookRequest): WebhookResponse =
        service.create(request)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: WebhookRequest): WebhookResponse =
        service.update(id, request)

    @PutMapping("/{id}/toggle")
    fun toggle(@PathVariable id: Long): WebhookResponse = service.toggleActive(id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = service.delete(id)

    @GetMapping("/{id}/deliveries")
    fun deliveries(@PathVariable id: Long): List<WebhookDeliveryResponse> =
        deliveryService.findDeliveriesByWebhook(id).map {
            WebhookDeliveryResponse(
                id = it.id ?: 0L,
                webhookId = it.webhookId,
                eventType = it.eventType,
                success = it.success,
                responseStatus = it.responseStatus,
                attemptCount = it.attemptCount,
                createdAt = it.createdAt
            )
        }
}
