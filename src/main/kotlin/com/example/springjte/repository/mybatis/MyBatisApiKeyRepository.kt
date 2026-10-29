package com.example.springjte.repository.mybatis

import com.example.springjte.model.ApiKey
import com.example.springjte.repository.ApiKeyRepository
import org.springframework.stereotype.Repository

@Repository
class MyBatisApiKeyRepository(private val mapper: ApiKeyMapper) : ApiKeyRepository {

    override fun findAll(): List<ApiKey> = mapper.findAll()

    override fun findActive(): List<ApiKey> = mapper.findActive()

    override fun findById(id: Long): ApiKey? = mapper.findById(id)

    override fun findByKeyValue(keyValue: String): ApiKey? = mapper.findByKeyValue(keyValue)

    override fun save(apiKey: ApiKey): Long {
        mapper.insert(apiKey)
        return apiKey.id ?: throw IllegalStateException("No generated key returned for api_key insert")
    }

    override fun update(apiKey: ApiKey): Int = mapper.update(apiKey)

    override fun updateLastUsed(id: Long): Int = mapper.updateLastUsed(id)

    override fun deleteById(id: Long): Int = mapper.deleteById(id)

    override fun count(): Long = mapper.count()
}
