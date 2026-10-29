package com.example.springjte.repository.mybatis

import com.example.springjte.model.WebhookDelivery
import com.example.springjte.repository.WebhookDeliveryRepository
import org.springframework.stereotype.Repository

@Repository
class MyBatisWebhookDeliveryRepository(private val mapper: WebhookDeliveryMapper) : WebhookDeliveryRepository {

    override fun findByWebhookId(webhookId: Long): List<WebhookDelivery> = mapper.findByWebhookId(webhookId)

    override fun findRecent(limit: Int): List<WebhookDelivery> = mapper.findRecent(limit)

    override fun save(delivery: WebhookDelivery): Long {
        mapper.insert(delivery)
        return delivery.id ?: throw IllegalStateException("No generated key returned for webhook_delivery insert")
    }

    override fun update(delivery: WebhookDelivery): Int = mapper.update(delivery)

    override fun countFailed(): Long = mapper.countFailed()
}
