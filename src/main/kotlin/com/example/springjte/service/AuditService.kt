package com.example.springjte.service

import com.example.springjte.dto.AuditLogResponse
import com.example.springjte.model.AuditLog
import com.example.springjte.repository.AuditLogRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuditService(private val repo: AuditLogRepository) {

    @Transactional
    fun log(
        actor: String,
        action: String,
        resource: String,
        resourceId: String? = null,
        details: String? = null,
        ipAddress: String? = null
    ) {
        val entry = AuditLog(
            actor = actor,
            action = action,
            resource = resource,
            resourceId = resourceId,
            details = details,
            ipAddress = ipAddress
        )
        repo.save(entry)
    }

    fun findAll(page: Int, size: Int): Pair<List<AuditLogResponse>, Long> {
        val safePage = page.coerceAtLeast(1)
        val safeSize = size.coerceIn(1, 200)
        val offset = (safePage - 1) * safeSize
        val items = repo.findAll(offset, safeSize).map { toResponse(it) }
        val total = repo.count()
        return items to total
    }

    fun findByAction(action: String, page: Int, size: Int): Pair<List<AuditLogResponse>, Long> {
        val safePage = page.coerceAtLeast(1)
        val safeSize = size.coerceIn(1, 200)
        val offset = (safePage - 1) * safeSize
        val items = repo.findByAction(action, offset, safeSize).map { toResponse(it) }
        val total = repo.countByAction(action)
        return items to total
    }

    fun findByActor(actor: String, page: Int, size: Int): Pair<List<AuditLogResponse>, Long> {
        val safePage = page.coerceAtLeast(1)
        val safeSize = size.coerceIn(1, 200)
        val offset = (safePage - 1) * safeSize
        val items = repo.findByActor(actor, offset, safeSize).map { toResponse(it) }
        val total = repo.countByActor(actor)
        return items to total
    }

    fun findRecent(limit: Int): List<AuditLogResponse> =
        repo.findRecent(limit).map { toResponse(it) }

    fun count(): Long = repo.count()

    private fun toResponse(log: AuditLog): AuditLogResponse = AuditLogResponse(
        id = log.id ?: 0L,
        actor = log.actor,
        action = log.action,
        resource = log.resource,
        resourceId = log.resourceId,
        details = log.details,
        ipAddress = log.ipAddress,
        createdAt = log.createdAt
    )
}
