package fr.social.gouv.agora.infrastructure.responseQag.repository

import fr.social.gouv.agora.domain.ResponseQag
import fr.social.gouv.agora.infrastructure.responseQag.dto.ResponseQagDTO
import fr.social.gouv.agora.infrastructure.responseQag.repository.ResponseQagCacheRepository.CacheResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
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

    @Nested
    inner class GetAllResponseQagTestCases {

        @Test
        fun `getAllResponseQag - when cache is not initialized - should initialize cache with database then return mapped results`() {
            // Given
            given(cacheRepository.getAllResponseQagList()).willReturn(CacheResult.CacheNotInitialized)

            val responseQagDTO = mock(ResponseQagDTO::class.java)
            given(databaseRepository.getAllResponseQagList()).willReturn(listOf(responseQagDTO))

            val responseQag = mock(ResponseQag::class.java)
            given(mapper.toDomain(responseQagDTO)).willReturn(responseQag)

            // When
            val result = repository.getAllResponseQag()

            // Then
            assertThat(result).isEqualTo(listOf(responseQag))
            inOrder(cacheRepository, databaseRepository, mapper).also {
                then(cacheRepository).should(it).getAllResponseQagList()
                then(databaseRepository).should(it).getAllResponseQagList()
                then(cacheRepository).should(it).initializeCache(listOf(responseQagDTO))
                then(mapper).should(it).toDomain(responseQagDTO)
                it.verifyNoMoreInteractions()
            }
        }

        @Test
        fun `getAllResponseQag - when cache is initialized - should return mapped results`() {
            // Given
            val responseQagDTO = mock(ResponseQagDTO::class.java)
            given(cacheRepository.getAllResponseQagList())
                .willReturn(CacheResult.CachedResponseQagList(listOf(responseQagDTO)))

            val responseQag = mock(ResponseQag::class.java)
            given(mapper.toDomain(responseQagDTO)).willReturn(responseQag)

            // When
            val result = repository.getAllResponseQag()

            // Then
            assertThat(result).isEqualTo(listOf(responseQag))
            inOrder(cacheRepository, mapper).also {
                then(cacheRepository).should(it).getAllResponseQagList()
                then(mapper).should(it).toDomain(responseQagDTO)
                it.verifyNoMoreInteractions()
            }
            then(databaseRepository).shouldHaveNoInteractions()
        }

    }

    @Nested
    inner class GetResponseQagTestCases {

        @Test
        fun `getResponseQag - when qagId is invalid UUID - should return null`() {
            // When
            val result = repository.getResponseQag(qagId = "qagId is invalid UUID")

            // Then
            assertThat(result).isEqualTo(null)
        }

        @Test
        fun `getResponseQag - when cache is not initialized and has no result - should initialize cache with database then return null`() {
            // Given
            given(cacheRepository.getAllResponseQagList()).willReturn(CacheResult.CacheNotInitialized)
            given(databaseRepository.getAllResponseQagList()).willReturn(emptyList())

            // When
            val result = repository.getResponseQag(qagId = UUID.randomUUID().toString())

            // Then
            assertThat(result).isEqualTo(null)
            inOrder(cacheRepository, databaseRepository).also {
                then(cacheRepository).should(it).getAllResponseQagList()
                then(databaseRepository).should(it).getAllResponseQagList()
                then(cacheRepository).should(it).initializeCache(emptyList())
                it.verifyNoMoreInteractions()
            }
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getResponseQag - when cache is initialized and has results - should return mapped results`() {
            // Given
            val qagId = UUID.randomUUID()
            val responseQagDTO = mock(ResponseQagDTO::class.java).also {
                given(it.qagId).willReturn(qagId)
            }
            given(cacheRepository.getAllResponseQagList())
                .willReturn(CacheResult.CachedResponseQagList(listOf(responseQagDTO)))

            val responseQag = mock(ResponseQag::class.java)
            given(mapper.toDomain(responseQagDTO)).willReturn(responseQag)

            // When
            val result = repository.getResponseQag(qagId = qagId.toString())

            // Then
            assertThat(result).isEqualTo(responseQag)
            inOrder(cacheRepository, mapper).also {
                then(cacheRepository).should(it).getAllResponseQagList()
                then(mapper).should(it).toDomain(responseQagDTO)
                it.verifyNoMoreInteractions()
            }
            then(databaseRepository).shouldHaveNoInteractions()
        }

    }

}