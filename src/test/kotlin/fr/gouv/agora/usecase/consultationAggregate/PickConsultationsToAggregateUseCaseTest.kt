package fr.gouv.agora.usecase.consultationAggregate

import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultationAggregate.repository.ConsultationResultAggregatedRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class PickConsultationsToAggregateUseCaseTest {

    @InjectMocks
    private lateinit var useCase: PickConsultationsToAggregateUseCase

    @Mock
    private lateinit var consultationInfoRepository: ConsultationInfoRepository

    @Mock
    private lateinit var consultationAggregatedRepository: ConsultationResultAggregatedRepository

    @Mock
    private lateinit var aggregateConsultationResultUseCase: AggregateConsultationResultUseCase

    @Test
    fun `aggregateConsultations - when no consultations - should do nothing`() {
        // Given
        given(consultationInfoRepository.getConsultationsToAggregate()).willReturn(emptyList())

        // When
        useCase.aggregateConsultations()

        // Then
        then(consultationInfoRepository).should(only()).getConsultationsToAggregate()
        then(consultationAggregatedRepository).shouldHaveNoInteractions()
        then(aggregateConsultationResultUseCase).shouldHaveNoInteractions()
    }

    @Test
    fun `aggregateConsultations - when has consultations that are not already aggregated - should aggregate them`() {
        // Given
        val consultations = listOf(
            mock(ConsultationInfo::class.java).also {
                given(it.id).willReturn("consultationId")
            }
        )
        given(consultationInfoRepository.getConsultationsToAggregate()).willReturn(consultations)

        // When
        useCase.aggregateConsultations()

        // Then
        then(consultationInfoRepository).should(only()).getConsultationsToAggregate()
        then(consultationAggregatedRepository).should(only()).getAggregatedResultsConsultationIds()
        then(aggregateConsultationResultUseCase).should(only()).aggregateConsultationResults("consultationId")
    }

    @Test
    fun `aggregateConsultations - when has consultations that already aggregated - should do nothing`() {
        // Given
        val consultations = listOf(
            mock(ConsultationInfo::class.java).also {
                given(it.id).willReturn("consultationId")
            }
        )
        given(consultationInfoRepository.getConsultationsToAggregate()).willReturn(consultations)
        given(consultationAggregatedRepository.getAggregatedResultsConsultationIds())
            .willReturn(listOf("consultationId"))

        // When
        useCase.aggregateConsultations()

        // Then
        then(consultationInfoRepository).should(only()).getConsultationsToAggregate()
        then(consultationAggregatedRepository).should(only()).getAggregatedResultsConsultationIds()
        then(aggregateConsultationResultUseCase).shouldHaveNoInteractions()
    }

}