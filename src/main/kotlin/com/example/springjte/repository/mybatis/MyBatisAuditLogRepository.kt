package com.example.springjte.repository.mybatis

import com.example.springjte.model.AuditLog
import com.example.springjte.repository.AuditLogRepository
import org.springframework.stereotype.Repository

@Repository
class MyBatisAuditLogRepository(private val mapper: AuditLogMapper) : AuditLogRepository {

    override fun findAll(offset: Int, limit: Int): List<AuditLog> = mapper.findAll(offset, limit)

    override fun findByAction(action: String, offset: Int, limit: Int): List<AuditLog> = mapper.findByAction(action, offset, limit)

    override fun findByActor(actor: String, offset: Int, limit: Int): List<AuditLog> = mapper.findByActor(actor, offset, limit)

    override fun findRecent(limit: Int): List<AuditLog> = mapper.findRecent(limit)

    override fun save(auditLog: AuditLog): Long {
        mapper.insert(auditLog)
        return auditLog.id ?: throw IllegalStateException("No generated key returned for audit_log insert")
    }

    override fun count(): Long = mapper.count()

    override fun countByAction(action: String): Long = mapper.countByAction(action)
}
