package fr.social.gouv.agora.infrastructure.responseQag.repository

import fr.social.gouv.agora.domain.ResponseQag
import fr.social.gouv.agora.infrastructure.responseQag.dto.ResponseQagDTO
import fr.social.gouv.agora.infrastructure.responseQag.repository.ResponseQagCacheRepository.CacheResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.cache.annotation.EnableCaching
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
@EnableCaching
@ImportAutoConfiguration(
    CacheAutoConfiguration::class,
    RedisAutoConfiguration::class,
)
internal class ResponseQagRepositoryImplTest {

    @Autowired
    private lateinit var repository: ResponseQagRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: ResponseQagDatabaseRepository

    @MockBean
    private lateinit var cacheRepository: ResponseQagCacheRepository

    @MockBean
    private lateinit var mapper: ResponseQagMapper

    @Test
    fun `getResponseQag - when qagId is invalid UUID - should return null`() {
        // When
        val result = repository.getResponseQag("qagId is invalid UUID")

        // Then
        assertThat(result).isEqualTo(null)
    }

    @Test
    fun `getResponseQag - when cacheResult is CachedResponseQagNotFound - should return null`() {
        // Given
        val qagId = UUID.randomUUID()
        given(cacheRepository.getResponseQag(qagId)).willReturn(CacheResult.CachedResponseQagNotFound)

        // When
        val result = repository.getResponseQag(qagId.toString())

        // Then
        assertThat(result).isEqualTo(null)
        then(cacheRepository).should(only()).getResponseQag(qagId)
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getResponseQag - when cacheResult is CachedResponseQag - should return mapped object from cache`() {
        // Given
        val qagId = UUID.randomUUID()
        val responseQagDTO = mock(ResponseQagDTO::class.java)
        given(cacheRepository.getResponseQag(qagId)).willReturn(CacheResult.CachedResponseQag(responseQagDTO))

        val responseQag = mock(ResponseQag::class.java)
        given(mapper.toDomain(responseQagDTO)).willReturn(responseQag)

        // When
        val result = repository.getResponseQag(qagId.toString())

        // Then
        assertThat(result).isEqualTo(responseQag)
        then(cacheRepository).should(only()).getResponseQag(qagId)
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).should(only()).toDomain(responseQagDTO)
    }

    @Test
    fun `getResponseQag - when cacheResult is CacheNotInitialized and get null from database - should return null and insert null in cache`() {
        // Given
        val qagId = UUID.randomUUID()
        given(cacheRepository.getResponseQag(qagId)).willReturn(CacheResult.CacheNotInitialized)
        given(databaseRepository.getResponseQag(qagId)).willReturn(null)

        // When
        val result = repository.getResponseQag(qagId.toString())

        // Then
        assertThat(result).isEqualTo(null)
        then(cacheRepository).should(times(1)).getResponseQag(qagId)
        then(cacheRepository).should(times(1)).insertResponseQag(qagId = qagId, responseQagDTO = null)
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(databaseRepository).should(only()).getResponseQag(qagId)
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getResponseQag - when cacheResult is CacheNotInitialized and get dto from database - should insert to it cache and return mapped object`() {
        // Given
        val qagId = UUID.randomUUID()
        val responseQagDTO = mock(ResponseQagDTO::class.java)
        given(cacheRepository.getResponseQag(qagId)).willReturn(CacheResult.CacheNotInitialized)
        given(databaseRepository.getResponseQag(qagId)).willReturn(responseQagDTO)

        val responseQag = mock(ResponseQag::class.java)
        given(mapper.toDomain(responseQagDTO)).willReturn(responseQag)

        // When
        val result = repository.getResponseQag(qagId.toString())

        // Then
        assertThat(result).isEqualTo(responseQag)
        then(cacheRepository).should(times(1)).getResponseQag(qagId)
        then(cacheRepository).should(times(1)).insertResponseQag(qagId, responseQagDTO)
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(databaseRepository).should(only()).getResponseQag(qagId)
        then(mapper).should(only()).toDomain(responseQagDTO)
    }

}