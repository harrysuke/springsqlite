package com.example.springjte.model

/**
 * One row of the `webhooks` table.
 */
data class Webhook(
    var id: Long? = null,
    var url: String = "",
    var description: String? = null,
    var eventTypes: String = "*",
    var active: Boolean = true,
    var secret: String? = null,
    var createdAt: String? = null
)
