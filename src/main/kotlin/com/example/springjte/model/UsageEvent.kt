package com.example.springjte.model

/**
 * One row of the `usage_events` table — append-only log of API activity.
 */
data class UsageEvent(
    var id: Long? = null,
    var eventType: String = "",
    var endpoint: String? = null,
    var method: String? = null,
    var userEmail: String? = null,
    var apiKeyId: Long? = null,
    var statusCode: Int? = null,
    var durationMs: Long? = null,
    var createdAt: String? = null
)
