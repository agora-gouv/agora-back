package fr.social.gouv.agora

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.server.ConfigurableWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.stereotype.Component
import javax.sql.DataSource


@SpringBootApplication
class AgoraBackApplication

fun main(args: Array<String>) {
    runApplication<AgoraBackApplication>(*args)
}

@Component
@Suppress("unused")
class ServerPortCustomizer : WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    override fun customize(factory: ConfigurableWebServerFactory) {
        System.getenv("PORT")?.toInt()?.let { port ->
            factory.setPort(port)
        }
    }
}

@Configuration
@Suppress("unused")
class JpaConfig {
    @Bean
    fun dataSource(): DataSource {
        val dataSourceBuilder = DataSourceBuilder.create()
        dataSourceBuilder.driverClassName("org.postgresql.Driver")
        dataSourceBuilder.url(System.getenv("DATABASE_URL"))
        dataSourceBuilder.username(System.getenv("DATABASE_USER"))
        dataSourceBuilder.password(System.getenv("DATABASE_PASSWORD"))
        return dataSourceBuilder.build()
    }
}

@Configuration
@Suppress("unused")
@EnableCaching
class RedisConfig {

    companion object {
        const val DEFAULT_REDIS_PORT = 6379
    }

    @Bean
    fun getConnectionFactory(): JedisConnectionFactory {
        val config = RedisStandaloneConfiguration()
        System.getenv("REDIS_URL")?.let { redisUrl ->
            config.hostName = redisUrl.substringAfter("redis://").substringBeforeLast(":")
            config.port = redisUrl.substringAfterLast(":").toIntOrNull() ?: DEFAULT_REDIS_PORT
        }
        return JedisConnectionFactory(config)
    }

    @Bean
    fun cacheManager(factory: RedisConnectionFactory): CacheManager {
        val config = RedisCacheConfiguration.defaultCacheConfig()
        val redisCacheConfiguration = config
            .serializeKeysWith(SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer()))
        return RedisCacheManager.builder(factory).cacheDefaults(redisCacheConfiguration).build()
    }

}