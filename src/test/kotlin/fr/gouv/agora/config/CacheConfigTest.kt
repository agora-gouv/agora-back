package fr.gouv.agora.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration
import java.time.Duration

class CacheConfigTest {

    private val cacheConfig = CacheConfig()

    @Nested
    inner class GetConnectionFactory {

        @Test
        fun `getConnectionFactory - should build a Lettuce connection factory`() {
            // When
            val connectionFactory = cacheConfig.getConnectionFactory()

            // Then
            assertThat(connectionFactory).isInstanceOf(LettuceConnectionFactory::class.java)
        }
    }

    @Nested
    inner class LettucePoolConfig {

        @Test
        fun `lettucePoolConfig - should size the pool above the previous Jedis default of 8`() {
            // When
            val poolConfig = cacheConfig.lettucePoolConfig()

            // Then
            assertThat(poolConfig.maxTotal).isGreaterThan(8)
            assertThat(poolConfig.maxIdle).isGreaterThan(0)
            assertThat(poolConfig.minIdle).isGreaterThanOrEqualTo(0)
        }

        @Test
        fun `lettucePoolConfig - should bound the wait time instead of waiting indefinitely`() {
            // When
            val poolConfig = cacheConfig.lettucePoolConfig()

            // Then
            assertThat(poolConfig.maxWaitDuration.toMillis()).isGreaterThanOrEqualTo(0L)
        }

        @Test
        fun `lettucePoolConfig - should validate connections before borrowing`() {
            // When
            val poolConfig = cacheConfig.lettucePoolConfig()

            // Then
            assertThat(poolConfig.testOnBorrow).isTrue()
            assertThat(poolConfig.testWhileIdle).isTrue()
        }
    }

    @Nested
    inner class LettuceClientConfiguration {

        @Test
        fun `lettuceClientConfiguration - should enable pooling`() {
            // When
            val clientConfiguration = cacheConfig.lettuceClientConfiguration()

            // Then
            assertThat(clientConfiguration).isInstanceOf(LettucePoolingClientConfiguration::class.java)
            assertThat(clientConfiguration.poolConfig).isNotNull()
        }

        @Test
        fun `lettuceClientConfiguration - should define a bounded command timeout`() {
            // When
            val clientConfiguration = cacheConfig.lettuceClientConfiguration()

            // Then
            val commandTimeoutMillis = clientConfiguration.commandTimeout.toMillis()
            assertThat(commandTimeoutMillis).isEqualTo(Duration.ofMillis(1_000L).toMillis())
        }
    }
}
