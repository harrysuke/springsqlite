package com.example.springjte.repository.mybatis

import com.example.springjte.model.UsageEvent
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface UsageEventMapper {

    fun insert(event: UsageEvent): Int

    fun findRecent(@Param("limit") limit: Int): List<UsageEvent>

    fun countAll(): Long

    fun countByEndpoint(): List<Map<String, Any>>

    fun countByMethod(): List<Map<String, Any>>

    fun countByStatus(): List<Map<String, Any>>

    fun countDaily(@Param("days") days: Int): List<Map<String, Any>>
}
