package com.example.springjte.repository

import com.example.springjte.model.User

/**
 * Persistence contract for the `users` table.
 *
 * Production uses the MyBatis implementation
 * ([com.example.springjte.repository.mybatis.MyBatisUserRepository] over
 * [com.example.springjte.repository.mybatis.UserMapper]); unit tests swap in an
 * in-memory double. Keeping this an interface is what lets the SQL layer be
 * replaced without touching the service, the security config or the controllers.
 */
interface UserRepository {

    /** All users, ordered by id; [search] matches name or email when not blank. */
    fun findAll(search: String? = null): List<User>

    /** One page of [findAll], starting at [offset]. */
    fun findPage(search: String?, offset: Int, limit: Int): List<User>

    /** Number of rows [findAll] would return for [search]. */
    fun count(search: String? = null): Long

    fun findById(id: Long): User?

    fun findByEmail(email: String): User?

    /** Inserts [user] and returns the generated primary key. */
    fun save(user: User): Long

    /** Updates every column except the key; returns the affected row count. */
    fun update(user: User): Int

    fun deleteById(id: Long): Int
}
