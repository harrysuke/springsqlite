package com.example.springjte.repository

import com.example.springjte.model.User
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement

@Repository
class UserRepository(private val jdbcTemplate: JdbcTemplate) {

    private val columns = "id, name, email, password, role"

    private val rowMapper = RowMapper<User> { rs, _ ->
        User(
            id = rs.getLong("id"),
            name = rs.getString("name"),
            email = rs.getString("email"),
            password = rs.getString("password"),
            role = rs.getString("role")
        )
    }

    fun findAll(search: String? = null): List<User> {
        val keyword = search?.trim()?.takeIf { it.isNotEmpty() }
        val sql = if (keyword == null) {
            "SELECT $columns FROM users ORDER BY id"
        } else {
            "SELECT $columns FROM users WHERE name LIKE ? ESCAPE '\\' OR email LIKE ? ESCAPE '\\' ORDER BY id"
        }
        return if (keyword == null) jdbcTemplate.query(sql, rowMapper)
        else jdbcTemplate.query(sql, rowMapper, like(keyword), like(keyword))
    }

    fun findPage(search: String?, offset: Int, limit: Int): List<User> {
        val keyword = search?.trim()?.takeIf { it.isNotEmpty() }
        val sql = if (keyword == null) {
            "SELECT $columns FROM users ORDER BY id LIMIT ? OFFSET ?"
        } else {
            "SELECT $columns FROM users WHERE name LIKE ? ESCAPE '\\' OR email LIKE ? ESCAPE '\\' ORDER BY id LIMIT ? OFFSET ?"
        }
        return if (keyword == null) jdbcTemplate.query(sql, rowMapper, limit, offset)
        else jdbcTemplate.query(sql, rowMapper, like(keyword), like(keyword), limit, offset)
    }

    fun count(search: String? = null): Long {
        val keyword = search?.trim()?.takeIf { it.isNotEmpty() }
        val sql = if (keyword == null) "SELECT COUNT(*) FROM users"
        else "SELECT COUNT(*) FROM users WHERE name LIKE ? ESCAPE '\\' OR email LIKE ? ESCAPE '\\'"
        return if (keyword == null) jdbcTemplate.queryForObject(sql, Long::class.java) ?: 0L
        else jdbcTemplate.queryForObject(sql, Long::class.java, like(keyword), like(keyword)) ?: 0L
    }

    fun findById(id: Long): User? =
        jdbcTemplate.query("SELECT $columns FROM users WHERE id = ?", rowMapper, id).firstOrNull()

    fun findByEmail(email: String): User? =
        jdbcTemplate.query("SELECT $columns FROM users WHERE email = ?", rowMapper, email).firstOrNull()

    fun save(user: User): Long {
        val keyHolder = GeneratedKeyHolder()
        jdbcTemplate.update(
            { connection ->
                connection.prepareStatement(
                    "INSERT INTO users (name, email, password, role, email_verified) VALUES (?, ?, ?, ?, 0)",
                    Statement.RETURN_GENERATED_KEYS
                ).apply {
                    setString(1, user.name)
                    setString(2, user.email)
                    setString(3, user.password)
                    setString(4, user.role ?: "ROLE_USER")
                }
            },
            keyHolder
        )
        return keyHolder.key?.toLong() ?: throw IllegalStateException("No generated key returned for user insert")
    }

    fun update(user: User): Int =
        jdbcTemplate.update(
            "UPDATE users SET name = ?, email = ?, password = ?, role = ? WHERE id = ?",
            user.name, user.email, user.password, user.role ?: "ROLE_USER", user.id
        )

    fun deleteById(id: Long): Int = jdbcTemplate.update("DELETE FROM users WHERE id = ?", id)

    private fun like(keyword: String): String =
        "%" + keyword.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_") + "%"
}