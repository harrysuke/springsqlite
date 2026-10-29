package com.example.springjte.model

/**
 * One row of the `audit_logs` table — who did what, when, from where.
 */
data class AuditLog(
    var id: Long? = null,
    var actor: String = "",
    var action: String = "",
    var resource: String = "",
    var resourceId: String? = null,
    var details: String? = null,
    var ipAddress: String? = null,
    var createdAt: String? = null
)
