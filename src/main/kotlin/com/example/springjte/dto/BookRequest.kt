package com.example.springjte.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

/**
 * Create/update payload for a book.
 *
 * [price] is nullable so that a missing field fails `@NotNull` with a readable
 * message instead of silently defaulting to 0 — the ASP.NET Core model bound
 * `int Price`, which quietly turned a blank input into 0.
 */
data class BookRequest(
    @field:NotBlank(message = "Title is required")
    @field:Size(max = 200, message = "Title must be at most 200 characters")
    val title: String,

    @field:NotBlank(message = "Author is required")
    @field:Size(max = 120, message = "Author must be at most 120 characters")
    val author: String,

    @field:NotNull(message = "Price is required")
    @field:Min(value = 0, message = "Price cannot be negative")
    @field:Max(value = 1_000_000, message = "Price must be at most 1000000")
    val price: Int?
)
