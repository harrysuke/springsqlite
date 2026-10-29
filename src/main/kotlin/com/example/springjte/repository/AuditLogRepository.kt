package com.example.springjte.repository

import com.example.springjte.model.AuditLog

interface AuditLogRepository {

    fun findAll(offset: Int, limit: Int): List<AuditLog>

    fun findByAction(action: String, offset: Int, limit: Int): List<AuditLog>

    fun findByActor(actor: String, offset: Int, limit: Int): List<AuditLog>

    fun findRecent(limit: Int): List<AuditLog>

    fun save(auditLog: AuditLog): Long

    fun count(): Long

    fun countByAction(action: String): Long

    fun countByActor(actor: String): Long
}
