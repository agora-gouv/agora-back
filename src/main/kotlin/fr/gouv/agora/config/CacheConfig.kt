package fr.gouv.agora.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
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
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.net.URI
import java.time.Duration

@Configuration
@EnableCaching
class CacheConfig {
    private val logger: Logger = LoggerFactory.getLogger(CacheConfig::class.java)

    companion object {
        const val DEFAULT_REDIS_USER = "default"

        // Le pool par défaut de Jedis (8 connexions, attente infinie) saturait face aux 200 threads Tomcat.
        // On bascule sur Lettuce avec un pool borné et un temps d'attente fini.
        private const val DEFAULT_POOL_MAX_TOTAL = 32
        private const val DEFAULT_POOL_MAX_IDLE = 16
        private const val DEFAULT_POOL_MIN_IDLE = 4
        private const val DEFAULT_POOL_MAX_WAIT_MS = 500L
        private const val DEFAULT_COMMAND_TIMEOUT_MS = 1_000L
        private const val DEFAULT_SHUTDOWN_TIMEOUT_MS = 100L
    }

    @Bean
    fun getConnectionFactory(): LettuceConnectionFactory {
        val standaloneConfig = RedisStandaloneConfiguration()
        System.getenv("REDIS_URL")?.let { redisUrl ->
            try {
                val redisURI = URI.create(redisUrl)
                val userInfo = redisURI.userInfo.split(":")
                standaloneConfig.username = userInfo[0].takeUnless { it.isEmpty() } ?: DEFAULT_REDIS_USER
                standaloneConfig.setPassword(userInfo[1])
                standaloneConfig.hostName = redisURI.host
                standaloneConfig.port = redisURI.port
            } catch (e: IllegalArgumentException) {
                logger.error("Invalid Redis URL: $redisUrl")
            }
        }

        return LettuceConnectionFactory(standaloneConfig, lettuceClientConfiguration())
    }

    internal fun lettuceClientConfiguration(): LettucePoolingClientConfiguration {
        return LettucePoolingClientConfiguration.builder()
            .poolConfig(lettucePoolConfig())
            .commandTimeout(Duration.ofMillis(envLong("REDIS_COMMAND_TIMEOUT_MS", DEFAULT_COMMAND_TIMEOUT_MS)))
            .shutdownTimeout(Duration.ofMillis(envLong("REDIS_SHUTDOWN_TIMEOUT_MS", DEFAULT_SHUTDOWN_TIMEOUT_MS)))
            .build()
    }

    internal fun lettucePoolConfig(): GenericObjectPoolConfig<Any> {
        return GenericObjectPoolConfig<Any>().apply {
            maxTotal = envInt("REDIS_POOL_MAX_TOTAL", DEFAULT_POOL_MAX_TOTAL)
            maxIdle = envInt("REDIS_POOL_MAX_IDLE", DEFAULT_POOL_MAX_IDLE)
            minIdle = envInt("REDIS_POOL_MIN_IDLE", DEFAULT_POOL_MIN_IDLE)
            setMaxWait(Duration.ofMillis(envLong("REDIS_POOL_MAX_WAIT_MS", DEFAULT_POOL_MAX_WAIT_MS)))
            testOnBorrow = true
            testWhileIdle = true
            setTimeBetweenEvictionRuns(Duration.ofSeconds(30))
            setMinEvictableIdleTime(Duration.ofMinutes(5))
        }
    }

    private fun envInt(name: String, default: Int): Int = System.getenv(name)?.toIntOrNull() ?: default

    private fun envLong(name: String, default: Long): Long = System.getenv(name)?.toLongOrNull() ?: default

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
