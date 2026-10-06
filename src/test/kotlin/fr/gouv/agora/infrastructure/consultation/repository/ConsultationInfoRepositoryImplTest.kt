package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationStrapiCacheRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.never
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import java.time.Clock
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
internal class ConsultationInfoRepositoryImplTest {

    @InjectMocks
    private lateinit var repository: ConsultationInfoRepositoryImpl

    @Mock
    private lateinit var strapiRepository: ConsultationStrapiRepository

    @Mock
    private lateinit var consultationInfoMapper: ConsultationInfoMapper

    @Mock
    private lateinit var clock: Clock

    @Mock
    private lateinit var cacheManager: CacheManager

    @Mock
    private lateinit var consultationStrapiCacheRepository: ConsultationStrapiCacheRepository

    @Mock
    private lateinit var userAnsweredConsultationsDatabaseRepository: fr.gouv.agora.infrastructure.userAnsweredConsultation.repository.UserAnsweredConsultationDatabaseRepository

    @Mock
    private lateinit var cache: Cache

    private val consultationInfo = ConsultationInfo(
        id = "doc-id-123",
        title = "Ma consultation",
        slug = "ma-consultation",
        coverUrl = "https://cover.jpg",
        detailsCoverUrl = "https://details-cover.jpg",
        startDate = LocalDateTime.of(2024, 1, 1, 0, 0),
        endDate = LocalDateTime.of(2024, 12, 31, 23, 59),
        questionCount = "10",
        estimatedTime = "5 min",
        participantCountGoal = 1000,
        thematique = Thematique(id = "thema-1", label = "Démocratie", picto = "🗳"),
        territory = "national",
        titreWeb = "Ma consultation web",
        sousTitreWeb = "Sous-titre",
    )

    private val strapiDTO = org.mockito.BDDMockito.mock(ConsultationStrapiDTO::class.java)

    @BeforeEach
    fun setUp() {
        given(cacheManager.getCache(ConsultationInfoRepositoryImpl.CONSULTATION_CACHE_NAME)).willReturn(cache)
    }

