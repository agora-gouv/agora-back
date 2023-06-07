package fr.social.gouv.agora.usecase.reponseConsultation

import fr.social.gouv.agora.domain.ReponseConsultationInserting
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewAnsweredRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository.InsertParameters
import fr.social.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository.InsertResult
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
internal class InsertReponseConsultationUseCaseTest {

    @Autowired
    private lateinit var useCase: InsertReponseConsultationUseCase

    @MockBean
    private lateinit var consultationPreviewAnsweredRepository: ConsultationPreviewAnsweredRepository

    @MockBean
    private lateinit var insertReponseConsultationRepository: InsertReponseConsultationRepository

    @MockBean
    private lateinit var consultationResponseRepository: GetConsultationResponseRepository

    @MockBean
    private lateinit var insertConsultationResponseParametersMapper: InsertConsultationResponseParametersMapper

    @Test
    fun `insertReponseConsultation - when has already answered - should return failure`() {
        // Given
        given(
            consultationResponseRepository.hasAnsweredConsultation(
                consultationId = "consultationId",
                userId = "userId",
            )
        ).willReturn(true)

        // When
        val result = useCase.insertReponseConsultation(
            consultationId = "consultationId",
            userId = "userId",
            consultationResponses = listOf(mock(ReponseConsultationInserting::class.java)),
        )

        // Then
        assertThat(result).isEqualTo(InsertResult.INSERT_FAILURE)
        then(consultationResponseRepository).should(only()).hasAnsweredConsultation(
            consultationId = "consultationId",
            userId = "userId",
        )
        then(consultationPreviewAnsweredRepository).shouldHaveNoInteractions()
        then(insertReponseConsultationRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `insertReponseConsultation - when has not answered yet - should ConsultationAnswered cache then return result from insert repository`() {
        // Given
        given(
            consultationResponseRepository.hasAnsweredConsultation(
                consultationId = "consultationId",
                userId = "userId",
            )
        ).willReturn(false)

        val consultationResponses = listOf(mock(ReponseConsultationInserting::class.java))
        val insertParameters = mock(InsertParameters::class.java)
        given(
            insertConsultationResponseParametersMapper.toInsertParameters(
                consultationId = "consultationId",
                userId = "userId",
            )
        ).willReturn(insertParameters)

        given(
            insertReponseConsultationRepository.insertConsultationResponses(
                insertParameters = insertParameters,
                consultationResponses = consultationResponses,
            )
        ).willReturn(InsertResult.INSERT_SUCCESS)

        // When
        val result = useCase.insertReponseConsultation(
            consultationId = "consultationId",
            userId = "userId",
            consultationResponses = consultationResponses,
        )

        // Then
        assertThat(result).isEqualTo(InsertResult.INSERT_SUCCESS)
        then(consultationResponseRepository).should(only()).hasAnsweredConsultation(
            consultationId = "consultationId",
            userId = "userId",
        )
        then(consultationPreviewAnsweredRepository).should(only()).deleteConsultationAnsweredList(userId = "userId")
        then(insertReponseConsultationRepository).should(only()).insertConsultationResponses(
            insertParameters = insertParameters,
            consultationResponses = consultationResponses,
        )
    }

}