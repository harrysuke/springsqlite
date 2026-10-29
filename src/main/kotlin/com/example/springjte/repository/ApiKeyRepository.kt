package com.example.springjte.repository

import com.example.springjte.model.ApiKey

interface ApiKeyRepository {

    fun findAll(): List<ApiKey>

    fun findActive(): List<ApiKey>

    fun findById(id: Long): ApiKey?

    fun findByKeyValue(keyValue: String): ApiKey?

    fun save(apiKey: ApiKey): Long

    fun update(apiKey: ApiKey): Int

    fun updateLastUsed(id: Long): Int

    fun deleteById(id: Long): Int

    fun count(): Long
}
