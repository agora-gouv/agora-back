package fr.gouv.agora.usecase.cache

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.stereotype.Service

@Service
class ClearCacheUseCase(
    private val redisConnectionFactory: RedisConnectionFactory,
) {
    private val logger: Logger = LoggerFactory.getLogger(ClearCacheUseCase::class.java)

    fun clearAllCaches() {
        redisConnectionFactory.connection.use { connection ->
            connection.serverCommands().flushDb()
        }
        logger.info("Cache Redis entièrement vidé (flushDb)")
    }
}
