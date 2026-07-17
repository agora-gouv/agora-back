package fr.gouv.agora.usecase.cache

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisServerCommands

@ExtendWith(MockitoExtension::class)
class ClearCacheUseCaseTest {

    @Mock
    private lateinit var redisConnectionFactory: RedisConnectionFactory

    private lateinit var useCase: ClearCacheUseCase

    @BeforeEach
    fun setUp() {
        useCase = ClearCacheUseCase(redisConnectionFactory)
    }

    @Nested
    inner class `clearAllCaches` {

        @Test
        fun `clearAllCaches - should call flushDb on redis connection`() {
            // Given
            val redisConnection = mock(RedisConnection::class.java)
            val serverCommands = mock(RedisServerCommands::class.java)
            org.mockito.BDDMockito.given(redisConnectionFactory.connection).willReturn(redisConnection)
            org.mockito.BDDMockito.given(redisConnection.serverCommands()).willReturn(serverCommands)

            // When
            useCase.clearAllCaches()

            // Then
            then(serverCommands).should().flushDb()
        }

        @Test
        fun `clearAllCaches - should close the redis connection after flushing`() {
            // Given
            val redisConnection = mock(RedisConnection::class.java)
            val serverCommands = mock(RedisServerCommands::class.java)
            org.mockito.BDDMockito.given(redisConnectionFactory.connection).willReturn(redisConnection)
            org.mockito.BDDMockito.given(redisConnection.serverCommands()).willReturn(serverCommands)

            // When
            useCase.clearAllCaches()

            // Then
            then(redisConnection).should().close()
        }
    }
}
