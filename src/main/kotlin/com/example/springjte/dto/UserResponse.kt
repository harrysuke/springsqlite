package com.example.springjte.dto

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val role: String?
)