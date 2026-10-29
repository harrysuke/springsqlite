package com.example.springjte.repository.mybatis

import com.example.springjte.model.Book
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface BookMapper {

    fun findAll(@Param("pattern") pattern: String?): List<Book>

    fun findPage(
        @Param("pattern") pattern: String?,
        @Param("limit") limit: Int,
        @Param("offset") offset: Int
    ): List<Book>

    fun count(@Param("pattern") pattern: String?): Long

    fun findById(@Param("id") id: Long): Book?

    fun insert(book: Book): Int

    fun update(book: Book): Int

    fun deleteById(@Param("id") id: Long): Int
}
