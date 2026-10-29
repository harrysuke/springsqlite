package com.example.springjte.service

import com.example.springjte.dto.DailyCount
import com.example.springjte.dto.UsageEventResponse
import com.example.springjte.dto.UsageSummary
import com.example.springjte.model.UsageEvent
import com.example.springjte.repository.UsageEventRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UsageService(private val repo: UsageEventRepository) {

    @Transactional
    fun recordEvent(
        eventType: String,
        endpoint: String?,
        method: String?,
        userEmail: String?,
        apiKeyId: Long?,
        statusCode: Int?,
        durationMs: Long?
    ) {
        val event = UsageEvent(
            eventType = eventType,
            endpoint = endpoint,
            method = method,
            userEmail = userEmail,
            apiKeyId = apiKeyId,
            statusCode = statusCode,
            durationMs = durationMs
        )
        repo.save(event)
    }

    fun getSummary(): UsageSummary {
        val totalRequests = repo.countAll()
        val recentEvents = repo.findRecent(20).map { toResponse(it) }

        val requestsByEndpoint = repo.countByEndpoint().associate {
            (it["endpoint"] as? String ?: "unknown") to count(it["cnt"])
        }
        val requestsByMethod = repo.countByMethod().associate {
            (it["method"] as? String ?: "unknown") to count(it["cnt"])
        }
        val requestsByStatus = repo.countByStatus().associate {
            (it["status"] as? String ?: "unknown") to count(it["cnt"])
        }
        val dailyCounts = repo.countDaily(30).map {
            DailyCount(
                date = it["date"] as? String ?: "",
                count = count(it["cnt"])
            )
        }

        return UsageSummary(
            totalRequests = totalRequests,
            requestsByEndpoint = requestsByEndpoint,
            requestsByMethod = requestsByMethod,
            requestsByStatus = requestsByStatus,
            recentEvents = recentEvents,
            dailyCounts = dailyCounts
        )
    }

    fun findRecent(limit: Int): List<UsageEventResponse> =
        repo.findRecent(limit).map { toResponse(it) }

    /**
     * Reads an aggregate `COUNT(*)` cell out of a `resultType="java.util.Map"` row.
     *
     * The driver decides the Java type: SQLite hands back [Integer] while a value
     * fits in 32 bits and only widens to [Long] above that. Casting straight to
     * `Long` therefore yields null for ordinary counts, which used to flatten
     * every total to 0 — and a 0 chart maximum made the dashboard divide by zero.
     * Going through [Number] accepts either width.
     */
    private fun count(value: Any?): Long = (value as? Number)?.toLong() ?: 0L

    private fun toResponse(event: UsageEvent): UsageEventResponse = UsageEventResponse(
        id = event.id ?: 0L,
        eventType = event.eventType,
        endpoint = event.endpoint,
        method = event.method,
        userEmail = event.userEmail,
        statusCode = event.statusCode,
        durationMs = event.durationMs,
        createdAt = event.createdAt
    )
}
