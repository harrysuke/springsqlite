package com.example.springjte.repository

import com.example.springjte.model.User
import java.util.concurrent.atomic.AtomicLong

/**
 * Test double for [UserRepository] — no database, no MyBatis.
 *
 * It implements the interface (rather than subclassing a concrete repository) so
 * the unit tests exercise the service layer exactly as production wires it.
 */
class InMemoryUserRepository : UserRepository {

    private val store = LinkedHashMap<Long, User>()
    private val ids = AtomicLong(0)

    override fun findAll(search: String?): List<User> {
        val keyword = search?.trim()?.takeIf { it.isNotEmpty() }
        return store.values
            .filter { user -> keyword == null || user.name.contains(keyword, true) || user.email.contains(keyword, true) }
            .sortedBy { it.id }
    }

    override fun findPage(search: String?, offset: Int, limit: Int): List<User> =
        findAll(search).drop(offset).take(limit)

    override fun count(search: String?): Long = findAll(search).size.toLong()

    override fun findById(id: Long): User? = store[id]

    override fun findByEmail(email: String): User? = store.values.firstOrNull { it.email == email }

    override fun save(user: User): Long {
        val id = user.id ?: ids.incrementAndGet()
        store[id] = user.copy(id = id)
        return id
    }

    override fun update(user: User): Int {
        val id = user.id ?: return 0
        if (store.containsKey(id)) {
            store[id] = user.copy(id = id)
            return 1
        }
        return 0
    }

    override fun deleteById(id: Long): Int = if (store.remove(id) != null) 1 else 0

    fun clear() {
        store.clear()
        ids.set(0)
    }
}
