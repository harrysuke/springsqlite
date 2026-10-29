package com.example.springjte.repository.mybatis

import com.example.springjte.model.WebhookDelivery
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

@Mapper
interface WebhookDeliveryMapper {

    fun findByWebhookId(@Param("webhookId") webhookId: Long): List<WebhookDelivery>

    fun findRecent(@Param("limit") limit: Int): List<WebhookDelivery>

    fun insert(delivery: WebhookDelivery): Int

    fun update(delivery: WebhookDelivery): Int

    fun countFailed(): Long
}
