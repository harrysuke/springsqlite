package com.example.springjte.controller

import com.example.springjte.dto.DashboardSummary
import com.example.springjte.dto.WebhookDeliveryResponse
import com.example.springjte.repository.UserRepository
import com.example.springjte.service.AuditService
import com.example.springjte.service.ApiKeyService
import com.example.springjte.service.UsageService
import com.example.springjte.service.WebhookDeliveryService
import com.example.springjte.service.WebhookService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dashboard")
class DashboardApiController(
    private val userRepo: UserRepository,
    private val apiKeyService: ApiKeyService,
    private val webhookService: WebhookService,
    private val auditService: AuditService,
    private val usageService: UsageService,
    private val webhookDeliveryService: WebhookDeliveryService
) {

    @GetMapping
    fun summary(): DashboardSummary {
        val recentDeliveries = webhookDeliveryService.findRecentDeliveries(10).map {
            WebhookDeliveryResponse(
                id = it.id ?: 0L,
                webhookId = it.webhookId,
                eventType = it.eventType,
                success = it.success,
                responseStatus = it.responseStatus,
                attemptCount = it.attemptCount,
                createdAt = it.createdAt
            )
        }

        return DashboardSummary(
            totalUsers = userRepo.count(),
            totalApiKeys = apiKeyService.count(),
            activeWebhooks = webhookService.countActive(),
            totalAuditLogs = auditService.count(),
            recentAuditLogs = auditService.findRecent(10),
            usageSummary = usageService.getSummary(),
            recentWebhookDeliveries = recentDeliveries
        )
    }
}
