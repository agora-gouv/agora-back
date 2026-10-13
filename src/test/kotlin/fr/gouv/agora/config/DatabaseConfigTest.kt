package fr.gouv.agora.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DatabaseConfigTest {

    private val databaseConfig = DatabaseConfig()

    @Test
    fun `hikariConfig - should size the pool above the previous default of 5`() {
        // When
        val hikariConfig = databaseConfig.hikariConfig()

        // Then
        assertThat(hikariConfig.maximumPoolSize).isGreaterThan(5)
    }

    @Test
    fun `hikariConfig - should keep minimumIdle within the pool size bounds`() {
        // When
        val hikariConfig = databaseConfig.hikariConfig()

        // Then
        assertThat(hikariConfig.minimumIdle).isGreaterThanOrEqualTo(0)
        assertThat(hikariConfig.minimumIdle).isLessThanOrEqualTo(hikariConfig.maximumPoolSize)
    }

    @Test
    fun `hikariConfig - should set a bounded connection timeout`() {
        // When
        val hikariConfig = databaseConfig.hikariConfig()

        // Then
        assertThat(hikariConfig.connectionTimeout).isGreaterThan(0)
        assertThat(hikariConfig.connectionTimeout).isLessThanOrEqualTo(30_000)
    }

    @Test
    fun `hikariConfig - should set a positive max lifetime to recycle stale connections`() {
        // When
        val hikariConfig = databaseConfig.hikariConfig()

        // Then
        assertThat(hikariConfig.maxLifetime).isGreaterThan(0)
    }
}
