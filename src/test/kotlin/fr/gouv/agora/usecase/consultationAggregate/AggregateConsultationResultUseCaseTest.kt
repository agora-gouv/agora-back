package fr.gouv.agora.usecase.consultationAggregate

import fr.gouv.agora.domain.ConsultationResultAggregatedInserting
import fr.gouv.agora.domain.ResponseConsultationCount
import fr.gouv.agora.usecase.consultationAggregate.repository.ConsultationResultAggregatedRepository
import fr.gouv.agora.usecase.consultationResponse.repository.GetConsultationResponseRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class AggregateConsultationResultUseCaseTest {

    @InjectMocks
    private lateinit var useCase: AggregateConsultationResultUseCase

    @Mock
    private lateinit var consultationResponseRepository: GetConsultationResponseRepository

    @Mock
    private lateinit var consultationResultAggregatedRepository: ConsultationResultAggregatedRepository

    @Mock
    private lateinit var mapper: AggregateConsultationResultMapper

    @Test
    fun `aggregateConsultationResults - when consultationResponsesCount is empty - should do nothing`() {
        // Given
        given(consultationResponseRepository.getConsultationResponsesCount(consultationId = "consultationId")).willReturn(
            emptyList()
        )

        // When
        useCase.aggregateConsultationResults(consultationId = "consultationId")

        // Then
        then(consultationResponseRepository).should(only())
            .getConsultationResponsesCount(consultationId = "consultationId")
        then(consultationResultAggregatedRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `aggregateConsultationResults - when consultationResponsesCount is not empty - should insert results to resultAggregatedRepository and delete responses`() {
        // Given
        val consultationResponses = listOf(mock(ResponseConsultationCount::class.java))
        given(consultationResponseRepository.getConsultationResponsesCount(consultationId = "consultationId")).willReturn(
            consultationResponses
        )

        val consultationResultAggregated = listOf(mock(ConsultationResultAggregatedInserting::class.java))
        given(
            mapper.toInserting(
                consultationId = "consultationId",
                consultationResponses = consultationResponses,
            )
        ).willReturn(consultationResultAggregated)

        // When
        useCase.aggregateConsultationResults(consultationId = "consultationId")

        // Then
        inOrder(consultationResponseRepository, consultationResultAggregatedRepository).also {
            then(consultationResponseRepository).should(it)
                .getConsultationResponsesCount(consultationId = "consultationId")
            then(consultationResultAggregatedRepository).should(it)
                .insertConsultationResultAggregated(consultationResultAggregated)
            then(consultationResponseRepository).should(it)
                .deleteConsultationResponses(consultationId = "consultationId")
        }
        then(mapper).should(only())
            .toInserting(consultationId = "consultationId", consultationResponses = consultationResponses)
    }

}