package com.example.springjte.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ApiKeyRequest(
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    val name: String,

    val userEmail: String? = null
)
