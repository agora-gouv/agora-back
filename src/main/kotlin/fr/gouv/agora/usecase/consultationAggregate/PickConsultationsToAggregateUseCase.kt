package fr.gouv.agora.usecase.consultationAggregate

import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultationAggregate.repository.ConsultationResultAggregatedRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class PickConsultationsToAggregateUseCase(
    private val consultationInfoRepository: ConsultationInfoRepository,
    private val consultationAggregatedRepository: ConsultationResultAggregatedRepository,
    private val aggregateConsultationResultUseCase: AggregateConsultationResultUseCase,
) {
    private val logger: Logger = LoggerFactory.getLogger(PickConsultationsToAggregateUseCase::class.java)

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
                        logger.info("🧮 Aggregate results for consultation: ${consultationsToAggregate.title}")
                        aggregateConsultationResultUseCase.aggregateConsultationResults(consultationsToAggregate.id)
                    }
            }
    }
}
