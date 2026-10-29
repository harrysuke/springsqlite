package com.example.springjte.repository

import com.example.springjte.model.ApiKey
import com.example.springjte.model.AuditLog
import com.example.springjte.model.UsageEvent
import com.example.springjte.model.Webhook
import com.example.springjte.model.WebhookDelivery
import java.util.concurrent.atomic.AtomicLong

/**
 * In-memory test doubles for the SaaS repositories.
 * These avoid database/MyBatis dependencies in unit tests.
 */

class InMemoryApiKeyRepository : ApiKeyRepository {
    private val store = LinkedHashMap<Long, ApiKey>()
    private val ids = AtomicLong(0)

    override fun findAll(): List<ApiKey> = store.values.toList()
    override fun findActive(): List<ApiKey> = store.values.filter { it.active }
    override fun findById(id: Long): ApiKey? = store[id]
    override fun findByKeyValue(keyValue: String): ApiKey? =
        store.values.firstOrNull { it.keyValue == keyValue && it.active }
    override fun save(apiKey: ApiKey): Long {
        val id = apiKey.id ?: ids.incrementAndGet()
        store[id] = apiKey.copy(id = id)
        return id
    }
    override fun update(apiKey: ApiKey): Int {
        val id = apiKey.id ?: return 0
        if (store.containsKey(id)) { store[id] = apiKey.copy(id = id); return 1 }
        return 0
    }
    override fun updateLastUsed(id: Long): Int = if (store.containsKey(id)) 1 else 0
    override fun deleteById(id: Long): Int = if (store.remove(id) != null) 1 else 0
    override fun count(): Long = store.size.toLong()
    fun clear() { store.clear(); ids.set(0) }
}

class InMemoryWebhookRepository : WebhookRepository {
    private val store = LinkedHashMap<Long, Webhook>()
    private val ids = AtomicLong(0)

    override fun findAll(): List<Webhook> = store.values.toList()
    override fun findActive(): List<Webhook> = store.values.filter { it.active }
    override fun findByEventTypesContaining(eventType: String): List<Webhook> =
        store.values.filter { it.active && (it.eventTypes == "*" || it.eventTypes.contains(eventType)) }
    override fun findById(id: Long): Webhook? = store[id]
    override fun save(webhook: Webhook): Long {
        val id = webhook.id ?: ids.incrementAndGet()
        store[id] = webhook.copy(id = id)
        return id
    }
    override fun update(webhook: Webhook): Int {
        val id = webhook.id ?: return 0
        if (store.containsKey(id)) { store[id] = webhook.copy(id = id); return 1 }
        return 0
    }
    override fun deleteById(id: Long): Int = if (store.remove(id) != null) 1 else 0
    override fun countActive(): Long = store.values.count { it.active }.toLong()
    fun clear() { store.clear(); ids.set(0) }
}

class InMemoryWebhookDeliveryRepository : WebhookDeliveryRepository {
    private val store = LinkedHashMap<Long, WebhookDelivery>()
    private val ids = AtomicLong(0)

    override fun findByWebhookId(webhookId: Long): List<WebhookDelivery> =
        store.values.filter { it.webhookId == webhookId }
    override fun findRecent(limit: Int): List<WebhookDelivery> =
        store.values.toList().takeLast(limit).reversed()
    override fun save(delivery: WebhookDelivery): Long {
        val id = delivery.id ?: ids.incrementAndGet()
        store[id] = delivery.copy(id = id)
        return id
    }
    override fun update(delivery: WebhookDelivery): Int {
        val id = delivery.id ?: return 0
        if (store.containsKey(id)) { store[id] = delivery.copy(id = id); return 1 }
        return 0
    }
    override fun countFailed(): Long = store.values.count { !it.success }.toLong()
    fun clear() { store.clear(); ids.set(0) }
}

class InMemoryUsageEventRepository : UsageEventRepository {
    private val store = mutableListOf<UsageEvent>()
    private val ids = AtomicLong(0)

    override fun save(event: UsageEvent): Long {
        val id = event.id ?: ids.incrementAndGet()
        store.add(event.copy(id = id))
        return id
    }
    override fun findRecent(limit: Int): List<UsageEvent> = store.takeLast(limit).reversed()
    override fun countAll(): Long = store.size.toLong()
    override fun countByEndpoint(): List<Map<String, Any>> =
        store.groupBy { it.endpoint ?: "unknown" }
            .map { mapOf("endpoint" to it.key, "cnt" to it.value.size.toLong()) }
    override fun countByMethod(): List<Map<String, Any>> =
        store.groupBy { it.method ?: "unknown" }
            .map { mapOf("method" to it.key, "cnt" to it.value.size.toLong()) }
    override fun countByStatus(): List<Map<String, Any>> =
        store.groupBy { (it.statusCode ?: 0).toString() }
            .map { mapOf("status" to it.key, "cnt" to it.value.size.toLong()) }
    override fun countDaily(days: Int): List<Map<String, Any>> =
        store.groupBy { it.createdAt?.substringBefore(" ") ?: "" }
            .map { mapOf("date" to it.key, "cnt" to it.value.size.toLong()) }
    fun clear() { store.clear(); ids.set(0) }
}

class InMemoryAuditLogRepository : AuditLogRepository {
    private val store = mutableListOf<AuditLog>()
    private val ids = AtomicLong(0)

    override fun findAll(offset: Int, limit: Int): List<AuditLog> =
        store.reversed().drop(offset).take(limit)
    override fun findByAction(action: String, offset: Int, limit: Int): List<AuditLog> =
        store.filter { it.action == action }.reversed().drop(offset).take(limit)
    override fun findByActor(actor: String, offset: Int, limit: Int): List<AuditLog> =
        store.filter { it.actor == actor }.reversed().drop(offset).take(limit)
    override fun findRecent(limit: Int): List<AuditLog> = store.reversed().take(limit)
    override fun save(auditLog: AuditLog): Long {
        val id = auditLog.id ?: ids.incrementAndGet()
        store.add(auditLog.copy(id = id))
        return id
    }
    override fun count(): Long = store.size.toLong()
    override fun countByAction(action: String): Long =
        store.count { it.action == action }.toLong()
    override fun countByActor(actor: String): Long =
        store.count { it.actor == actor }.toLong()
    fun clear() { store.clear(); ids.set(0) }
}
