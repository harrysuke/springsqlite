package com.example.springjte.repository

import com.example.springjte.model.UsageEvent

interface UsageEventRepository {

    fun save(event: UsageEvent): Long

    fun findRecent(limit: Int): List<UsageEvent>

    fun countAll(): Long

    fun countByEndpoint(): List<Map<String, Any>>

    fun countByMethod(): List<Map<String, Any>>

    fun countByStatus(): List<Map<String, Any>>

    fun countDaily(days: Int): List<Map<String, Any>>
}
