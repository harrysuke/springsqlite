package com.example.springjte.repository.mybatis

import com.example.springjte.model.Webhook
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface WebhookMapper {

    fun findAll(): List<Webhook>

    fun findActive(): List<Webhook>

    fun findByEventTypesContaining(@Param("eventType") eventType: String): List<Webhook>

    fun findById(@Param("id") id: Long): Webhook?

    fun insert(webhook: Webhook): Int

    fun update(webhook: Webhook): Int

    fun deleteById(@Param("id") id: Long): Int

    fun countActive(): Long
}
