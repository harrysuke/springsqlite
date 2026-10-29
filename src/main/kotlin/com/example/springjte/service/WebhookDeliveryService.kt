package com.example.springjte.service

import com.example.springjte.model.WebhookDelivery
import com.example.springjte.repository.WebhookDeliveryRepository
import com.example.springjte.repository.WebhookRepository
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestClient
import tools.jackson.databind.ObjectMapper

/**
 * Delivers webhook payloads to registered endpoints.
 *
 * On each call:
 * 1. Finds all active webhooks subscribed to the event type.
 * 2. POSTs the JSON payload to each webhook URL (with HMAC signature if a secret is set).
 * 3. Records a webhook_delivery row with the response status / body / success flag.
 *
 * Uses a simple synchronous delivery with a single retry on failure.
 */
@Service
class WebhookDeliveryService(
    private val webhookRepo: WebhookRepository,
    private val deliveryRepo: WebhookDeliveryRepository,
    private val objectMapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val restClient: RestClient = RestClient.builder()
        .requestFactory(SimpleClientHttpRequestFactory().apply {
            setConnectTimeout(10_000)
            setReadTimeout(10_000)
        })
        .build()

    @Transactional
    fun deliver(eventType: String, payload: Map<String, Any>) {
        val webhooks = webhookRepo.findByEventTypesContaining(eventType)
        if (webhooks.isEmpty()) return

        val jsonPayload = objectMapper.writeValueAsString(payload)

        for (webhook in webhooks) {
            val maxAttempts = 2
            var attempt = 0
            var success = false
            var responseStatus: Int? = null
            var responseBody: String? = null

            while (attempt < maxAttempts && !success) {
                attempt++
                try {
                    val response = restClient.post()
                        .uri(webhook.url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Webhook-Event", eventType)
                        .apply {
                            webhook.secret?.let { secret ->
                                header("X-Webhook-Signature", hmacSha256(jsonPayload, secret))
                            }
                        }
                        .body(jsonPayload)
                        .retrieve()
                        .toEntity(String::class.java)

                    responseStatus = response.statusCode.value()
                    responseBody = response.body?.take(500)
                    success = response.statusCode.is2xxSuccessful
                } catch (e: Exception) {
                    responseStatus = null
                    responseBody = e.message?.take(500)
                    logger.warn("Webhook delivery to {} failed (attempt {}): {}",
                        webhook.url, attempt, e.message)
                }
            }

            val delivery = WebhookDelivery(
                webhookId = webhook.id ?: 0L,
                eventType = eventType,
                payload = jsonPayload,
                responseStatus = responseStatus,
                responseBody = responseBody,
                success = success,
                attemptCount = attempt
            )
            deliveryRepo.save(delivery)
        }
    }

    fun findRecentDeliveries(limit: Int): List<WebhookDelivery> = deliveryRepo.findRecent(limit)

    fun findDeliveriesByWebhook(webhookId: Long): List<WebhookDelivery> =
        deliveryRepo.findByWebhookId(webhookId)

    private fun hmacSha256(data: String, key: String): String {
        val mac = javax.crypto.Mac.getInstance("HmacSHA256")
        val secretKey = javax.crypto.spec.SecretKeySpec(key.toByteArray(Charsets.UTF_8), "HmacSHA256")
        mac.init(secretKey)
        val hash = mac.doFinal(data.toByteArray(Charsets.UTF_8))
        return java.util.HexFormat.of().formatHex(hash)
    }
}
