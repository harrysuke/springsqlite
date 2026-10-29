package com.example.springjte.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class WebhookRequest(
    @field:NotBlank(message = "URL is required")
    val url: String,

    val description: String? = null,

    val eventTypes: String = "*",

    val secret: String? = null
)
