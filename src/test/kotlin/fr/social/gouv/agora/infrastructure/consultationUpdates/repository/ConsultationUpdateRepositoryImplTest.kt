package fr.social.gouv.agora.infrastructure.consultationUpdates.repository

import fr.social.gouv.agora.domain.ConsultationStatus
import fr.social.gouv.agora.domain.ConsultationUpdate
import fr.social.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateDTO
import fr.social.gouv.agora.infrastructure.consultationUpdates.dto.ExplanationDTO
import fr.social.gouv.agora.infrastructure.utils.UuidUtils.NOT_FOUND_UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
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
    private lateinit var updateDatabaseRepository: ConsultationUpdateDatabaseRepository

    @MockBean
    private lateinit var updatesCacheRepository: ConsultationUpdateCacheRepository

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
        consultationId = "consultationId",
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

    @Nested
    inner class OngoingConsultationUpdateTestCases {

        @Test
        fun `getOngoingConsultationUpdate - when invalid UUID - should return null`() {
            // When
            val result = repository.getOngoingConsultationUpdate("invalidUUID")

            // Then
            assertThat(result).isEqualTo(null)
            then(updateDatabaseRepository).shouldHaveNoInteractions()
            then(updatesCacheRepository).shouldHaveNoInteractions()
            then(explanationDatabaseRepository).shouldHaveNoInteractions()
            then(explanationCacheRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getOngoingConsultationUpdate - when CacheNotInitialized but no update in database - should return null`() {
            // Given
            val uuid = UUID.randomUUID()
            given(updatesCacheRepository.getOngoingConsultationUpdate(uuid))
                .willReturn(ConsultationUpdateCacheRepository.CacheResult.CacheNotInitialized)
            given(updateDatabaseRepository.getOngoingConsultationUpdate(uuid)).willReturn(null)

            // When
            val result = repository.getOngoingConsultationUpdate(uuid.toString())

            // Then
            assertThat(result).isEqualTo(null)
            then(updateDatabaseRepository).should(only()).getOngoingConsultationUpdate(uuid)
            then(updatesCacheRepository).should().getOngoingConsultationUpdate(uuid)
            then(updatesCacheRepository).should().insertOngoingConsultationUpdate(uuid, consultationUpdateNotFound)
            then(updatesCacheRepository).shouldHaveNoMoreInteractions()
            then(explanationDatabaseRepository).shouldHaveNoInteractions()
            then(explanationCacheRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getOngoingConsultationUpdate - when CacheNotInitialized and has update in database - should return mapped DTO`() {
            // Given
            val uuid = UUID.randomUUID()
            given(updatesCacheRepository.getOngoingConsultationUpdate(uuid))
                .willReturn(ConsultationUpdateCacheRepository.CacheResult.CacheNotInitialized)
            given(updateDatabaseRepository.getOngoingConsultationUpdate(uuid)).willReturn(consultationUpdateDTO)
            given(explanationCacheRepository.getExplanationList(consultationUpdateDTO.id))
                .willReturn(ExplanationCacheRepository.CacheResult.CachedExplanationList(emptyList()))
            given(mapper.toDomain(consultationUpdateDTO, emptyList())).willReturn(consultationUpdate)

            // When
            val result = repository.getOngoingConsultationUpdate(uuid.toString())

            // Then
            assertThat(result).isEqualTo(consultationUpdate)
            then(updateDatabaseRepository).should(only()).getOngoingConsultationUpdate(uuid)
            then(updatesCacheRepository).should().getOngoingConsultationUpdate(uuid)
            then(updatesCacheRepository).should().insertOngoingConsultationUpdate(uuid, consultationUpdateDTO)
            then(updatesCacheRepository).shouldHaveNoMoreInteractions()

            then(explanationDatabaseRepository).shouldHaveNoInteractions()
            then(explanationCacheRepository).should(only()).getExplanationList(consultationUpdateDTO.id)

            then(mapper).should(only()).toDomain(consultationUpdateDTO, emptyList())
        }

        @Test
        fun `getOngoingConsultationUpdate - when CachedConsultationUpdateNotFound - should return null`() {
            // Given
            val uuid = UUID.randomUUID()
            given(updatesCacheRepository.getOngoingConsultationUpdate(uuid))
                .willReturn(ConsultationUpdateCacheRepository.CacheResult.CachedConsultationUpdateNotFound)

            // When
            val result = repository.getOngoingConsultationUpdate(uuid.toString())

            // Then
            assertThat(result).isEqualTo(null)
            then(updateDatabaseRepository).shouldHaveNoInteractions()
            then(updatesCacheRepository).should(only()).getOngoingConsultationUpdate(uuid)

            then(explanationDatabaseRepository).shouldHaveNoInteractions()
            then(explanationCacheRepository).shouldHaveNoInteractions()

            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getOngoingConsultationUpdate - when CachedConsultationUpdate and CachedExplanationList - should return mapped DTO from cache`() {
            // Given
            val uuid = UUID.randomUUID()
            given(updatesCacheRepository.getOngoingConsultationUpdate(uuid)).willReturn(
                ConsultationUpdateCacheRepository.CacheResult.CachedConsultationUpdate(consultationUpdateDTO)
            )
            given(explanationCacheRepository.getExplanationList(consultationUpdateDTO.id))
                .willReturn(ExplanationCacheRepository.CacheResult.CachedExplanationList(emptyList()))
            given(mapper.toDomain(consultationUpdateDTO, emptyList())).willReturn(consultationUpdate)

            // When
            val result = repository.getOngoingConsultationUpdate(uuid.toString())

            // Then
            assertThat(result).isEqualTo(consultationUpdate)
            then(updateDatabaseRepository).shouldHaveNoInteractions()
            then(updatesCacheRepository).should(only()).getOngoingConsultationUpdate(uuid)

            then(explanationDatabaseRepository).shouldHaveNoInteractions()
            then(explanationCacheRepository).should(only()).getExplanationList(consultationUpdateDTO.id)

            then(mapper).should(only()).toDomain(consultationUpdateDTO, emptyList())
        }

    }

    @Nested
    inner class OngoingConsultationExplanationTestCases {

        private val uuid = UUID.randomUUID()

        @BeforeEach
        fun setUp() {
            given(updatesCacheRepository.getOngoingConsultationUpdate(uuid)).willReturn(
                ConsultationUpdateCacheRepository.CacheResult.CachedConsultationUpdate(consultationUpdateDTO)
            )
        }

        @Test
        fun `getOngoingConsultationUpdate - when CachedConsultationUpdate and CachedExplanationList - should return mapped DTO from cache`() {
            // Given
            val explanationDTO = mock(ExplanationDTO::class.java)
            given(explanationCacheRepository.getExplanationList(consultationUpdateDTO.id))
                .willReturn(ExplanationCacheRepository.CacheResult.CachedExplanationList(listOf(explanationDTO)))
            given(mapper.toDomain(consultationUpdateDTO, listOf(explanationDTO))).willReturn(consultationUpdate)

            // When
            val result = repository.getOngoingConsultationUpdate(uuid.toString())

            // Then
            assertThat(result).isEqualTo(consultationUpdate)
            then(updatesCacheRepository).should(only()).getOngoingConsultationUpdate(uuid)
            then(updateDatabaseRepository).shouldHaveNoInteractions()

            then(explanationCacheRepository).should(only()).getExplanationList(consultationUpdateDTO.id)
            then(explanationDatabaseRepository).shouldHaveNoInteractions()

            then(mapper).should(only()).toDomain(consultationUpdateDTO, listOf(explanationDTO))
        }

        @Test
        fun `getOngoingConsultationUpdate - when CachedConsultationUpdate and CacheNotInitialized - should init explanation cache then return mapped DTO`() {
            // Given
            val explanationDTO = mock(ExplanationDTO::class.java)
            given(explanationCacheRepository.getExplanationList(consultationUpdateDTO.id))
                .willReturn(ExplanationCacheRepository.CacheResult.CacheNotInitialized)
            given(explanationDatabaseRepository.getExplanationList(consultationUpdateDTO.id))
                .willReturn(listOf(explanationDTO))

            given(mapper.toDomain(consultationUpdateDTO, listOf(explanationDTO))).willReturn(consultationUpdate)

            // When
            val result = repository.getOngoingConsultationUpdate(uuid.toString())

            // Then
            assertThat(result).isEqualTo(consultationUpdate)
            then(updatesCacheRepository).should(only()).getOngoingConsultationUpdate(uuid)
            then(updateDatabaseRepository).shouldHaveNoInteractions()

            then(explanationCacheRepository).should().getExplanationList(consultationUpdateDTO.id)
            then(explanationCacheRepository).should()
                .insertExplanationList(consultationUpdateDTO.id, listOf(explanationDTO))
            then(explanationCacheRepository).shouldHaveNoMoreInteractions()
            then(explanationDatabaseRepository).should(only()).getExplanationList(consultationUpdateDTO.id)

            then(mapper).should(only()).toDomain(consultationUpdateDTO, listOf(explanationDTO))
        }

    }

    @Nested
    inner class FinishedConsultationUpdateTestCases {

        @Test
        fun `getFinishedConsultationUpdate - when invalid UUID - should return null`() {
            // When
            val result = repository.getFinishedConsultationUpdate("invalidUUID")

            // Then
            assertThat(result).isEqualTo(null)
            then(updateDatabaseRepository).shouldHaveNoInteractions()
            then(updatesCacheRepository).shouldHaveNoInteractions()
            then(explanationDatabaseRepository).shouldHaveNoInteractions()
            then(explanationCacheRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getFinishedConsultationUpdate - when CacheNotInitialized but no update in database - should return null`() {
            // Given
            val uuid = UUID.randomUUID()
            given(updatesCacheRepository.getFinishedConsultationUpdate(uuid))
                .willReturn(ConsultationUpdateCacheRepository.CacheResult.CacheNotInitialized)
            given(updateDatabaseRepository.getFinishedConsultationUpdate(uuid)).willReturn(null)

            // When
            val result = repository.getFinishedConsultationUpdate(uuid.toString())

            // Then
            assertThat(result).isEqualTo(null)
            then(updateDatabaseRepository).should(only()).getFinishedConsultationUpdate(uuid)
            then(updatesCacheRepository).should().getFinishedConsultationUpdate(uuid)
            then(updatesCacheRepository).should().insertFinishedConsultationUpdate(uuid, consultationUpdateNotFound)
            then(updatesCacheRepository).shouldHaveNoMoreInteractions()
            then(explanationDatabaseRepository).shouldHaveNoInteractions()
            then(explanationCacheRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getFinishedConsultationUpdate - when CacheNotInitialized and has update in database - should return mapped DTO`() {
            // Given
            val uuid = UUID.randomUUID()
            given(updatesCacheRepository.getFinishedConsultationUpdate(uuid))
                .willReturn(ConsultationUpdateCacheRepository.CacheResult.CacheNotInitialized)
            given(updateDatabaseRepository.getFinishedConsultationUpdate(uuid)).willReturn(consultationUpdateDTO)
            given(explanationCacheRepository.getExplanationList(consultationUpdateDTO.id))
                .willReturn(ExplanationCacheRepository.CacheResult.CachedExplanationList(emptyList()))
            given(mapper.toDomain(consultationUpdateDTO, emptyList())).willReturn(consultationUpdate)

            // When
            val result = repository.getFinishedConsultationUpdate(uuid.toString())

            // Then
            assertThat(result).isEqualTo(consultationUpdate)
            then(updateDatabaseRepository).should(only()).getFinishedConsultationUpdate(uuid)
            then(updatesCacheRepository).should().getFinishedConsultationUpdate(uuid)
            then(updatesCacheRepository).should().insertFinishedConsultationUpdate(uuid, consultationUpdateDTO)
            then(updatesCacheRepository).shouldHaveNoMoreInteractions()

            then(explanationDatabaseRepository).shouldHaveNoInteractions()
            then(explanationCacheRepository).should(only()).getExplanationList(consultationUpdateDTO.id)

            then(mapper).should(only()).toDomain(consultationUpdateDTO, emptyList())
        }

        @Test
        fun `getFinishedConsultationUpdate - when CachedConsultationUpdateNotFound - should return null`() {
            // Given
            val uuid = UUID.randomUUID()
            given(updatesCacheRepository.getFinishedConsultationUpdate(uuid))
                .willReturn(ConsultationUpdateCacheRepository.CacheResult.CachedConsultationUpdateNotFound)

            // When
            val result = repository.getFinishedConsultationUpdate(uuid.toString())

            // Then
            assertThat(result).isEqualTo(null)
            then(updateDatabaseRepository).shouldHaveNoInteractions()
            then(updatesCacheRepository).should(only()).getFinishedConsultationUpdate(uuid)

            then(explanationDatabaseRepository).shouldHaveNoInteractions()
            then(explanationCacheRepository).shouldHaveNoInteractions()

            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getFinishedConsultationUpdate - when CachedConsultationUpdate and CachedExplanationList - should return mapped DTO from cache`() {
            // Given
            val uuid = UUID.randomUUID()
            given(updatesCacheRepository.getFinishedConsultationUpdate(uuid)).willReturn(
                ConsultationUpdateCacheRepository.CacheResult.CachedConsultationUpdate(consultationUpdateDTO)
            )
            given(explanationCacheRepository.getExplanationList(consultationUpdateDTO.id))
                .willReturn(ExplanationCacheRepository.CacheResult.CachedExplanationList(emptyList()))
            given(mapper.toDomain(consultationUpdateDTO, emptyList())).willReturn(consultationUpdate)

            // When
            val result = repository.getFinishedConsultationUpdate(uuid.toString())

            // Then
            assertThat(result).isEqualTo(consultationUpdate)
            then(updateDatabaseRepository).shouldHaveNoInteractions()
            then(updatesCacheRepository).should(only()).getFinishedConsultationUpdate(uuid)

            then(explanationDatabaseRepository).shouldHaveNoInteractions()
            then(explanationCacheRepository).should(only()).getExplanationList(consultationUpdateDTO.id)

            then(mapper).should(only()).toDomain(consultationUpdateDTO, emptyList())
        }

    }

    @Nested
    inner class FinishedConsultationExplanationTestCases {

        private val uuid = UUID.randomUUID()

        @BeforeEach
        fun setUp() {
            given(updatesCacheRepository.getFinishedConsultationUpdate(uuid)).willReturn(
                ConsultationUpdateCacheRepository.CacheResult.CachedConsultationUpdate(consultationUpdateDTO)
            )
        }

        @Test
        fun `getFinishedConsultationUpdate - when CachedConsultationUpdate and CachedExplanationList - should return mapped DTO from cache`() {
            // Given
            val explanationDTO = mock(ExplanationDTO::class.java)
            given(explanationCacheRepository.getExplanationList(consultationUpdateDTO.id))
                .willReturn(ExplanationCacheRepository.CacheResult.CachedExplanationList(listOf(explanationDTO)))
            given(mapper.toDomain(consultationUpdateDTO, listOf(explanationDTO))).willReturn(consultationUpdate)

            // When
            val result = repository.getFinishedConsultationUpdate(uuid.toString())

            // Then
            assertThat(result).isEqualTo(consultationUpdate)
            then(updatesCacheRepository).should(only()).getFinishedConsultationUpdate(uuid)
            then(updateDatabaseRepository).shouldHaveNoInteractions()

            then(explanationCacheRepository).should(only()).getExplanationList(consultationUpdateDTO.id)
            then(explanationDatabaseRepository).shouldHaveNoInteractions()

            then(mapper).should(only()).toDomain(consultationUpdateDTO, listOf(explanationDTO))
        }

        @Test
        fun `getFinishedConsultationUpdate - when CachedConsultationUpdate and CacheNotInitialized - should init explanation cache then return mapped DTO`() {
            // Given
            val explanationDTO = mock(ExplanationDTO::class.java)
            given(explanationCacheRepository.getExplanationList(consultationUpdateDTO.id))
                .willReturn(ExplanationCacheRepository.CacheResult.CacheNotInitialized)
            given(explanationDatabaseRepository.getExplanationList(consultationUpdateDTO.id))
                .willReturn(listOf(explanationDTO))

            given(mapper.toDomain(consultationUpdateDTO, listOf(explanationDTO))).willReturn(consultationUpdate)

            // When
            val result = repository.getFinishedConsultationUpdate(uuid.toString())

            // Then
            assertThat(result).isEqualTo(consultationUpdate)
            then(updatesCacheRepository).should(only()).getFinishedConsultationUpdate(uuid)
            then(updateDatabaseRepository).shouldHaveNoInteractions()

            then(explanationCacheRepository).should().getExplanationList(consultationUpdateDTO.id)
            then(explanationCacheRepository).should()
                .insertExplanationList(consultationUpdateDTO.id, listOf(explanationDTO))
            then(explanationCacheRepository).shouldHaveNoMoreInteractions()
            then(explanationDatabaseRepository).should(only()).getExplanationList(consultationUpdateDTO.id)

            then(mapper).should(only()).toDomain(consultationUpdateDTO, listOf(explanationDTO))
        }

    }
}