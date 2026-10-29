package com.example.springjte.dto

data class AuditLogResponse(
    val id: Long,
    val actor: String,
    val action: String,
    val resource: String,
    val resourceId: String?,
    val details: String?,
    val ipAddress: String?,
    val createdAt: String?
)
