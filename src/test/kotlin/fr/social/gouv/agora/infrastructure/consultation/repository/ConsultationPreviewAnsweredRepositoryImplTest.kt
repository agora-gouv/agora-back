package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.domain.ConsultationPreviewAnsweredInfo
import fr.social.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import fr.social.gouv.agora.infrastructure.consultation.repository.ConsultationAnsweredCacheRepository.CacheResult
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
internal class ConsultationPreviewAnsweredRepositoryImplTest {

    @Autowired
    private lateinit var repository: ConsultationPreviewAnsweredRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: ConsultationDatabaseRepository

    @MockBean
    private lateinit var cacheRepository: ConsultationAnsweredCacheRepository

    @MockBean
    private lateinit var mapper: ConsultationPreviewAnsweredMapper

    private val consultationDTO = ConsultationDTO(
        id = UUID.randomUUID(),
        title = "dto-title",
        abstract = "dto-abstract",
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

    private val consultationPreviewAnsweredInfo = ConsultationPreviewAnsweredInfo(
        id = "1234",
        title = "domain-title",
        coverUrl = "domain-cover-url",
        thematiqueId = "5678",
    )

    private val userId = UUID.fromString("c6655dd2-ee48-11ed-a05b-0242ac120003")

    @Test
    fun `getConsultationAnsweredList - when invalid userId - should return null`() {
        // When
        val result = repository.getConsultationAnsweredList("1111")

        // Then
        assertThat(result).isNull()
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationAnsweredList - when CacheNotInitialized & database return emptyList - should call getConsultationAnsweredListFromDatabase and insert emptylist to cache`() {
        // Given
        given(cacheRepository.getConsultationAnsweredList(userId))
            .willReturn(CacheResult.CacheNotInitialized)

        given(databaseRepository.getConsultationAnsweredList(userId)).willReturn(emptyList())

        // When
        val result = repository.getConsultationAnsweredList(userId.toString())

        // Then
        assertThat(result).isEqualTo(emptyList<ConsultationPreviewAnsweredInfo>())
        then(cacheRepository).should(times(1)).getConsultationAnsweredList(userId)
        then(cacheRepository).should(times(1))
            .insertConsultationAnsweredList(userId = userId, consultationAnsweredListDTO = emptyList())
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(databaseRepository).should(only()).getConsultationAnsweredList(userId)
    }

    @Test
    fun `getConsultationAnsweredList - when CacheNotInitialized & database return listof DTO - should call getConsultationAnsweredListFromDatabase and insert result to cache`() {
        // Given
        given(cacheRepository.getConsultationAnsweredList(userId))
            .willReturn(CacheResult.CacheNotInitialized)

        given(databaseRepository.getConsultationAnsweredList(userId)).willReturn(listOf(consultationDTO))
        given(mapper.toDomain(consultationDTO)).willReturn(consultationPreviewAnsweredInfo)

        // When
        val result = repository.getConsultationAnsweredList(userId.toString())

        // Then
        assertThat(result).isEqualTo(listOf(consultationPreviewAnsweredInfo))
        then(cacheRepository).should(times(1)).getConsultationAnsweredList(userId)
        then(cacheRepository).should(times(1))
            .insertConsultationAnsweredList(userId = userId, consultationAnsweredListDTO = listOf(consultationDTO))
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(databaseRepository).should(only()).getConsultationAnsweredList(userId)
    }

    @Test
    fun `getConsultationAnsweredList - when has cache with emptyList - should return emptylist from cache`() {
        // Given
        given(cacheRepository.getConsultationAnsweredList(userId))
            .willReturn(CacheResult.CachedConsultationAnsweredList(emptyList()))

        // When
        val result = repository.getConsultationAnsweredList(userId.toString())

        // Then
        assertThat(result).isEqualTo(emptyList<ConsultationPreviewAnsweredInfo>())
        then(cacheRepository).should(only()).getConsultationAnsweredList(userId)
        then(databaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationAnsweredList - when has cache with listof DTO - should return listof DTO from cache`() {
        // Given
        given(cacheRepository.getConsultationAnsweredList(userId))
            .willReturn(CacheResult.CachedConsultationAnsweredList(listOf(consultationDTO)))
        given(mapper.toDomain(consultationDTO)).willReturn(consultationPreviewAnsweredInfo)

        // When
        val result = repository.getConsultationAnsweredList(userId.toString())

        // Then
        assertThat(result).isEqualTo(listOf(consultationPreviewAnsweredInfo))
        then(cacheRepository).should(only()).getConsultationAnsweredList(userId)
        then(databaseRepository).shouldHaveNoInteractions()
    }
}