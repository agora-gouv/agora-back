package fr.gouv.agora.usecase.consultationAggregate

import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultationAggregate.repository.ConsultationResultAggregatedRepository
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
                        println("🧮 Aggregate results for consultation: ${consultationsToAggregate.title}")
                        aggregateConsultationResultUseCase.aggregateConsultationResults(consultationsToAggregate.id)
                    }
            }
    }

}