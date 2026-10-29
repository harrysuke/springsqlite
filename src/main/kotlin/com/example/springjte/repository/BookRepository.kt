package com.example.springjte.repository

import com.example.springjte.model.Book

/**
 * Persistence contract for the `books` table.
 *
 * Mirrors the ASP.NET Core `IBookRepository`, with the paginated variants the rest
 * of this project uses. Production uses the MyBatis implementation
 * ([com.example.springjte.repository.mybatis.MyBatisBookRepository]); tests can
 * swap in a double without touching the service or the controllers.
 */
interface BookRepository {

    /** All books, ordered by id; [search] matches title or author when not blank. */
    fun findAll(search: String? = null): List<Book>

    /** One page of [findAll], starting at [offset]. */
    fun findPage(search: String?, offset: Int, limit: Int): List<Book>

    /** Number of rows [findAll] would return for [search]. */
    fun count(search: String? = null): Long

    fun findById(id: Long): Book?

    /** Inserts [book] and returns the generated primary key. */
    fun save(book: Book): Long

    /** Updates every column except the key; returns the affected row count. */
    fun update(book: Book): Int

    fun deleteById(id: Long): Int
}
