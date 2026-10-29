package com.example.springjte.repository.mybatis

import com.example.springjte.model.Book
import com.example.springjte.repository.BookRepository
import org.springframework.stereotype.Repository

/**
 * MyBatis-backed [BookRepository].
 *
 * All SQL lives in `mappers/BookMapper.xml`; this class only adapts the mapper to
 * the repository contract — turning a raw search term into a LIKE pattern and
 * surfacing the generated key from `Book.id` after an insert.
 */
@Repository
class MyBatisBookRepository(private val mapper: BookMapper) : BookRepository {

    override fun findAll(search: String?): List<Book> = mapper.findAll(pattern(search))

    override fun findPage(search: String?, offset: Int, limit: Int): List<Book> =
        mapper.findPage(pattern(search), limit, offset)

    override fun count(search: String?): Long = mapper.count(pattern(search))

    override fun findById(id: Long): Book? = mapper.findById(id)

    override fun save(book: Book): Long {
        mapper.insert(book)
        return book.id
            ?: throw IllegalStateException("No generated key returned for book insert")
    }

    override fun update(book: Book): Int = mapper.update(book)

    override fun deleteById(id: Long): Int = mapper.deleteById(id)

    /**
     * `%keyword%`, lower-cased, or null when there is nothing to search for.
     *
     * The term is lower-cased because the SQL compares `lower(title)` /
     * `lower(author)` — SQLite's LIKE is only case-insensitive for ASCII, so
     * folding both sides keeps the match predictable. `\`, `%` and `_` are
     * escaped so `ESCAPE '\'` treats them as literals; otherwise searching for
     * `%` would match the whole catalog.
     */
    private fun pattern(search: String?): String? {
        val keyword = search?.trim()?.takeIf { it.isNotEmpty() } ?: return null
        val escaped = keyword
            .lowercase()
            .replace("\\", "\\\\")
            .replace("%", "\\%")
            .replace("_", "\\_")
        return "%$escaped%"
    }
}
