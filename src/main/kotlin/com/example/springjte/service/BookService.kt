package com.example.springjte.service

import com.example.springjte.dto.BookRequest
import com.example.springjte.dto.BookResponse
import com.example.springjte.model.Book
import com.example.springjte.model.Page
import com.example.springjte.repository.BookRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

/**
 * Book catalog use cases — the Spring equivalent of the ASP.NET Core
 * `BookRepository` plus the controller glue.
 *
 * Unlike [UserService] there is no password handling and no webhook fan-out: a
 * book is plain content, so the service stays a thin CRUD wrapper around
 * [BookRepository].
 */
@Service
class BookService(private val repo: BookRepository) {

    fun findAll(search: String? = null): List<BookResponse> =
        repo.findAll(search).map { toResponse(it) }

    fun findPage(search: String?, page: Int, size: Int): Page<BookResponse> {
        val safePage = page.coerceAtLeast(1)
        val safeSize = size.coerceIn(1, 100)
        val offset = (safePage - 1) * safeSize
        val items = repo.findPage(search, offset, safeSize).map { toResponse(it) }
        return Page(items = items, total = repo.count(search), page = safePage, size = safeSize)
    }

    fun findById(id: Long): BookResponse {
        val book = repo.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found")
        return toResponse(book)
    }

    fun count(): Long = repo.count()

    @Transactional
    fun create(request: BookRequest): BookResponse {
        val id = repo.save(
            Book(
                title = request.title.trim(),
                author = request.author.trim(),
                price = requirePrice(request)
            )
        )
        return toResponse(requireNotNull(repo.findById(id)) { "Book $id vanished after insert" })
    }

    @Transactional
    fun update(id: Long, request: BookRequest): BookResponse {
        repo.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found")
        repo.update(
            Book(
                id = id,
                title = request.title.trim(),
                author = request.author.trim(),
                price = requirePrice(request)
            )
        )
        return toResponse(requireNotNull(repo.findById(id)) { "Book $id vanished after update" })
    }

    @Transactional
    fun delete(id: Long) {
        if (repo.findById(id) == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found")
        }
        repo.deleteById(id)
    }

    /**
     * The whole catalog as RFC 4180 CSV, used by the "Export CSV" button.
     *
     * Stands in for the iTextSharp PDF and ClosedXML Excel reports of the
     * ASP.NET Core app without pulling two extra document libraries into this
     * project.
     */
    fun toCsv(): String {
        val out = StringBuilder("id,title,author,price\r\n")
        for (book in repo.findAll(null)) {
            out.append(book.id).append(',')
                .append(csvField(book.title)).append(',')
                .append(csvField(book.author)).append(',')
                .append(book.price).append("\r\n")
        }
        return out.toString()
    }

    /** Quotes a field only when it contains a delimiter, quote or newline. */
    private fun csvField(value: String): String =
        if (value.any { it == ',' || it == '"' || it == '\n' || it == '\r' }) {
            "\"" + value.replace("\"", "\"\"") + "\""
        } else {
            value
        }

    private fun requirePrice(request: BookRequest): Int =
        request.price ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Price is required")

    private fun toResponse(book: Book): BookResponse = BookResponse(
        id = book.id ?: 0L,
        title = book.title,
        author = book.author,
        price = book.price
    )
}
