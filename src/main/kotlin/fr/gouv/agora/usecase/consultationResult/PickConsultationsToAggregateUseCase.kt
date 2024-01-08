package fr.gouv.agora.usecase.consultationResult

import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.reponseConsultation.repository.ConsultationResultAggregatedRepository
import org.springframework.stereotype.Component

@Component
class PickConsultationsToAggregateUseCase(
    private val consultationInfoRepository: ConsultationInfoRepository,
    private val consultationAggregatedRepository: ConsultationResultAggregatedRepository,
    private val aggregateConsultationResultUseCase: AggregateConsultationResultUseCase,
) {

    fun aggregateConsultations() {
        consultationInfoRepository.getConsultationsToAggregate()
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