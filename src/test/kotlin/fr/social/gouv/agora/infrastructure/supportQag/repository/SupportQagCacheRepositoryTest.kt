package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.infrastructure.supportQag.dto.SupportQagDTO
import fr.social.gouv.agora.infrastructure.supportQag.repository.SupportQagCacheRepository.CacheResult
import fr.social.gouv.agora.infrastructure.supportQag.repository.SupportQagCacheRepository.Companion.SUPPORT_QAG_CACHE_NAME
import fr.social.gouv.agora.infrastructure.supportQag.repository.SupportQagCacheRepository.Companion.SUPPORT_QAG_COUNT_CACHE_NAME
import fr.social.gouv.agora.infrastructure.supportQag.repository.SupportQagCacheRepository.Companion.SUPPORT_QAG_NOT_FOUND_ID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
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
internal class SupportQagCacheRepositoryTest {

    @Autowired
    private lateinit var repository: SupportQagCacheRepository

    @Autowired
    private lateinit var cacheManager: CacheManager

    @BeforeEach
    fun setUp() {
        cacheManager.getCache(SUPPORT_QAG_COUNT_CACHE_NAME)?.clear()
        cacheManager.getCache(SUPPORT_QAG_CACHE_NAME)?.clear()
    }

    private val qagId = UUID.randomUUID()
    private val userId = UUID.randomUUID()

    private val supportQagDTO = SupportQagDTO(
        id = UUID.randomUUID(),
        userId = UUID.randomUUID(),
        qagId = UUID.randomUUID(),
    )

    private val notFoundSupportQagDto = SupportQagDTO(
        id = UUID.fromString(SUPPORT_QAG_NOT_FOUND_ID),
        userId = UUID.fromString(SUPPORT_QAG_NOT_FOUND_ID),
        qagId = UUID.fromString(SUPPORT_QAG_NOT_FOUND_ID),
    )

    private fun getCache() = cacheManager.getCache(SUPPORT_QAG_CACHE_NAME)
    private fun getCountCache() = cacheManager.getCache(SUPPORT_QAG_COUNT_CACHE_NAME)
    private fun buildSupportQagCacheKey() = "$qagId/$userId"

    @Nested
    inner class SupportCountTestCases {

        @Test
        fun `getSupportCount - when not initialized - should return null`() {
            // When
            val result = repository.getSupportCount(qagId)

            // Then
            assertThat(result).isEqualTo(null)
        }

        @Test
        fun `getSupportCount - when initialized with int as String - should return String to int`() {
            // Given
            getCountCache()?.put(qagId.toString(), "42")

            // When
            val result = repository.getSupportCount(qagId)

            // Then
            assertThat(result).isEqualTo(42)
        }

        @Test
        fun `getSupportCount - when initialized with String not valid - should return null`() {
            // Given
            getCountCache()?.put(qagId.toString(), "Not a number")

            // When
            val result = repository.getSupportCount(qagId)

            // Then
            assertThat(result).isEqualTo(null)
        }

        @Test
        fun `getSupportCount - when initialized with another type - should return null`() {
            // Given
            getCountCache()?.put(qagId.toString(), emptyList<String>())

            // When
            val result = repository.getSupportCount(qagId)

            // Then
            assertThat(result).isEqualTo(null)
        }

        @Test
        fun `insertSupportCount - should put argument to expected key`() {
            // When
            repository.insertSupportCount(qagId = qagId, supportCount = 51)

            // Then
            assertThat(getCountCache()?.get(qagId.toString())?.get()).isEqualTo("51")
        }

    }

    @Nested
    inner class GetSupportQagTestCases {

        @Test
        fun `getSupportQag - when not initialized - should return CacheNotInitialized`() {
            // When
            val result = repository.getSupportQag(qagId = qagId, userId = userId)

            // Then
            assertThat(result).isEqualTo(CacheResult.CacheNotInitialized)
        }

        @Test
        fun `getSupportQag - when initialized with correct type and ID SUPPORT_QAG_NOT_FOUND_ID - should return CachedSupportQagNotFound`() {
            // Given
            getCache()?.put(buildSupportQagCacheKey(), notFoundSupportQagDto)

            // When
            val result = repository.getSupportQag(qagId = qagId, userId = userId)

            // Then
            assertThat(result).isEqualTo(CacheResult.CachedSupportQagNotFound)
        }

        @Test
        fun `getSupportQag - when initialized with correct type and any other ID - should return CachedSupportQag`() {
            // Given
            getCache()?.put(buildSupportQagCacheKey(), supportQagDTO)

            // When
            val result = repository.getSupportQag(qagId = qagId, userId = userId)

            // Then
            assertThat(result).isEqualTo(CacheResult.CachedSupportQag(supportQagDTO))
        }

        @Test
        fun `getSupportQag - when initialized with invalid type - should return CacheNotInitialized`() {
            // Given
            getCache()?.put(buildSupportQagCacheKey(), "Invalid type")

            // When
            val result = repository.getSupportQag(qagId = qagId, userId = userId)

            // Then
            assertThat(result).isEqualTo(CacheResult.CacheNotInitialized)
        }

    }

    @Nested
    inner class InsertQagTestCases {

        @Test
        fun `insertSupportQag - when null - should insert expected DTO and reset count cache`() {
            // Given
            getCountCache()?.put(qagId.toString(), "42")

            // When
            repository.insertSupportQag(qagId = qagId, userId = userId, supportQagDTO = null)

            // Then
            assertThat(getCache()?.get(buildSupportQagCacheKey())?.get()).isEqualTo(notFoundSupportQagDto)
            assertThat(getCountCache()?.get(qagId.toString())?.get()).isNull()
        }

        @Test
        fun `insertSupportQag - when not null - should insert DTO and reset count cache`() {
            // When
            repository.insertSupportQag(qagId = qagId, userId = userId, supportQagDTO = supportQagDTO)

            // Then
            assertThat(getCache()?.get(buildSupportQagCacheKey())?.get()).isEqualTo(supportQagDTO)
            assertThat(getCountCache()?.get(qagId.toString())?.get()).isNull()
        }

    }

}