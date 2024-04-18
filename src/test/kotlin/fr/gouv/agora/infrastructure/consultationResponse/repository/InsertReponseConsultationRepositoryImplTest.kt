package fr.gouv.agora.infrastructure.consultationResponse.repository

import fr.gouv.agora.domain.ReponseConsultationInserting
import fr.gouv.agora.infrastructure.consultationResponse.dto.ReponseConsultationDTO
import fr.gouv.agora.usecase.consultationResponse.repository.InsertReponseConsultationRepository.InsertParameters
import fr.gouv.agora.usecase.consultationResponse.repository.InsertReponseConsultationRepository.InsertResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class InsertReponseConsultationRepositoryImplTest {

    @InjectMocks
    private lateinit var repository: InsertReponseConsultationRepositoryImpl

    @Mock
    private lateinit var cacheRepository: ReponseConsultationCacheRepository

    @Mock
    private lateinit var databaseRepository: ReponseConsultationDatabaseRepository

    @Mock
    private lateinit var mapper: ReponseConsultationMapper

    private val consultationId = UUID.randomUUID()
    private val userId = UUID.randomUUID()
    private val participationId = UUID.randomUUID()
    private val insertParameters = InsertParameters(
        consultationId = consultationId.toString(),
        userId = userId.toString(),
        participationId = participationId.toString(),
    )

    @Test
    fun `insertConsultationResponses - when consultationId is invalid UUID - should return Failure`() {
        // When
        val result = repository.insertConsultationResponses(
            insertParameters = insertParameters.copy(
                consultationId = "invalid consultationId",
            ),
            consultationResponses = listOf(mock(ReponseConsultationInserting::class.java)),
        )

        // Then
        assertThat(result).isEqualTo(InsertResult.INSERT_FAILURE)
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `insertConsultationResponses - when userId is invalid UUID - should return Failure`() {
        // When
        val result = repository.insertConsultationResponses(
            insertParameters = insertParameters.copy(
                userId = "invalid userID",
            ),
            consultationResponses = listOf(mock(ReponseConsultationInserting::class.java)),
        )

        // Then
        assertThat(result).isEqualTo(InsertResult.INSERT_FAILURE)
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `insertConsultationResponses - when participationId is invalid UUID - should return Failure`() {
        // When
        val result = repository.insertConsultationResponses(
            insertParameters = insertParameters.copy(
                participationId = "invalid participationId",
            ),
            consultationResponses = listOf(mock(ReponseConsultationInserting::class.java)),
        )

        // Then
        assertThat(result).isEqualTo(InsertResult.INSERT_FAILURE)
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `insertConsultationResponses - when consultationResponses is emptyList - should return Success without interacting with cache nor database`() {
        // When
        val result = repository.insertConsultationResponses(
            insertParameters = insertParameters,
            consultationResponses = emptyList(),
        )

        // Then
        assertThat(result).isEqualTo(InsertResult.INSERT_SUCCESS)
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `insertConsultationResponses - when consultationResponses is not empty - should map objects to dto then insert to cache & database then return Success`() {
        // Given
        val reponseConsultationInserting = mock(ReponseConsultationInserting::class.java)
        val reponseConsultationDTO = mock(ReponseConsultationDTO::class.java)
        given(
            mapper.toDto(
                consultationId = consultationId,
                userId = userId,
                participationId = participationId,
                domain = reponseConsultationInserting,
            )
        ).willReturn(listOf(reponseConsultationDTO))

        val savedReponseConsultationDTO = mock(ReponseConsultationDTO::class.java)
        given(databaseRepository.saveAll(listOf(reponseConsultationDTO))).willReturn(listOf(savedReponseConsultationDTO))

        // When
        val result = repository.insertConsultationResponses(
            insertParameters = insertParameters,
            consultationResponses = listOf(reponseConsultationInserting),
        )

        // Then
        assertThat(result).isEqualTo(InsertResult.INSERT_SUCCESS)
        then(databaseRepository).should(only()).saveAll(listOf(reponseConsultationDTO))
        then(cacheRepository).should(only()).insertReponseConsultationList(
            consultationId = consultationId,
            reponseConsultationList = listOf(savedReponseConsultationDTO),
        )
    }
}