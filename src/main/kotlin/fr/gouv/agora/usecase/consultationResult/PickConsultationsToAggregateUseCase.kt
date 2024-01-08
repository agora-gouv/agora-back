package fr.gouv.agora.usecase.consultationResult

import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDate
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.reponseConsultation.repository.ConsultationResultAggregatedRepository
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDate

@Component
class PickConsultationsToAggregateUseCase(
    private val clock: Clock,
    private val consultationInfoRepository: ConsultationInfoRepository,
    private val consultationAggregatedRepository: ConsultationResultAggregatedRepository,
    private val aggregateConsultationResultUseCase: AggregateConsultationResultUseCase,
) {

    companion object {
        private const val MINIMUM_DAYS_AFTER_CONSULTATION_END_TO_AGGREGATE = 14L
    }

    fun aggregateConsultations() {
        val currentDate = LocalDate.now(clock)
        consultationInfoRepository.getConsultations()
            .filter { consultationInfo ->
                currentDate.isAfter(
                    consultationInfo
                        .endDate
                        .toLocalDate()
                        .plusDays(MINIMUM_DAYS_AFTER_CONSULTATION_END_TO_AGGREGATE)
                )
            }
            .takeIf { it.isNotEmpty() }
            ?.let { consultationInfoList ->
                val aggregatedConsultationIds = consultationAggregatedRepository.getAggregatedResultsConsultationIds()
                consultationInfoList
                    .filter { consultationInfo ->
                        !aggregatedConsultationIds.contains(consultationInfo.id)
                    }
                    .forEach { consultationsToAggregate ->
                        aggregateConsultationResultUseCase.aggregateConsultationResults(consultationsToAggregate.id)
                    }
            }
    }

}