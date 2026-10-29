package com.example.springjte.model

/**
 * One row of the `users` table.
 *
 * The properties are `var` on purpose. MyBatis instantiates entities with a no-arg
 * constructor and fills them through setters, and `UserMapper.insert` writes the
 * generated key back into [id] (`useGeneratedKeys` + `keyProperty="id"`). A fully
 * immutable data class would need a `<constructor>` result map plus a mutable
 * holder for the key, so mutability keeps the mapper XML simple.
 */
data class User(
    var id: Long? = null,
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var role: String? = null
)
