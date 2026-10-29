package com.example.springjte.repository.mybatis

import com.example.springjte.model.Webhook
import com.example.springjte.repository.WebhookRepository
import org.springframework.stereotype.Repository

@Repository
class MyBatisWebhookRepository(private val mapper: WebhookMapper) : WebhookRepository {

    override fun findAll(): List<Webhook> = mapper.findAll()

    override fun findActive(): List<Webhook> = mapper.findActive()

    override fun findByEventTypesContaining(eventType: String): List<Webhook> = mapper.findByEventTypesContaining(eventType)

    override fun findById(id: Long): Webhook? = mapper.findById(id)

    override fun save(webhook: Webhook): Long {
        mapper.insert(webhook)
        return webhook.id ?: throw IllegalStateException("No generated key returned for webhook insert")
    }

    override fun update(webhook: Webhook): Int = mapper.update(webhook)

    override fun deleteById(id: Long): Int = mapper.deleteById(id)

    override fun countActive(): Long = mapper.countActive()
}
