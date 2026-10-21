package com.example.springjte.service

import com.example.springjte.dto.UserRequest
import com.example.springjte.dto.UserResponse
import com.example.springjte.model.Page
import com.example.springjte.model.User
import com.example.springjte.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
    private val repo: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun findAll(search: String? = null): List<UserResponse> =
        repo.findAll(search).map { toResponse(it) }

    fun findPage(search: String?, page: Int, size: Int): Page<UserResponse> {
        val safePage = page.coerceAtLeast(1)
        val safeSize = size.coerceIn(1, 100)
        val offset = (safePage - 1) * safeSize
        val items = repo.findPage(search, offset, safeSize).map { toResponse(it) }
        return Page(items = items, total = repo.count(search), page = safePage, size = safeSize)
    }

    fun findById(id: Long): UserResponse {
        val user = repo.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        return toResponse(user)
    }

    @Transactional
    fun create(request: UserRequest): UserResponse {
        val password = request.password?.takeIf { it.isNotBlank() }
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required")
        val email = request.email.trim().lowercase()
        if (repo.findByEmail(email) != null) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Email already registered")
        }
        val id = repo.save(
            User(
                name = request.name.trim(),
                email = email,
                password = passwordEncoder.encode(password)!!,
                role = normalizeRole(request.role)
            )
        )
        return toResponse(requireNotNull(repo.findById(id)))
    }

    @Transactional
    fun update(id: Long, request: UserRequest): UserResponse {
        val existing = repo.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        val email = request.email.trim().lowercase()
        repo.findByEmail(email)?.let { existingUser ->
            if (existingUser.id != id) {
                throw ResponseStatusException(HttpStatus.CONFLICT, "Email already registered")
            }
        }
        val password = if (request.password.isNullOrBlank()) existing.password
        else passwordEncoder.encode(request.password)!!
        repo.update(
            User(
                id = id,
                name = request.name.trim(),
                email = email,
                password = password,
                role = request.role?.let { normalizeRole(it) } ?: existing.role
            )
        )
        return toResponse(requireNotNull(repo.findById(id)))
    }

    @Transactional
    fun delete(id: Long) {
        if (repo.findById(id) == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        }
        repo.deleteById(id)
    }

    private fun normalizeRole(role: String?): String = when {
        role.isNullOrBlank() -> "ROLE_USER"
        role.equals("ADMIN", ignoreCase = true) || role.equals("ROLE_ADMIN", ignoreCase = true) -> "ROLE_ADMIN"
        else -> "ROLE_USER"
    }

    private fun toResponse(user: User): UserResponse = UserResponse(
        id = user.id ?: 0L,
        name = user.name,
        email = user.email,
        role = user.role
    )
}