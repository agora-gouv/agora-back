package fr.social.gouv.agora.infrastructure.consultationUpdates.repository

import fr.social.gouv.agora.domain.ConsultationStatus
import fr.social.gouv.agora.domain.ConsultationUpdate
import fr.social.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateDTO
import fr.social.gouv.agora.infrastructure.consultationUpdates.dto.ExplanationDTO
import fr.social.gouv.agora.infrastructure.utils.UuidUtils.NOT_FOUND_UUID
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
internal class ConsultationUpdateRepositoryImplTest {

    @Autowired
    private lateinit var repository: ConsultationUpdateRepositoryImpl

    @MockBean
    private lateinit var consultationUpdateDatabaseRepository: ConsultationUpdateDatabaseRepository

    @MockBean
    private lateinit var consultationUpdatesCacheRepository: ConsultationUpdatesCacheRepository

    @MockBean
    private lateinit var explanationCacheRepository: ExplanationCacheRepository

    @MockBean
    private lateinit var explanationDatabaseRepository: ExplanationDatabaseRepository

    @MockBean
    private lateinit var mapper: ConsultationUpdateMapper

    private val consultationUpdateDTO = ConsultationUpdateDTO(
        id = UUID.randomUUID(),
        step = 42,
        description = "dto-description",
        consultationId = UUID.randomUUID(),
        explanationsTitle = null,
        videoTitle = null,
        videoIntro = null,
        videoUrl = null,
        videoWidth = null,
        videoHeight = null,
        videoTranscription = null,
        conclusionTitle = null,
        conclusionDescription = null,
    )

    private val consultationUpdate = ConsultationUpdate(
        status = ConsultationStatus.COLLECTING_DATA,
        description = "domain-description",
        explanationsTitle = null,
        explanations = emptyList(),
        video = null,
        conclusion = null,
    )

    private val consultationUpdateNotFound = ConsultationUpdateDTO(
        id = NOT_FOUND_UUID,
        step = 0,
        description = "",
        consultationId = NOT_FOUND_UUID,
        explanationsTitle = null,
        videoTitle = null,
        videoIntro = null,
        videoUrl = null,
        videoWidth = null,
        videoHeight = null,
        videoTranscription = null,
        conclusionTitle = null,
        conclusionDescription = null,
    )

