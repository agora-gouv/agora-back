package fr.gouv.agora.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.net.URI
import java.time.Duration

@Configuration
@Suppress("unused")
@EnableCaching
class CacheConfig {
    private val logger: Logger = LoggerFactory.getLogger(CacheConfig::class.java)

    companion object {
        const val DEFAULT_REDIS_USER = "default"
        const val DEFAULT_REDIS_PORT = 6379
    }

    @Bean
    fun getConnectionFactory(): JedisConnectionFactory {
        val config = RedisStandaloneConfiguration()
        System.getenv("REDIS_URL")?.let { redisUrl ->
            try {
                val redisURI = URI.create(redisUrl)
                val userInfo = redisURI.userInfo.split(":")
                config.username = userInfo[0].takeUnless { it.isEmpty() } ?: DEFAULT_REDIS_USER
                config.setPassword(userInfo[1])
                config.hostName = redisURI.host
                config.port = redisURI.port
            } catch (e: IllegalArgumentException) {
                logger.error("Invalid Redis URL: $redisUrl")
            }
        }

        return JedisConnectionFactory(config)
    }

    @Bean
    @Primary
    fun cacheManager(factory: RedisConnectionFactory, objectMapper: ObjectMapper): CacheManager {
        return RedisCacheManager
            .builder(factory)
            .cacheDefaults(
                getDefautConfig().entryTtl(Duration.ofHours(1L))
            ).build()
    }

    @Bean
    @Qualifier("shortTermCacheManager")
    fun shortTermCacheManager(factory: RedisConnectionFactory): CacheManager {
        return RedisCacheManager
            .builder(factory)
            .cacheDefaults(
                getDefautConfig().entryTtl(Duration.ofMinutes(5))
            ).build()
    }

    @Bean
    @Qualifier("longTermCacheManager")
    fun longTermCacheManager(factory: RedisConnectionFactory): CacheManager {
        return RedisCacheManager
            .builder(factory)
            .cacheDefaults(
                getDefautConfig().entryTtl(Duration.ofDays(1))
            ).build()
    }

    @Bean
    @Qualifier("eternalCacheManager")
    fun eternalCacheManager(factory: RedisConnectionFactory): CacheManager {
        return RedisCacheManager
            .builder(factory)
            .cacheDefaults(getDefautConfig())
            .build()
    }

    private fun getDefautConfig(): RedisCacheConfiguration {
        val jacksonObjectMapper = jacksonObjectMapper()
        val objectMapper = jacksonObjectMapper
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .activateDefaultTyping(jacksonObjectMapper.polymorphicTypeValidator, ObjectMapper.DefaultTyping.EVERYTHING)

        return RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    GenericJackson2JsonRedisSerializer(objectMapper)
                )
            )
    }
}
