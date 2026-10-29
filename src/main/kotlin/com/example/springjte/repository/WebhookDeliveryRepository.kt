package com.example.springjte.repository

import com.example.springjte.model.WebhookDelivery

interface WebhookDeliveryRepository {

    fun findByWebhookId(webhookId: Long): List<WebhookDelivery>

    fun findRecent(limit: Int): List<WebhookDelivery>

    fun save(delivery: WebhookDelivery): Long

    fun update(delivery: WebhookDelivery): Int

    fun countFailed(): Long
}