    @Test
    fun `getConsultationUpdate - when invalid UUID - should return null`() {
        // When
        val result = repository.getConsultationUpdate("invalidUUID")

        // Then
        assertThat(result).isEqualTo(null)
        then(consultationUpdatesCacheRepository).shouldHaveNoInteractions()
        then(consultationUpdateDatabaseRepository).shouldHaveNoInteractions()
        then(explanationCacheRepository).shouldHaveNoInteractions()
        then(explanationDatabaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationUpdate - when no cache and no DTOs in database - should return null`() {
        // Given
        val uuid = UUID.randomUUID()
        given(consultationUpdatesCacheRepository.getConsultationUpdate(uuid)).willReturn(
            ConsultationUpdatesCacheRepository.CacheResult.CacheNotInitialized
        )
        given(consultationUpdateDatabaseRepository.getLastConsultationUpdate(uuid)).willReturn(null)

        // When
        val result = repository.getConsultationUpdate(uuid.toString())

        // Then
        assertThat(result).isEqualTo(null)
        then(consultationUpdateDatabaseRepository).should(only()).getLastConsultationUpdate(uuid)
        then(consultationUpdatesCacheRepository).should(times(1)).getConsultationUpdate(uuid)
        then(consultationUpdatesCacheRepository).should(times(1))
            .insertConsultationUpdate(uuid, consultationUpdateNotFound)
        then(explanationCacheRepository).shouldHaveNoInteractions()
        then(explanationDatabaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationUpdate - when no cache but DTOs exist in database - should return mapped DTO`() {
        // Given
        val uuid = UUID.randomUUID()
        val explanationDTO = mock(ExplanationDTO::class.java)
        given(consultationUpdatesCacheRepository.getConsultationUpdate(uuid)).willReturn(
            ConsultationUpdatesCacheRepository.CacheResult.CacheNotInitialized
        )
        given(consultationUpdateDatabaseRepository.getLastConsultationUpdate(uuid)).willReturn(consultationUpdateDTO)
        given(explanationCacheRepository.getExplanationList(consultationUpdateDTO.id)).willReturn(
            ExplanationCacheRepository.CacheResult.CacheNotInitialized
        )
        given(explanationDatabaseRepository.getExplanationList(consultationUpdateDTO.id)).willReturn(
            listOf(
                explanationDTO
            )
        )
        given(mapper.toDomain(consultationUpdateDTO, listOf(explanationDTO))).willReturn(consultationUpdate)

        // When
        val result = repository.getConsultationUpdate(uuid.toString())

        // Then
        assertThat(result).isEqualTo(consultationUpdate)
        then(consultationUpdateDatabaseRepository).should(only()).getLastConsultationUpdate(uuid)
        then(consultationUpdatesCacheRepository).should(times(1)).getConsultationUpdate(uuid)
        then(consultationUpdatesCacheRepository).should(times(1))
            .insertConsultationUpdate(uuid, consultationUpdateDTO)
        then(explanationCacheRepository).should(times(1)).getExplanationList(consultationUpdateDTO.id)
        then(explanationCacheRepository).should(times(1))
            .insertExplanationList(consultationUpdateDTO.id, listOf(explanationDTO))
        then(explanationDatabaseRepository).should(only()).getExplanationList(consultationUpdateDTO.id)
        then(mapper).should(only()).toDomain(consultationUpdateDTO, listOf(explanationDTO))
    }

    @Test
    fun `getConsultationUpdate - when CachedConsultationUpdateNotFound - should return null`() {
        // Given
        val uuid = UUID.randomUUID()
        given(consultationUpdatesCacheRepository.getConsultationUpdate(uuid)).willReturn(
            ConsultationUpdatesCacheRepository.CacheResult.CachedConsultationUpdateNotFound
        )

        // When
        val result = repository.getConsultationUpdate(uuid.toString())

        // Then
        assertThat(result).isEqualTo(null)
        then(consultationUpdatesCacheRepository).should(only()).getConsultationUpdate(uuid)
        then(consultationUpdateDatabaseRepository).shouldHaveNoInteractions()
        then(explanationCacheRepository).shouldHaveNoInteractions()
        then(explanationDatabaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationUpdate - when DTOs exist in cache - should return mapped DTO from cache`() {
        // Given
        val uuid = UUID.randomUUID()
        val explanationDTO = mock(ExplanationDTO::class.java)
        given(consultationUpdatesCacheRepository.getConsultationUpdate(uuid)).willReturn(
            ConsultationUpdatesCacheRepository.CacheResult.CachedConsultationUpdate(consultationUpdateDTO)
        )
        given(explanationCacheRepository.getExplanationList(consultationUpdateDTO.id)).willReturn(
            ExplanationCacheRepository.CacheResult.CachedExplanationList(listOf(explanationDTO))
        )
        given(mapper.toDomain(consultationUpdateDTO, listOf(explanationDTO))).willReturn(consultationUpdate)

        // When
        val result = repository.getConsultationUpdate(uuid.toString())

        // Then
        assertThat(result).isEqualTo(consultationUpdate)
        then(consultationUpdatesCacheRepository).should(only()).getConsultationUpdate(uuid)
        then(explanationCacheRepository).should(only()).getExplanationList(consultationUpdateDTO.id)
        then(mapper).should(only()).toDomain(consultationUpdateDTO, listOf(explanationDTO))
        then(consultationUpdateDatabaseRepository).shouldHaveNoInteractions()
        then(explanationDatabaseRepository).shouldHaveNoInteractions()
    }
}