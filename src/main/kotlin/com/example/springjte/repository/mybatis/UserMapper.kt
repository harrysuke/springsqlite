package com.example.springjte.repository.mybatis

import com.example.springjte.model.User
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

/**
 * MyBatis mapper for the `users` table.
 *
 * There is no implementation class: MyBatis generates one at startup from
 * `src/main/resources/mappers/UserMapper.xml`, whose `namespace` is this
 * interface's fully-qualified name and whose statement ids match these method
 * names. The interface is picked up by `@MapperScan` in
 * [com.example.springjte.config.MyBatisConfig].
 *
 * `@Param` names are mandatory here: Kotlin does not retain parameter names in
 * the class file by default, and the XML refers to them (`#{pattern}`,
 * `#{limit}`, …).
 */
@Mapper
interface UserMapper {

    /** [pattern] is an already LIKE-escaped `%keyword%` pattern, or null for "no filter". */
    fun findAll(@Param("pattern") pattern: String?): List<User>

    fun findPage(
        @Param("pattern") pattern: String?,
        @Param("limit") limit: Int,
        @Param("offset") offset: Int
    ): List<User>

    fun count(@Param("pattern") pattern: String?): Long

    fun findById(@Param("id") id: Long): User?

    fun findByEmail(@Param("email") email: String): User?

    /** Writes the generated key back into `user.id`. */
    fun insert(user: User): Int

    fun update(user: User): Int

    fun deleteById(@Param("id") id: Long): Int
}
