package com.example.springjte.web

import com.example.springjte.service.AuditService
import com.example.springjte.service.UsageService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

/**
 * Intercepts API requests to:
 * 1. Record a usage event for every /api/ call (Usage Analytics & Reporting).
 * 2. Record an audit log entry for every mutation (POST/PUT/DELETE) on /api/ (Audit Logs).
 *
 * Both happen AFTER the handler runs successfully, using postHandle so we have the
 * status code. Failed requests still get a usage event with the error status.
 */
@Component
class SaasInterceptor(
    private val auditService: AuditService,
    private val usageService: UsageService
) : HandlerInterceptor {

    private val mutableMethods = setOf("POST", "PUT", "DELETE", "PATCH")

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        val path = request.requestURI
        if (!path.startsWith("/api/")) return

        val method = request.method
        val statusCode = response.status
        val userEmail = request.userPrincipal?.name ?: "anonymous"
        val startTime = request.getAttribute("__startTime") as? Long ?: System.currentTimeMillis()
        val durationMs = System.currentTimeMillis() - startTime

        // --- Usage event ---
        runCatching {
            usageService.recordEvent(
                eventType = "api_request",
                endpoint = path,
                method = method,
                userEmail = userEmail,
                apiKeyId = null,
                statusCode = statusCode,
                durationMs = durationMs
            )
        }

        // --- Audit log for mutations ---
        if (method in mutableMethods) {
            val action = when (method) {
                "POST" -> "CREATE"
                "PUT", "PATCH" -> "UPDATE"
                "DELETE" -> "DELETE"
                else -> method
            }
            val resource = path.removePrefix("/api/").substringBefore("/")
            val resourceId = path.split("/").lastOrNull()?.takeIf { it.matches(Regex("\\d+")) }

            runCatching {
                auditService.log(
                    actor = userEmail,
                    action = action,
                    resource = resource,
                    resourceId = resourceId,
                    details = "$method $path",
                    ipAddress = request.remoteAddr
                )
            }
        }
    }

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        request.setAttribute("__startTime", System.currentTimeMillis())
        return true
    }
}
