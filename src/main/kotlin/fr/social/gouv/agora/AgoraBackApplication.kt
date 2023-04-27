package fr.social.gouv.agora

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.SpringBootApplication
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
import java.net.URI
import javax.sql.DataSource

@SpringBootApplication
class AgoraBackApplication

fun main(args: Array<String>) {
    FirebaseWrapper.initFirebase()
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

    companion object {
        private const val DEFAULT_DB_MAX_POOL_SIZE = 5
    }

    @Bean
    fun dataSource(hikariConfig: HikariConfig): DataSource {
        return HikariDataSource(hikariConfig)
    }

    @Bean
    fun hikariConfig(): HikariConfig {
        return HikariConfig().apply {
            System.getenv("DATABASE_URL")?.let { databaseUrl ->
                try {
                    val databaseURI = URI.create(databaseUrl)
                    jdbcUrl = "jdbc:${databaseUrl.replace("${databaseURI.userInfo}@", "")}"
                    val userInfo = databaseURI.userInfo.split(":")
                    username = userInfo[0]
                    password = userInfo[1]
                } catch (e: IllegalArgumentException) {
                    println("Invalid Database URL: $databaseUrl")
                }
                maximumPoolSize = System.getenv("DATABASE_MAX_POOL_SIZE").toIntOrNull() ?: DEFAULT_DB_MAX_POOL_SIZE
            }
        }
    }
}

@Configuration
@Suppress("unused")
@EnableCaching
class RedisConfig {

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
    fun cacheManager(factory: RedisConnectionFactory): CacheManager {
        val config = RedisCacheConfiguration.defaultCacheConfig()
        val redisCacheConfiguration = config
            .serializeKeysWith(SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer()))
        return RedisCacheManager.builder(factory).cacheDefaults(redisCacheConfiguration).build()
    }

}