package com.example.springjte.model

/**
 * One row of the `books` table.
 *
 * Ported from the ASP.NET Core `BookViewModel`. Properties are `var` for the same
 * reason as [User]: MyBatis instantiates the entity through a no-arg constructor
 * and fills it via setters, and `BookMapper.insert` writes the generated key back
 * into [id] (`useGeneratedKeys` + `keyProperty="id"`).
 *
 * [price] mirrors the original `int Price` column — a whole number, not a decimal.
 */
data class Book(
    var id: Long? = null,
    var title: String = "",
    var author: String = "",
    var price: Int = 0
)
