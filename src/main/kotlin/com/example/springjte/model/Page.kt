package com.example.springjte.model

data class Page<T>(
    val items: List<T>,
    val total: Long,
    val page: Int,
    val size: Int
) {
    val totalPages: Int
        get() = if (size == 0) 0 else ((total + size - 1) / size).toInt()

    val hasPrevious: Boolean
        get() = page > 1

    val hasNext: Boolean
        get() = page < totalPages
}