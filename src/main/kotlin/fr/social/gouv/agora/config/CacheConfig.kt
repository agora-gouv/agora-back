package fr.social.gouv.agora.config

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
                println("Invalid Redis URL: $redisUrl")
            }
        }
        return JedisConnectionFactory(config)
    }

    @Bean
    @Primary
    fun cacheManager(factory: RedisConnectionFactory): CacheManager {
        val config = RedisCacheConfiguration.defaultCacheConfig()
        val redisCacheConfiguration = config
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    GenericJackson2JsonRedisSerializer()
                )
            )
            .entryTtl(Duration.ofHours(1L))
        return RedisCacheManager.builder(factory).cacheDefaults(redisCacheConfiguration).build()
    }

    @Bean
    @Qualifier("shortTermCacheManager")
    fun shortTermCacheManager(factory: RedisConnectionFactory): CacheManager {
        val config = RedisCacheConfiguration.defaultCacheConfig()
        val redisCacheConfiguration = config
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    GenericJackson2JsonRedisSerializer()
                )
            )
            .entryTtl(Duration.ofMinutes(5))
        return RedisCacheManager.builder(factory).cacheDefaults(redisCacheConfiguration).build()
    }
}