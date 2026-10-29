package com.example.springjte.controller

import com.example.springjte.dto.BookRequest
import com.example.springjte.dto.BookResponse
import com.example.springjte.service.BookService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * JSON catalog API — the counterpart of the ASP.NET Core `RestController` book
 * actions (GetBooks / AddBook / UpdateBook / DeleteBook).
 *
 * Requests under the `api` tree are also picked up by
 * [com.example.springjte.web.SaasInterceptor], so every call lands in usage
 * analytics and every mutation in the audit log.
 */
@RestController
@RequestMapping("/api/books")
class BookApiController(private val service: BookService) {

    @GetMapping
    fun list(@RequestParam(required = false) search: String?): List<BookResponse> =
        service.findAll(search)

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): BookResponse = service.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: BookRequest): BookResponse = service.create(request)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: BookRequest): BookResponse =
        service.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        service.delete(id)
    }
}
