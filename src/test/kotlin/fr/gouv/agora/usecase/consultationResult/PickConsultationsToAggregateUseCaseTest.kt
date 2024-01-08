package fr.gouv.agora.usecase.consultationResult

import fr.gouv.agora.TestUtils
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.reponseConsultation.repository.ConsultationResultAggregatedRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDate
import java.time.Month

@ExtendWith(SpringExtension::class)
@SpringBootTest
class PickConsultationsToAggregateUseCaseTest {

    private lateinit var useCase: PickConsultationsToAggregateUseCase

    @MockBean
    private lateinit var consultationInfoRepository: ConsultationInfoRepository

    @MockBean
    private lateinit var consultationAggregatedRepository: ConsultationResultAggregatedRepository

    @MockBean
    private lateinit var aggregateConsultationResultUseCase: AggregateConsultationResultUseCase

    @Test
    fun `aggregateConsultations - when no consultations - should do nothing`() {
        // Given
        mockCurrentDate()
        given(consultationInfoRepository.getConsultations()).willReturn(emptyList())

        // When
        useCase.aggregateConsultations()

        // Then
        then(consultationInfoRepository).should(only()).getConsultations()
        then(consultationAggregatedRepository).shouldHaveNoInteractions()
        then(aggregateConsultationResultUseCase).shouldHaveNoInteractions()
    }

    @Test
    fun `aggregateConsultations - when has consultations that aren't finished or finished but for less than 15 days - should do nothing`() {
        // Given
        mockCurrentDate(currentDate = LocalDate.of(1998, Month.JULY, 15))
        val consultations = listOf(
            mockConsultation(id = "consultationId1", endDate = LocalDate.of(1998, Month.SEPTEMBER, 21)),
            mockConsultation(id = "consultationId2", endDate = LocalDate.of(1998, Month.JULY, 16)),
        )
        given(consultationInfoRepository.getConsultations()).willReturn(consultations)

        // When
        useCase.aggregateConsultations()

        // Then
        then(consultationInfoRepository).should(only()).getConsultations()
        then(consultationAggregatedRepository).shouldHaveNoInteractions()
        then(aggregateConsultationResultUseCase).shouldHaveNoInteractions()
    }

    @Test
    fun `aggregateConsultations - when has consultations that are finished for more than 15 days and not already aggregated - should aggregate them`() {
        // Given
        mockCurrentDate(currentDate = LocalDate.of(1998, Month.JULY, 15))
        val consultations = listOf(
            mockConsultation(id = "consultationId0", endDate = LocalDate.of(1998, Month.JULY, 2)),
            mockConsultation(id = "consultationId1", endDate = LocalDate.of(1998, Month.JULY, 1)),
            mockConsultation(id = "consultationId2", endDate = LocalDate.of(1998, Month.JUNE, 30)),
        )
        given(consultationInfoRepository.getConsultations()).willReturn(consultations)

        // When
        useCase.aggregateConsultations()

        // Then
        then(consultationInfoRepository).should(only()).getConsultations()
        then(consultationAggregatedRepository).should(only()).getAggregatedResultsConsultationIds()
        then(aggregateConsultationResultUseCase).should(only()).aggregateConsultationResults("consultationId2")
    }

    @Test
    fun `aggregateConsultations - when has consultations that are finished for more than 15 days but already aggregated - should do nothing`() {
        // Given
        mockCurrentDate(currentDate = LocalDate.of(1998, Month.JULY, 15))
        val consultations = listOf(
            mockConsultation(id = "consultationId2", endDate = LocalDate.of(1998, Month.JUNE, 30)),
        )
        given(consultationInfoRepository.getConsultations()).willReturn(consultations)
        given(consultationAggregatedRepository.getAggregatedResultsConsultationIds())
            .willReturn(listOf("consultationId2"))

        // When
        useCase.aggregateConsultations()

        // Then
        then(consultationInfoRepository).should(only()).getConsultations()
        then(consultationAggregatedRepository).should(only()).getAggregatedResultsConsultationIds()
        then(aggregateConsultationResultUseCase).shouldHaveNoInteractions()
    }

    private fun mockCurrentDate(currentDate: LocalDate = LocalDate.MIN) {
        useCase = PickConsultationsToAggregateUseCase(
            clock = TestUtils.getFixedClock(currentDate.atStartOfDay()),
            consultationInfoRepository = consultationInfoRepository,
            consultationAggregatedRepository = consultationAggregatedRepository,
            aggregateConsultationResultUseCase = aggregateConsultationResultUseCase,
        )
    }

    private fun mockConsultation(id: String, endDate: LocalDate): ConsultationInfo {
        return mock(ConsultationInfo::class.java).also {
            given(it.id).willReturn(id)
            given(it.endDate).willReturn(endDate.toDate())
        }
    }

}