    @Nested
    inner class `getConsultationByIdOrSlug` {

        @Test
        fun `getConsultationByIdOrSlug - when cache hit on input key - should return cached value without calling strapi`() {
            // Given
            given(cache.get("ma-consultation", ConsultationInfo::class.java)).willReturn(consultationInfo)

            // When
            val result = repository.getConsultationByIdOrSlug("ma-consultation")

            // Then
            assertThat(result).isEqualTo(consultationInfo)
            then(strapiRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `getConsultationByIdOrSlug - when cache miss and strapi returns null - should return null`() {
            // Given
            given(cache.get("unknown-slug", ConsultationInfo::class.java)).willReturn(null)
            given(strapiRepository.getConsultationBySlug("unknown-slug")).willReturn(null)
            given(strapiRepository.getConsultationById("unknown-slug")).willReturn(null)

            // When
            val result = repository.getConsultationByIdOrSlug("unknown-slug")

            // Then
            assertThat(result).isNull()
        }

        @Test
        fun `getConsultationByIdOrSlug - when cache miss and strapi returns consultation by slug - should store under both slug and documentId`() {
            // Given
            given(cache.get("ma-consultation", ConsultationInfo::class.java)).willReturn(null)
            given(strapiRepository.getConsultationBySlug("ma-consultation")).willReturn(strapiDTO)
            given(consultationInfoMapper.toConsultationInfo(strapiDTO)).willReturn(consultationInfo)

            // When
            val result = repository.getConsultationByIdOrSlug("ma-consultation")

            // Then
            assertThat(result).isEqualTo(consultationInfo)
            // stored under the input key (slug)
            then(cache).should().put("ma-consultation", consultationInfo)
            // stored under the documentId (different from slug)
            then(cache).should().put("doc-id-123", consultationInfo)
        }

        @Test
        fun `getConsultationByIdOrSlug - when cache miss and strapi returns consultation by documentId - should store under both documentId and slug`() {
            // Given
            given(cache.get("doc-id-123", ConsultationInfo::class.java)).willReturn(null)
            given(strapiRepository.getConsultationBySlug("doc-id-123")).willReturn(null)
            given(strapiRepository.getConsultationById("doc-id-123")).willReturn(strapiDTO)
            given(consultationInfoMapper.toConsultationInfo(strapiDTO)).willReturn(consultationInfo)

            // When
            val result = repository.getConsultationByIdOrSlug("doc-id-123")

            // Then
            assertThat(result).isEqualTo(consultationInfo)
            // stored under the input key (documentId)
            then(cache).should().put("doc-id-123", consultationInfo)
            // stored under the slug (different from documentId)
            then(cache).should().put("ma-consultation", consultationInfo)
        }

        @Test
        fun `getConsultationByIdOrSlug - when cache miss and id equals slug - should store only once`() {
            // Given
            val sameIdAndSlugInfo = consultationInfo.copy(id = "same-key", slug = "same-key")
            given(cache.get("same-key", ConsultationInfo::class.java)).willReturn(null)
            given(strapiRepository.getConsultationBySlug("same-key")).willReturn(strapiDTO)
            given(consultationInfoMapper.toConsultationInfo(strapiDTO)).willReturn(sameIdAndSlugInfo)

            // When
            repository.getConsultationByIdOrSlug("same-key")

            // Then - only one put since all three keys are the same
            then(cache).should().put("same-key", sameIdAndSlugInfo)
        }
    }

    @Nested
    inner class `getConsultationByIdOrSlugWithUnpublished` {

        @Test
        fun `getConsultationByIdOrSlugWithUnpublished - when cache hit - should return cached value without calling strapi`() {
            // Given
            given(cache.get("ma-consultation", ConsultationInfo::class.java)).willReturn(consultationInfo)

            // When
            val result = repository.getConsultationByIdOrSlugWithUnpublished("ma-consultation")

            // Then
            assertThat(result).isEqualTo(consultationInfo)
            then(strapiRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `getConsultationByIdOrSlugWithUnpublished - when cache miss and strapi returns consultation by slug - should store under both slug and documentId`() {
            // Given
            given(cache.get("ma-consultation", ConsultationInfo::class.java)).willReturn(null)
            given(strapiRepository.getConsultationBySlugWithUnpublished("ma-consultation")).willReturn(strapiDTO)
            given(consultationInfoMapper.toConsultationInfo(strapiDTO)).willReturn(consultationInfo)

            // When
            val result = repository.getConsultationByIdOrSlugWithUnpublished("ma-consultation")

            // Then
            assertThat(result).isEqualTo(consultationInfo)
            then(cache).should().put("ma-consultation", consultationInfo)
            then(cache).should().put("doc-id-123", consultationInfo)
        }

        @Test
        fun `getConsultationByIdOrSlugWithUnpublished - when cache miss and strapi returns consultation by documentId - should store under both documentId and slug`() {
            // Given
            given(cache.get("doc-id-123", ConsultationInfo::class.java)).willReturn(null)
            given(strapiRepository.getConsultationBySlugWithUnpublished("doc-id-123")).willReturn(null)
            given(strapiRepository.getConsultationByIdWithUnpublished("doc-id-123")).willReturn(strapiDTO)
            given(consultationInfoMapper.toConsultationInfo(strapiDTO)).willReturn(consultationInfo)

            // When
            val result = repository.getConsultationByIdOrSlugWithUnpublished("doc-id-123")

            // Then
            assertThat(result).isEqualTo(consultationInfo)
            then(cache).should().put("doc-id-123", consultationInfo)
            then(cache).should().put("ma-consultation", consultationInfo)
        }

        @Test
        fun `getConsultationByIdOrSlugWithUnpublished - when cache miss and strapi returns null - should return null`() {
            // Given
            given(cache.get("unknown", ConsultationInfo::class.java)).willReturn(null)
            given(strapiRepository.getConsultationBySlugWithUnpublished("unknown")).willReturn(null)
            given(strapiRepository.getConsultationByIdWithUnpublished("unknown")).willReturn(null)

            // When
            val result = repository.getConsultationByIdOrSlugWithUnpublished("unknown")

            // Then
            assertThat(result).isNull()
            then(cache).should(never()).put(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any())
        }
    }
}
