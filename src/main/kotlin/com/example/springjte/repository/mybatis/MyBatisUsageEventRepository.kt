package com.example.springjte.repository.mybatis

import com.example.springjte.model.UsageEvent
import com.example.springjte.repository.UsageEventRepository
import org.springframework.stereotype.Repository

@Repository
class MyBatisUsageEventRepository(private val mapper: UsageEventMapper) : UsageEventRepository {

    override fun save(event: UsageEvent): Long {
        mapper.insert(event)
        return event.id ?: throw IllegalStateException("No generated key returned for usage_event insert")
    }

    override fun findRecent(limit: Int): List<UsageEvent> = mapper.findRecent(limit)

    override fun countAll(): Long = mapper.countAll()

    override fun countByEndpoint(): List<Map<String, Any>> = mapper.countByEndpoint()

    override fun countByMethod(): List<Map<String, Any>> = mapper.countByMethod()

    override fun countByStatus(): List<Map<String, Any>> = mapper.countByStatus()

    override fun countDaily(days: Int): List<Map<String, Any>> = mapper.countDaily(days)
}
