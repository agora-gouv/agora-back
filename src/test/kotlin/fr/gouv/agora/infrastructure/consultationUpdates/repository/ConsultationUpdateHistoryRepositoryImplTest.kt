package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.domain.ConsultationUpdateHistory
import fr.gouv.agora.domain.ConsultationUpdateHistoryStatus
import fr.gouv.agora.domain.ConsultationUpdateHistoryType
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import fr.gouv.agora.infrastructure.consultation.repository.ConsultationStrapiRepository
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import java.util.Date

@ExtendWith(MockitoExtension::class)
internal class ConsultationUpdateHistoryRepositoryImplTest {

    @Mock
    private lateinit var consultationStrapiRepository: ConsultationStrapiRepository

    @Mock
    private lateinit var featureFlagsRepository: FeatureFlagsRepository

    @Mock
    private lateinit var mapper: ConsultationUpdateHistoryMapper

    @Mock
    private lateinit var shortTermCacheManager: CacheManager

    @Mock
    private lateinit var cache: Cache

    @Mock
    private lateinit var cacheValueWrapper: Cache.ValueWrapper

    private lateinit var repository: ConsultationUpdateHistoryRepositoryImpl

    @BeforeEach
    fun setUp() {
        repository = ConsultationUpdateHistoryRepositoryImpl(
            consultationStrapiRepository = consultationStrapiRepository,
            featureFlagsRepository = featureFlagsRepository,
            mapper = mapper,
            shortTermCacheManager = shortTermCacheManager,
        )
    }

    private val consultationId = "consultation-id-123"

    private val historyEntry = ConsultationUpdateHistory(
        type = ConsultationUpdateHistoryType.UPDATE,
        consultationUpdateId = "update-id-456",
        status = ConsultationUpdateHistoryStatus.CURRENT,
        title = "Mise à jour",
        slug = "mise-a-jour",
        updateDate = Date(),
        actionText = null,
    )

    @Nested
    inner class `getConsultationUpdateHistory - when cache HIT` {

        @Test
        fun `should return cached value without calling Strapi`() {
            // Given
            given(shortTermCacheManager.getCache("consultationUpdateHistory")).willReturn(cache)
            given(cache.get(consultationId)).willReturn(cacheValueWrapper)
            given(cacheValueWrapper.get()).willReturn(listOf(historyEntry))

            // When
            val result = repository.getConsultationUpdateHistory(consultationId)

            // Then
            assertThat(result).containsExactly(historyEntry)
            then(consultationStrapiRepository).shouldHaveNoInteractions()
        }
    }

    @Nested
    inner class `getConsultationUpdateHistory - when cache MISS` {

        @Test
        fun `should call Strapi, cache the result, and return it`() {
            // Given
            val strapiDTO = mock(ConsultationStrapiDTO::class.java)
            given(shortTermCacheManager.getCache("consultationUpdateHistory")).willReturn(cache)
            given(cache.get(consultationId)).willReturn(null)
            given(consultationStrapiRepository.getConsultationByIdWithUnpublished(consultationId)).willReturn(strapiDTO)
            given(mapper.toDomain(strapiDTO)).willReturn(listOf(historyEntry))

            // When
            val result = repository.getConsultationUpdateHistory(consultationId)

            // Then
            assertThat(result).containsExactly(historyEntry)
            then(cache).should().put(consultationId, listOf(historyEntry))
        }

        @Test
        fun `when Strapi returns null - should return empty list and not write to cache`() {
            // Given
            given(shortTermCacheManager.getCache("consultationUpdateHistory")).willReturn(cache)
            given(cache.get(consultationId)).willReturn(null)
            given(consultationStrapiRepository.getConsultationByIdWithUnpublished(consultationId)).willReturn(null)

            // When
            val result = repository.getConsultationUpdateHistory(consultationId)

            // Then
            assertThat(result).isEmpty()
            then(cache).shouldHaveNoMoreInteractions()
            then(mapper).shouldHaveNoInteractions()
        }
    }

    @Nested
    inner class `getConsultationUpdateHistory - when cache unavailable` {

        @Test
        fun `when cache manager returns null - should call Strapi and return result`() {
            // Given
            val strapiDTO = mock(ConsultationStrapiDTO::class.java)
            given(shortTermCacheManager.getCache("consultationUpdateHistory")).willReturn(null)
            given(consultationStrapiRepository.getConsultationByIdWithUnpublished(consultationId)).willReturn(strapiDTO)
            given(mapper.toDomain(strapiDTO)).willReturn(listOf(historyEntry))

            // When
            val result = repository.getConsultationUpdateHistory(consultationId)

            // Then
            assertThat(result).containsExactly(historyEntry)
        }
    }
}
