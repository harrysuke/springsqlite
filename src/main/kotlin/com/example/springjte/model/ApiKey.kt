package com.example.springjte.model

/**
 * One row of the `api_keys` table.
 *
 * Properties are `var` for the same reason as [User]: MyBatis instantiates via
 * no-arg constructor and fills through setters.
 */
data class ApiKey(
    var id: Long? = null,
    var keyValue: String = "",
    var name: String = "",
    var userEmail: String = "",
    var active: Boolean = true,
    var createdAt: String? = null,
    var lastUsedAt: String? = null
)
