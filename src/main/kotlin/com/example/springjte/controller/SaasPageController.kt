package com.example.springjte.controller

import com.example.springjte.dto.DashboardSummary
import com.example.springjte.dto.WebhookDeliveryResponse
import com.example.springjte.repository.UserRepository
import com.example.springjte.service.AuditService
import com.example.springjte.service.ApiKeyService
import com.example.springjte.service.UsageService
import com.example.springjte.service.WebhookDeliveryService
import com.example.springjte.service.WebhookService
import com.example.springjte.web.WebUtils
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * Server-rendered pages for the SaaS management console:
 * dashboard, API keys, webhooks, audit logs, and usage analytics.
 *
 * All routes are admin-only (enforced in SecurityConfig).
 */
@Controller
@RequestMapping("/saas")
class SaasPageController(
    private val userRepo: UserRepository,
    private val apiKeyService: ApiKeyService,
    private val webhookService: WebhookService,
    private val auditService: AuditService,
    private val usageService: UsageService,
    private val webhookDeliveryService: WebhookDeliveryService
) {

    @GetMapping("/dashboard")
    fun dashboard(model: Model, request: HttpServletRequest): String {
        addCommon(model, request)

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

        val summary = DashboardSummary(
            totalUsers = userRepo.count(),
            totalApiKeys = apiKeyService.count(),
            activeWebhooks = webhookService.countActive(),
            totalAuditLogs = auditService.count(),
            recentAuditLogs = auditService.findRecent(10),
            usageSummary = usageService.getSummary(),
            recentWebhookDeliveries = recentDeliveries
        )

        model.addAttribute("summary", summary)
        return "saas/dashboard"
    }

    @GetMapping("/api-keys")
    fun apiKeys(model: Model, request: HttpServletRequest): String {
        addCommon(model, request)
        model.addAttribute("apiKeys", apiKeyService.findAll())
        return "saas/api-keys"
    }

    @GetMapping("/webhooks")
    fun webhooks(model: Model, request: HttpServletRequest): String {
        addCommon(model, request)
        model.addAttribute("webhooks", webhookService.findAll())
        return "saas/webhooks"
    }

    @GetMapping("/audit-logs")
    fun auditLogs(
        @RequestParam(required = false) action: String?,
        @RequestParam(required = false) actor: String?,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        model: Model,
        request: HttpServletRequest
    ): String {
        addCommon(model, request)
        val (items, total) = when {
            action != null -> auditService.findByAction(action, page, size)
            actor != null -> auditService.findByActor(actor, page, size)
            else -> auditService.findAll(page, size)
        }
        model.addAttribute("auditLogs", items)
        model.addAttribute("total", total)
        model.addAttribute("page", page)
        model.addAttribute("size", size)
        model.addAttribute("filterAction", action ?: "")
        model.addAttribute("filterActor", actor ?: "")
        return "saas/audit-logs"
    }

    @GetMapping("/usage")
    fun usage(model: Model, request: HttpServletRequest): String {
        addCommon(model, request)
        model.addAttribute("usageSummary", usageService.getSummary())
        return "saas/usage"
    }

    private fun addCommon(model: Model, request: HttpServletRequest) {
        model.addAttribute("contextPath", request.contextPath)
        model.addAttribute("csrf", WebUtils.csrfToken(request)?.token)
        model.addAttribute("csrfParam", "_csrf")
        model.addAttribute("currentUser", request.userPrincipal?.name ?: "")
    }
}
