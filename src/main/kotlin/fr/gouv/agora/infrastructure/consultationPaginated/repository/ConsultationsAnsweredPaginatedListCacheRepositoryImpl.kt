package fr.gouv.agora.infrastructure.consultationPaginated.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.usecase.consultationPaginated.ConsultationAnsweredPaginatedList
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationAnsweredPaginatedListCacheRepository
import org.springframework.cache.CacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.ScanOptions
import org.springframework.stereotype.Component

@Component
class ConsultationsAnsweredPaginatedListCacheRepositoryImpl(
    private val cacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
    private val redisConnectionFactory: RedisConnectionFactory,
) : ConsultationAnsweredPaginatedListCacheRepository {

    companion object {
        private const val CACHE_NAME = "consultationsAnsweredPaginated"
        private const val KEY_SEPARATOR = "/"
        private const val SCAN_COUNT = 500L
    }

    override fun getConsultationAnsweredPage(userId: String, pageNumber: Int): ConsultationAnsweredPaginatedList? {
        return try {
            getCache()?.get(toKey(userId, pageNumber), String::class.java)?.let { cacheContent ->
                objectMapper.readValue(cacheContent, ConsultationAnsweredPaginatedList::class.java)
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun initConsultationAnsweredPage(
        userId: String,
        pageNumber: Int,
        content: ConsultationAnsweredPaginatedList?,
    ) {
        getCache()?.put(toKey(userId, pageNumber), objectMapper.writeValueAsString(content))
    }

    override fun clearCache(userId: String) {
        val keyPattern = "$CACHE_NAME::$userId$KEY_SEPARATOR*"
        redisConnectionFactory.connection.use { connection ->
            val scanOptions = ScanOptions.scanOptions().match(keyPattern).count(SCAN_COUNT).build()
            val keysToDelete = mutableListOf<ByteArray>()
            connection.keyCommands().scan(scanOptions).use { cursor ->
                while (cursor.hasNext()) {
                    keysToDelete.add(cursor.next())
                }
            }
            if (keysToDelete.isNotEmpty()) {
                connection.keyCommands().del(*keysToDelete.toTypedArray())
            }
        }
    }

    private fun toKey(userId: String, pageNumber: Int) = "$userId$KEY_SEPARATOR$pageNumber"

    private fun getCache() = cacheManager.getCache(CACHE_NAME)
}
