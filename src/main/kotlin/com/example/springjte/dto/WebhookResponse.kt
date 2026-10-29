package com.example.springjte.dto

data class WebhookResponse(
    val id: Long,
    val url: String,
    val description: String?,
    val eventTypes: String,
    val active: Boolean,
    val secret: String?,
    val createdAt: String?
)
