package com.example.springjte.controller

import com.example.springjte.dto.UsageSummary
import com.example.springjte.service.UsageService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/usage")
class UsageApiController(private val service: UsageService) {

    @GetMapping("/summary")
    fun summary(): UsageSummary = service.getSummary()

    @GetMapping("/recent")
    fun recent(@RequestParam(defaultValue = "50") limit: Int) = service.findRecent(limit)
}
