package com.example.springjte.dto

data class ApiKeyResponse(
    val id: Long,
    val name: String,
    val keyValue: String,
    val userEmail: String,
    val active: Boolean,
    val createdAt: String?,
    val lastUsedAt: String?
)
