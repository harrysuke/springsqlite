package com.example.springjte.controller

import com.example.springjte.dto.ApiKeyRequest
import com.example.springjte.dto.ApiKeyResponse
import com.example.springjte.service.ApiKeyService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/api-keys")
class ApiKeyApiController(private val service: ApiKeyService) {

    @GetMapping
    fun list(): List<ApiKeyResponse> = service.findAll()

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ApiKeyResponse = service.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: ApiKeyRequest, auth: Authentication): ApiKeyResponse =
        service.create(request, auth.name)

    @PutMapping("/{id}/toggle")
    fun toggle(@PathVariable id: Long): ApiKeyResponse = service.toggleActive(id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = service.delete(id)
}
