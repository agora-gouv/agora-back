package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.domain.ConsultationPreviewOngoingInfo
import fr.social.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import fr.social.gouv.agora.infrastructure.consultation.repository.ConsultationOngoingCacheRepository.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class ConsultationPreviewOngoingRepositoryImplTest {

    @Autowired
    private lateinit var repository: ConsultationPreviewOngoingRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: ConsultationDatabaseRepository

    @MockBean
    private lateinit var cacheRepository: ConsultationOngoingCacheRepository

    @MockBean
    private lateinit var mapper: ConsultationPreviewOngoingInfoMapper

    private val consultationDTO = ConsultationDTO(
        id = UUID.randomUUID(),
        title = "dto-title",
        startDate = Date(0),
        endDate = Date(1),
        coverUrl = "dto-cover_url",
        questionCount = "dto-question_count",
        estimatedTime = "dto-estimated_time",
        participantCountGoal = 6401,
        description = "dto-description",
        tipsDescription = "dto-tips_description",
        thematiqueId = UUID.randomUUID(),
    )

    private val consultationPreviewOngoingInfo = ConsultationPreviewOngoingInfo(
        id = "1234",
        title = "domain-title",
        coverUrl = "domain-cover-url",
        endDate = Date(1),
        thematiqueId = "5678",
    )

    @Test
    fun `getConsultationPreviewOngoingList - when CacheNotInitialized - should call getConsultationOngoingListFromDatabase and insert result (case result = null) to cache`() {
        // Given
        given(cacheRepository.getConsultationOngoingList()).willReturn(CacheResult.CacheNotInitialized)
        given(databaseRepository.getConsultationOngoingList()).willReturn(emptyList())

        // When
        val result = repository.getConsultationPreviewOngoingList()

        // Then
        assertThat(result).isEqualTo(emptyList<ConsultationPreviewOngoingInfo>())
        then(cacheRepository).should().getConsultationOngoingList()
        then(cacheRepository).should().insertConsultationOngoingList(emptyList())
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(databaseRepository).should(only()).getConsultationOngoingList()
    }

    @Test
    fun `getConsultationPreviewOngoingList - when CacheNotInitialized - should call getConsultationOngoingListFromDatabase and insert result (case result not null) to cache`() {
        // Given
        given(cacheRepository.getConsultationOngoingList())
            .willReturn(CacheResult.CacheNotInitialized)

        given(databaseRepository.getConsultationOngoingList()).willReturn(listOf(consultationDTO))
        given(mapper.toDomain(consultationDTO)).willReturn(consultationPreviewOngoingInfo)

        // When
        val result = repository.getConsultationPreviewOngoingList()

        // Then
        assertThat(result).isEqualTo(listOf(consultationPreviewOngoingInfo))
        then(cacheRepository).should().getConsultationOngoingList()
        then(cacheRepository).should().insertConsultationOngoingList(listOf(consultationDTO))
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(databaseRepository).should(only()).getConsultationOngoingList()
    }

    @Test
    fun `getConsultationPreviewOngoingList - when has cache with emptyList - should return emptylist from cache`() {
        // Given
        given(cacheRepository.getConsultationOngoingList())
            .willReturn(CacheResult.CachedConsultationOngoingList(emptyList()))

        // When
        val result = repository.getConsultationPreviewOngoingList()

        // Then
        assertThat(result).isEqualTo(emptyList<ConsultationPreviewOngoingInfo>())
        then(cacheRepository).should(only()).getConsultationOngoingList()
        then(databaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationPreviewOngoingList - when has cache with listof DTO - should return listof DTO from cache`() {
        // Given
        given(cacheRepository.getConsultationOngoingList())
            .willReturn(CacheResult.CachedConsultationOngoingList(listOf(consultationDTO)))
        given(mapper.toDomain(consultationDTO)).willReturn(consultationPreviewOngoingInfo)

        // When
        val result = repository.getConsultationPreviewOngoingList()

        // Then
        assertThat(result).isEqualTo(listOf(consultationPreviewOngoingInfo))
        then(cacheRepository).should(only()).getConsultationOngoingList()
        then(databaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `clearCache - should only clear cache`() {
        // When
        repository.clearCache()

        // Then
        then(cacheRepository).should(only()).clearCache()
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }
}