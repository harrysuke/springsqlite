package com.example.springjte.repository.mybatis

import com.example.springjte.model.Book
import com.example.springjte.repository.BookRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import java.nio.file.Files
import java.nio.file.Path

/**
 * Runs the real `mappers/BookMapper.xml` against a real SQLite file.
 *
 * Complements [MyBatisUserRepositoryTest]: the V4 migration seeds six demo books,
 * so every assertion is scoped to a unique title prefix instead of a global count.
 * This is what proves the dynamic `<if>` search filter parses, that `lower()`
 * matching is case-insensitive, and that `useGeneratedKeys` returns the key.
 */
@SpringBootTest
class MyBatisBookRepositoryTest {

    companion object {
        private const val PREFIX = "it-book-"
        private val dbFile: Path = Path.of("build", "test-mybatis-books.db").toAbsolutePath()

        @DynamicPropertySource
        @JvmStatic
        fun datasource(registry: DynamicPropertyRegistry) {
            Files.deleteIfExists(dbFile)
            registry.add("spring.datasource.url") { "jdbc:sqlite:$dbFile" }
            registry.add("spring.datasource.driver-class-name") { "org.sqlite.JDBC" }
            registry.add("spring.datasource.hikari.maximum-pool-size") { "1" }
            registry.add("spring.flyway.enabled") { "true" }
            registry.add("spring.flyway.locations") { "classpath:db/migration" }
        }
    }

    @Autowired
    lateinit var repo: BookRepository

    private fun newBook(title: String, author: String = "Test Author", price: Int = 10) =
        Book(title = title, author = author, price = price)

    @Test
    fun `migration seeds the demo catalog`() {
        // The six rows from V4__create_books.sql, independent of what tests add.
        assertTrue(repo.count("The Pragmatic Programmer") >= 1)
        assertTrue(repo.count("Clean Code") >= 1)
    }

    @Test
    fun `insert returns the generated key and the row can be read back`() {
        val id = repo.save(newBook("${PREFIX}insert", "Ada Lovelace", 42))

        assertTrue(id > 0, "expected a generated key, got $id")
        val loaded = repo.findById(id)
        assertNotNull(loaded)
        assertEquals("${PREFIX}insert", loaded!!.title)
        assertEquals("Ada Lovelace", loaded.author)
        assertEquals(42, loaded.price)
    }

    @Test
    fun `findById returns null for an unknown id`() {
        assertNull(repo.findById(999_999))
    }

    @Test
    fun `search matches title or author case-insensitively`() {
        repo.save(newBook("${PREFIX}TitleMatch", "Someone Else"))
        repo.save(newBook("${PREFIX}Other", "${PREFIX}AuthorMatch Person"))

        // Upper-cased terms prove lower() folding on both sides of the comparison.
        val byTitle = repo.findAll("${PREFIX}TITLEMATCH")
        val byAuthor = repo.findAll("${PREFIX}AUTHORMATCH")

        assertTrue(byTitle.any { it.title == "${PREFIX}TitleMatch" })
        assertTrue(byAuthor.any { it.author == "${PREFIX}AuthorMatch Person" })
    }

    @Test
    fun `blank search returns every row`() {
        assertEquals(repo.count(null), repo.findAll(null).size.toLong())
        assertEquals(repo.count(""), repo.findAll("   ").size.toLong())
    }

    @Test
    fun `search treats LIKE wildcards as literals`() {
        repo.save(newBook("${PREFIX}percent"))

        // Unescaped, "it-book-%" would match every row these tests created.
        assertEquals(0, repo.count("$PREFIX%"))
        assertTrue(repo.count(PREFIX) > 0)
    }

    @Test
    fun `findPage pages through the filtered rows in id order`() {
        val prefix = "${PREFIX}page-"
        repeat(3) { repo.save(newBook("$prefix$it")) }

        val first = repo.findPage(prefix, offset = 0, limit = 2)
        val second = repo.findPage(prefix, offset = 2, limit = 2)

        assertEquals(3L, repo.count(prefix))
        assertEquals(2, first.size)
        assertEquals(1, second.size)
        assertTrue(first.last().id!! < second.first().id!!)
    }

    @Test
    fun `update rewrites the row and delete removes it`() {
        val id = repo.save(newBook("${PREFIX}before"))
        val stored = repo.findById(id)!!

        stored.title = "${PREFIX}after"
        stored.author = "New Author"
        stored.price = 99
        assertEquals(1, repo.update(stored))

        val updated = repo.findById(id)!!
        assertEquals("${PREFIX}after", updated.title)
        assertEquals("New Author", updated.author)
        assertEquals(99, updated.price)

        assertEquals(1, repo.deleteById(id))
        assertNull(repo.findById(id))
    }
}
