package com.example.springjte.repository.mybatis

import com.example.springjte.model.ApiKey
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface ApiKeyMapper {

    fun findAll(): List<ApiKey>

    fun findActive(): List<ApiKey>

    fun findById(@Param("id") id: Long): ApiKey?

    fun findByKeyValue(@Param("keyValue") keyValue: String): ApiKey?

    fun insert(apiKey: ApiKey): Int

    fun update(apiKey: ApiKey): Int

    fun updateLastUsed(@Param("id") id: Long): Int

    fun deleteById(@Param("id") id: Long): Int

    fun count(): Long
}
