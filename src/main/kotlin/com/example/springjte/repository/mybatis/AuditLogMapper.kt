package com.example.springjte.repository.mybatis

import com.example.springjte.model.AuditLog
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface AuditLogMapper {

    fun findAll(@Param("offset") offset: Int, @Param("limit") limit: Int): List<AuditLog>

    fun findByAction(@Param("action") action: String, @Param("offset") offset: Int, @Param("limit") limit: Int): List<AuditLog>

    fun findByActor(@Param("actor") actor: String, @Param("offset") offset: Int, @Param("limit") limit: Int): List<AuditLog>

    fun findRecent(@Param("limit") limit: Int): List<AuditLog>

    fun insert(auditLog: AuditLog): Int

    fun count(): Long

    fun countByAction(@Param("action") action: String): Long
}
