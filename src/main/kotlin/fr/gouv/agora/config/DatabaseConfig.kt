package fr.gouv.agora.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.URI
import javax.sql.DataSource

@Configuration
class DatabaseConfig {
    private val logger: Logger = LoggerFactory.getLogger(DatabaseConfig::class.java)

    companion object {
        // L'ancien défaut (5 connexions) créait une file d'attente face aux threads Tomcat.
        // À garder < max_connections Postgres / nb d'instances applicatives.
        private const val DEFAULT_DB_MAX_POOL_SIZE = 10
        private const val DEFAULT_DB_MIN_IDLE = 2
        private const val DEFAULT_DB_CONNECTION_TIMEOUT_MS = 10_000L
        private const val DEFAULT_DB_MAX_LIFETIME_MS = 30 * 60 * 1_000L
        private const val DEFAULT_DB_IDLE_TIMEOUT_MS = 10 * 60 * 1_000L
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
                    jdbcUrl = formatJdbcUrl(databaseUrl = databaseUrl, userInfo = databaseURI.userInfo)
                    val userInfo = databaseURI.userInfo.split(":")
                    username = userInfo[0]
                    password = userInfo[1]
                } catch (e: IllegalArgumentException) {
                    logger.error("Invalid Database URL: $databaseUrl")
                }
            }

            // Dimensionnement du pool, appliqué indépendamment de DATABASE_URL.
            maximumPoolSize = envInt("DATABASE_MAX_POOL_SIZE", DEFAULT_DB_MAX_POOL_SIZE)
            minimumIdle = envInt("DATABASE_MIN_IDLE", DEFAULT_DB_MIN_IDLE).coerceIn(0, maximumPoolSize)
            connectionTimeout = envLong("DATABASE_CONNECTION_TIMEOUT_MS", DEFAULT_DB_CONNECTION_TIMEOUT_MS)
            maxLifetime = envLong("DATABASE_MAX_LIFETIME_MS", DEFAULT_DB_MAX_LIFETIME_MS)
            idleTimeout = envLong("DATABASE_IDLE_TIMEOUT_MS", DEFAULT_DB_IDLE_TIMEOUT_MS)
            poolName = "agora-db-pool"
        }
    }

    private fun envInt(name: String, default: Int): Int = System.getenv(name)?.toIntOrNull() ?: default

    private fun envLong(name: String, default: Long): Long = System.getenv(name)?.toLongOrNull() ?: default

    private fun formatJdbcUrl(databaseUrl: String, userInfo: String): String {
        val alteredDatabaseUrl = databaseUrl
            .replace("postgres://", "postgresql://")
            .replace("$userInfo@", "")
        return "jdbc:$alteredDatabaseUrl"
    }
}
