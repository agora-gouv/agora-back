package fr.gouv.agora.infrastructure.consultation.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager

@ExtendWith(MockitoExtension::class)
internal class ConsultationStrapiCacheByIdRepositoryImplTest {

    @Mock
    private lateinit var shortTermCacheManager: CacheManager

    @Mock
    private lateinit var objectMapper: ObjectMapper

    @Mock
    private lateinit var cache: Cache

    @Mock
    private lateinit var cacheValueWrapper: Cache.ValueWrapper

    private lateinit var repository: ConsultationStrapiCacheByIdRepositoryImpl

    private val consultationId = "raknhpgc9zzuplz3lu9y0huf"

    @BeforeEach
    fun setUp() {
        repository = ConsultationStrapiCacheByIdRepositoryImpl(
            shortTermCacheManager = shortTermCacheManager,
            objectMapper = objectMapper,
        )
    }

    @Nested
    inner class `getConsultationById` {

        @Test
        fun `getConsultationById - when cache miss - should return null`() {
            // Given
            given(shortTermCacheManager.getCache(ConsultationStrapiCacheByIdRepositoryImpl.CONSULTATION_BY_ID_CACHE_NAME))
                .willReturn(cache)
            given(cache.get(consultationId, String::class.java)).willReturn(null)

            // When
            val result = repository.getConsultationById(consultationId)

            // Then
            assertThat(result).isNull()
        }

        @Test
        fun `getConsultationById - when cache hit with null value - should return null`() {
            // Given
            given(shortTermCacheManager.getCache(ConsultationStrapiCacheByIdRepositoryImpl.CONSULTATION_BY_ID_CACHE_NAME))
                .willReturn(cache)
            given(cache.get(consultationId, String::class.java)).willReturn("null")

            // When
            val result = repository.getConsultationById(consultationId)

            // Then
            assertThat(result).isNull()
            then(objectMapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getConsultationById - when cache hit with dto - should return deserialized dto`() {
            // Given
            val serialized = """{"documentId":"$consultationId","slug":"lycee"}"""
            val dto = org.mockito.Mockito.mock(ConsultationStrapiDTO::class.java)
            given(shortTermCacheManager.getCache(ConsultationStrapiCacheByIdRepositoryImpl.CONSULTATION_BY_ID_CACHE_NAME))
                .willReturn(cache)
            given(cache.get(consultationId, String::class.java)).willReturn(serialized)
            given(objectMapper.readValue(serialized, ConsultationStrapiDTO::class.java)).willReturn(dto)

            // When
            val result = repository.getConsultationById(consultationId)

            // Then
            assertThat(result).isEqualTo(dto)
        }

        @Test
        fun `getConsultationById - when deserialization fails - should return null`() {
            // Given
            val serialized = """{"broken json"""
            given(shortTermCacheManager.getCache(ConsultationStrapiCacheByIdRepositoryImpl.CONSULTATION_BY_ID_CACHE_NAME))
                .willReturn(cache)
            given(cache.get(consultationId, String::class.java)).willReturn(serialized)
            given(objectMapper.readValue(serialized, ConsultationStrapiDTO::class.java))
                .willThrow(RuntimeException("JSON parse error"))

            // When
            val result = repository.getConsultationById(consultationId)

            // Then
            assertThat(result).isNull()
        }
    }

    @Nested
    inner class `putConsultationById` {

        @Test
        fun `putConsultationById - when dto is not null - should serialize and put in cache`() {
            // Given
            val dto = org.mockito.Mockito.mock(ConsultationStrapiDTO::class.java)
            val serialized = """{"documentId":"$consultationId"}"""
            given(shortTermCacheManager.getCache(ConsultationStrapiCacheByIdRepositoryImpl.CONSULTATION_BY_ID_CACHE_NAME))
                .willReturn(cache)
            given(objectMapper.writeValueAsString(dto)).willReturn(serialized)

            // When
            repository.putConsultationById(consultationId, dto)

            // Then
            then(cache).should().put(consultationId, serialized)
        }

        @Test
        fun `putConsultationById - when dto is null - should put null string in cache`() {
            // Given
            given(shortTermCacheManager.getCache(ConsultationStrapiCacheByIdRepositoryImpl.CONSULTATION_BY_ID_CACHE_NAME))
                .willReturn(cache)

            // When
            repository.putConsultationById(consultationId, null)

            // Then
            then(cache).should().put(consultationId, "null")
            then(objectMapper).shouldHaveNoInteractions()
        }
    }

    @Nested
    inner class `getConsultationByIdWithUnpublished` {

        @Test
        fun `getConsultationByIdWithUnpublished - when cache miss - should return null`() {
            // Given
            given(shortTermCacheManager.getCache(ConsultationStrapiCacheByIdRepositoryImpl.CONSULTATION_BY_ID_WITH_UNPUBLISHED_CACHE_NAME))
                .willReturn(cache)
            given(cache.get(consultationId, String::class.java)).willReturn(null)

            // When
            val result = repository.getConsultationByIdWithUnpublished(consultationId)

            // Then
            assertThat(result).isNull()
        }

        @Test
        fun `getConsultationByIdWithUnpublished - when cache hit with dto - should return deserialized dto`() {
            // Given
            val serialized = """{"documentId":"$consultationId","slug":"lycee"}"""
            val dto = org.mockito.Mockito.mock(ConsultationStrapiDTO::class.java)
            given(shortTermCacheManager.getCache(ConsultationStrapiCacheByIdRepositoryImpl.CONSULTATION_BY_ID_WITH_UNPUBLISHED_CACHE_NAME))
                .willReturn(cache)
            given(cache.get(consultationId, String::class.java)).willReturn(serialized)
            given(objectMapper.readValue(serialized, ConsultationStrapiDTO::class.java)).willReturn(dto)

            // When
            val result = repository.getConsultationByIdWithUnpublished(consultationId)

            // Then
            assertThat(result).isEqualTo(dto)
        }
    }

    @Nested
    inner class `putConsultationByIdWithUnpublished` {

        @Test
        fun `putConsultationByIdWithUnpublished - when dto is not null - should serialize and put in cache`() {
            // Given
            val dto = org.mockito.Mockito.mock(ConsultationStrapiDTO::class.java)
            val serialized = """{"documentId":"$consultationId"}"""
            given(shortTermCacheManager.getCache(ConsultationStrapiCacheByIdRepositoryImpl.CONSULTATION_BY_ID_WITH_UNPUBLISHED_CACHE_NAME))
                .willReturn(cache)
            given(objectMapper.writeValueAsString(dto)).willReturn(serialized)

            // When
            repository.putConsultationByIdWithUnpublished(consultationId, dto)

            // Then
            then(cache).should().put(consultationId, serialized)
        }

        @Test
        fun `putConsultationByIdWithUnpublished - when dto is null - should put null string in cache`() {
            // Given
            given(shortTermCacheManager.getCache(ConsultationStrapiCacheByIdRepositoryImpl.CONSULTATION_BY_ID_WITH_UNPUBLISHED_CACHE_NAME))
                .willReturn(cache)

            // When
            repository.putConsultationByIdWithUnpublished(consultationId, null)

            // Then
            then(cache).should().put(consultationId, "null")
            then(objectMapper).shouldHaveNoInteractions()
        }
    }

    @Nested
    inner class `evictConsultationById` {

        @Test
        fun `evictConsultationById - should evict from both caches`() {
            // Given
            val cacheById = org.mockito.Mockito.mock(Cache::class.java)
            val cacheByIdWithUnpublished = org.mockito.Mockito.mock(Cache::class.java)
            given(shortTermCacheManager.getCache(ConsultationStrapiCacheByIdRepositoryImpl.CONSULTATION_BY_ID_CACHE_NAME))
                .willReturn(cacheById)
            given(shortTermCacheManager.getCache(ConsultationStrapiCacheByIdRepositoryImpl.CONSULTATION_BY_ID_WITH_UNPUBLISHED_CACHE_NAME))
                .willReturn(cacheByIdWithUnpublished)

            // When
            repository.evictConsultationById(consultationId)

            // Then
            then(cacheById).should().evict(consultationId)
            then(cacheByIdWithUnpublished).should().evict(consultationId)
        }
    }
}
