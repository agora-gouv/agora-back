package fr.gouv.agora.usecase.cache

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisKeyCommands
import org.springframework.data.redis.connection.RedisServerCommands
import org.springframework.data.redis.core.Cursor
import org.springframework.data.redis.core.ScanOptions
import java.util.Properties

@ExtendWith(MockitoExtension::class)
class GetRedisCacheStatsUseCaseTest {

    @Mock
    private lateinit var redisConnectionFactory: RedisConnectionFactory

    private lateinit var useCase: GetRedisCacheStatsUseCase

    @BeforeEach
    fun setUp() {
        useCase = GetRedisCacheStatsUseCase(redisConnectionFactory)
    }

    @Nested
    inner class `getStats` {

        @Test
        fun `getStats - should return total keys and memory info`() {
            // Given
            val connection = mock(RedisConnection::class.java)
            val serverCommands = mock(RedisServerCommands::class.java)
            val keyCommands = mock(RedisKeyCommands::class.java)
            given(redisConnectionFactory.connection).willReturn(connection)
            given(connection.serverCommands()).willReturn(serverCommands)
            given(connection.keyCommands()).willReturn(keyCommands)
            given(serverCommands.dbSize()).willReturn(123L)
            given(serverCommands.info("memory")).willReturn(
                Properties().apply {
                    setProperty("used_memory_human", "42.5M")
                    setProperty("used_memory", "44564480")
                    setProperty("maxmemory_human", "256M")
                    setProperty("maxmemory", "268435456")
                    setProperty("maxmemory_policy", "allkeys-lru")
                    setProperty("mem_fragmentation_ratio", "1.2")
                }
            )
            val cursor = cursorOf()
            given(keyCommands.scan(any(ScanOptions::class.java))).willReturn(cursor)

            // When
            val result = useCase.getStats()

            // Then
            assertThat(result.totalKeys).isEqualTo(123L)
            assertThat(result.usedMemoryHuman).isEqualTo("42.5M")
            assertThat(result.usedMemoryBytes).isEqualTo(44564480L)
            assertThat(result.maxMemoryHuman).isEqualTo("256M")
            assertThat(result.maxMemoryBytes).isEqualTo(268435456L)
            assertThat(result.maxMemoryPolicy).isEqualTo("allkeys-lru")
            assertThat(result.memoryFragmentationRatio).isEqualTo("1.2")
        }

        @Test
        fun `getStats - should group keys by cache prefix and sort by count descending`() {
            // Given
            val connection = mock(RedisConnection::class.java)
            val serverCommands = mock(RedisServerCommands::class.java)
            val keyCommands = mock(RedisKeyCommands::class.java)
            given(redisConnectionFactory.connection).willReturn(connection)
            given(connection.serverCommands()).willReturn(serverCommands)
            given(connection.keyCommands()).willReturn(keyCommands)
            given(serverCommands.dbSize()).willReturn(5L)
            given(serverCommands.info("memory")).willReturn(Properties())
            val cursor = cursorOf(
                "consultationCache::id-1",
                "consultationCache::id-2",
                "consultationCache::id-3",
                "userCache::user-1",
                "userCache::user-2",
            )
            given(keyCommands.scan(any(ScanOptions::class.java))).willReturn(cursor)

            // When
            val result = useCase.getStats()

            // Then
            assertThat(result.keysByCachePrefix).containsExactly(
                CachePrefixKeyCount(prefix = "consultationCache", keyCount = 3L),
                CachePrefixKeyCount(prefix = "userCache", keyCount = 2L),
            )
        }

        @Test
        fun `getStats - should close the redis connection after reading stats`() {
            // Given
            val connection = mock(RedisConnection::class.java)
            val serverCommands = mock(RedisServerCommands::class.java)
            val keyCommands = mock(RedisKeyCommands::class.java)
            given(redisConnectionFactory.connection).willReturn(connection)
            given(connection.serverCommands()).willReturn(serverCommands)
            given(connection.keyCommands()).willReturn(keyCommands)
            given(serverCommands.dbSize()).willReturn(0L)
            given(serverCommands.info("memory")).willReturn(Properties())
            val cursor = cursorOf()
            given(keyCommands.scan(any(ScanOptions::class.java))).willReturn(cursor)

            // When
            useCase.getStats()

            // Then
            then(connection).should().close()
        }

        @Test
        fun `getStats - should return empty prefix list when scan fails`() {
            // Given
            val connection = mock(RedisConnection::class.java)
            val serverCommands = mock(RedisServerCommands::class.java)
            val keyCommands = mock(RedisKeyCommands::class.java)
            given(redisConnectionFactory.connection).willReturn(connection)
            given(connection.serverCommands()).willReturn(serverCommands)
            given(connection.keyCommands()).willReturn(keyCommands)
            given(serverCommands.dbSize()).willReturn(42L)
            given(serverCommands.info("memory")).willReturn(Properties())
            given(keyCommands.scan(any(ScanOptions::class.java))).willThrow(RuntimeException("SCAN indisponible"))

            // When
            val result = useCase.getStats()

            // Then
            assertThat(result.totalKeys).isEqualTo(42L)
            assertThat(result.keysByCachePrefix).isEmpty()
        }

        @Test
        fun `getStats - should not fail when memory info is unavailable`() {
            // Given
            val connection = mock(RedisConnection::class.java)
            val serverCommands = mock(RedisServerCommands::class.java)
            val keyCommands = mock(RedisKeyCommands::class.java)
            given(redisConnectionFactory.connection).willReturn(connection)
            given(connection.serverCommands()).willReturn(serverCommands)
            given(connection.keyCommands()).willReturn(keyCommands)
            given(serverCommands.dbSize()).willReturn(7L)
            given(serverCommands.info("memory")).willThrow(RuntimeException("INFO indisponible"))
            val cursor = cursorOf()
            given(keyCommands.scan(any(ScanOptions::class.java))).willReturn(cursor)

            // When
            val result = useCase.getStats()

            // Then
            assertThat(result.totalKeys).isEqualTo(7L)
            assertThat(result.usedMemoryHuman).isNull()
            assertThat(result.maxMemoryPolicy).isNull()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun cursorOf(vararg keys: String): Cursor<ByteArray> {
        val cursor = mock(Cursor::class.java) as Cursor<ByteArray>
        val hasNextAnswers = keys.map { true } + false
        given(cursor.hasNext()).willReturn(hasNextAnswers.first(), *hasNextAnswers.drop(1).toTypedArray())
        if (keys.isNotEmpty()) {
            val nextAnswers = keys.map { it.toByteArray() }
            given(cursor.next()).willReturn(nextAnswers.first(), *nextAnswers.drop(1).toTypedArray())
        }
        return cursor
    }
}
