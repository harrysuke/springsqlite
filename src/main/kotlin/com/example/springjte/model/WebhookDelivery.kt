package com.example.springjte.model

/**
 * One row of the `webhook_deliveries` table — a record of each delivery attempt.
 */
data class WebhookDelivery(
    var id: Long? = null,
    var webhookId: Long = 0L,
    var eventType: String = "",
    var payload: String = "",
    var responseStatus: Int? = null,
    var responseBody: String? = null,
    var success: Boolean = false,
    var attemptCount: Int = 1,
    var createdAt: String? = null
)
