package com.example.springjte.repository.mybatis

import com.example.springjte.model.User
import com.example.springjte.repository.UserRepository
import org.springframework.stereotype.Repository

/**
 * MyBatis-backed [UserRepository].
 *
 * All SQL lives in `mappers/UserMapper.xml`; this class only adapts the mapper to
 * the repository contract — turning a raw search term into a LIKE pattern and
 * surfacing the generated key from `User.id` after an insert.
 */
@Repository
class MyBatisUserRepository(private val mapper: UserMapper) : UserRepository {

    override fun findAll(search: String?): List<User> = mapper.findAll(pattern(search))

    override fun findPage(search: String?, offset: Int, limit: Int): List<User> =
        mapper.findPage(pattern(search), limit, offset)

    override fun count(search: String?): Long = mapper.count(pattern(search))

    override fun findById(id: Long): User? = mapper.findById(id)

    override fun findByEmail(email: String): User? = mapper.findByEmail(email)

    override fun save(user: User): Long {
        mapper.insert(user)
        return user.id
            ?: throw IllegalStateException("No generated key returned for user insert")
    }

    override fun update(user: User): Int = mapper.update(user)

    override fun deleteById(id: Long): Int = mapper.deleteById(id)

    /**
     * `%keyword%`, or null when there is nothing to search for.
     *
     * `\`, `%` and `_` are escaped so the SQL can use `ESCAPE '\'` and treat them
     * as literals — otherwise searching for `_` or `%` would match every row.
     */
    private fun pattern(search: String?): String? {
        val keyword = search?.trim()?.takeIf { it.isNotEmpty() } ?: return null
        val escaped = keyword
            .replace("\\", "\\\\")
            .replace("%", "\\%")
            .replace("_", "\\_")
        return "%$escaped%"
    }
}
