package com.example.springjte.controller

import com.example.springjte.dto.AuditLogResponse
import com.example.springjte.service.AuditService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/audit-logs")
class AuditLogApiController(private val service: AuditService) {

    @GetMapping
    fun list(
        @RequestParam(required = false) action: String?,
        @RequestParam(required = false) actor: String?,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): Map<String, Any> {
        val (items, total) = when {
            action != null -> service.findByAction(action, page, size)
            actor != null -> service.findByActor(actor, page, size)
            else -> service.findAll(page, size)
        }
        return mapOf(
            "items" to items,
            "total" to total,
            "page" to page,
            "size" to size
        )
    }

    @GetMapping("/recent")
    fun recent(@RequestParam(defaultValue = "20") limit: Int): List<AuditLogResponse> =
        service.findRecent(limit)
}
