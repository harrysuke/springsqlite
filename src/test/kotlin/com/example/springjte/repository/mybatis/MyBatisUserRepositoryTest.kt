package com.example.springjte.repository.mybatis

import com.example.springjte.model.User
import com.example.springjte.repository.UserRepository
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
 * Runs the real mapper XML against a real SQLite file.
 *
 * The `UserService` tests use an in-memory double, which cannot prove that the
 * statements in `mappers/UserMapper.xml` parse, that `useGeneratedKeys` writes
 * the key back, or that `ESCAPE '\'` behaves. This test covers exactly that.
 *
 * The migrations seed seven legacy users plus the default admin, so every
 * assertion is scoped to a unique email prefix instead of a global count.
 */
@SpringBootTest
class MyBatisUserRepositoryTest {

    companion object {
        private const val PREFIX = "mybatis-it-"
        private val dbFile: Path = Path.of("build", "test-mybatis-repo.db").toAbsolutePath()

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
    lateinit var repo: UserRepository

    private fun newUser(name: String, email: String = "$PREFIX${System.nanoTime()}@test.local") =
        User(name = name, email = email, password = "hash", role = "ROLE_USER")

    @Test
    fun `insert returns the generated key and the row can be read back`() {
        val id = repo.save(newUser("Alice"))

        assertTrue(id > 0, "expected a generated key, got $id")
        val loaded = repo.findById(id)
        assertNotNull(loaded)
        assertEquals("Alice", loaded!!.name)
        assertEquals("ROLE_USER", loaded.role)
    }

    @Test
    fun `insert falls back to ROLE_USER when the role is null`() {
        val user = User(name = "NoRole", email = "$PREFIX-nullrole@test.local", password = "hash", role = null)
        val id = repo.save(user)

        assertEquals("ROLE_USER", repo.findById(id)?.role)
    }

    @Test
    fun `findByEmail resolves the login identity`() {
        val email = "${PREFIX}login@test.local"
        repo.save(newUser("Login", email))

        assertEquals("Login", repo.findByEmail(email)?.name)
        assertNull(repo.findByEmail("$PREFIX-missing@test.local"))
    }

    @Test
    fun `search matches name or email and count agrees with findAll`() {
        repo.save(newUser("Searchable One", "${PREFIX}search1@test.local"))
        repo.save(newUser("Someone Else", "${PREFIX}search2@test.local"))

        val byName = repo.findAll("Searchable")
        val byEmail = repo.findAll("${PREFIX}search2")

        assertTrue(byName.any { it.name == "Searchable One" })
        assertTrue(byEmail.any { it.email == "${PREFIX}search2@test.local" })
        assertEquals(repo.findAll(PREFIX).size.toLong(), repo.count(PREFIX))
    }

    @Test
    fun `blank search returns every row`() {
        assertEquals(repo.count(null), repo.findAll(null).size.toLong())
        assertEquals(repo.count(""), repo.findAll("   ").size.toLong())
    }

    @Test
    fun `search treats LIKE wildcards as literals`() {
        repo.save(newUser("Percent Test", "${PREFIX}pct@test.local"))

        // Unescaped, "mybatis-it-%" would match every row this test created.
        assertEquals(0, repo.findAll("$PREFIX%").size)
        assertTrue(repo.findAll(PREFIX).isNotEmpty())
    }

    @Test
    fun `findPage pages through the filtered rows in id order`() {
        // Its own sub-prefix: the other tests in this class share the database.
        val prefix = "${PREFIX}page-"
        repeat(3) { repo.save(newUser("Paged $it", "$prefix$it@test.local")) }

        val first = repo.findPage(prefix, offset = 0, limit = 2)
        val second = repo.findPage(prefix, offset = 2, limit = 2)

        assertEquals(3L, repo.count(prefix))
        assertEquals(2, first.size)
        assertEquals(1, second.size)
        assertTrue(first.last().id!! < second.first().id!!)
    }

    @Test
    fun `update rewrites the row and delete removes it`() {
        val id = repo.save(newUser("Before"))
        val stored = repo.findById(id)!!

        stored.name = "After"
        stored.email = "$PREFIX-after@test.local"
        stored.role = "ROLE_ADMIN"
        assertEquals(1, repo.update(stored))

        val updated = repo.findById(id)!!
        assertEquals("After", updated.name)
        assertEquals("ROLE_ADMIN", updated.role)

        assertEquals(1, repo.deleteById(id))
        assertNull(repo.findById(id))
    }
}
