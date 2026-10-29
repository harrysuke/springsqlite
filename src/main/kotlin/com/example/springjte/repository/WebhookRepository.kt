package com.example.springjte.repository

import com.example.springjte.model.Webhook

interface WebhookRepository {

    fun findAll(): List<Webhook>

    fun findActive(): List<Webhook>

    fun findByEventTypesContaining(eventType: String): List<Webhook>

    fun findById(id: Long): Webhook?

    fun save(webhook: Webhook): Long

    fun update(webhook: Webhook): Int

    fun deleteById(id: Long): Int

    fun countActive(): Long
}
