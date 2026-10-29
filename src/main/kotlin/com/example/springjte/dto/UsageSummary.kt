package com.example.springjte.dto

data class UsageSummary(
    val totalRequests: Long,
    val requestsByEndpoint: Map<String, Long>,
    val requestsByMethod: Map<String, Long>,
    val requestsByStatus: Map<String, Long>,
    val recentEvents: List<UsageEventResponse>,
    val dailyCounts: List<DailyCount>
)

data class UsageEventResponse(
    val id: Long,
    val eventType: String,
    val endpoint: String?,
    val method: String?,
    val userEmail: String?,
    val statusCode: Int?,
    val durationMs: Long?,
    val createdAt: String?
)

data class DailyCount(
    val date: String,
    val count: Long
)

data class DashboardSummary(
    val totalUsers: Long,
    val totalApiKeys: Long,
    val activeWebhooks: Long,
    val totalAuditLogs: Long,
    val recentAuditLogs: List<AuditLogResponse>,
    val usageSummary: UsageSummary,
    val recentWebhookDeliveries: List<WebhookDeliveryResponse>
)

data class WebhookDeliveryResponse(
    val id: Long,
    val webhookId: Long,
    val eventType: String,
    val success: Boolean,
    val responseStatus: Int?,
    val attemptCount: Int,
    val createdAt: String?
)
