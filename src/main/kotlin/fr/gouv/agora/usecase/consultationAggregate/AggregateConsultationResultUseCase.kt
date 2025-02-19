package fr.gouv.agora.usecase.consultationAggregate

import fr.gouv.agora.usecase.consultationAggregate.repository.ConsultationResultAggregatedRepository
import fr.gouv.agora.usecase.consultationResponse.repository.GetConsultationResponseRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AggregateConsultationResultUseCase(
    private val consultationResponseRepository: GetConsultationResponseRepository,
    private val consultationResultAggregatedRepository: ConsultationResultAggregatedRepository,
    private val mapper: AggregateConsultationResultMapper,
) {
    private val logger: Logger = LoggerFactory.getLogger(AggregateConsultationResultUseCase::class.java)

    fun aggregateConsultationResults(consultationId: String) {
        consultationResponseRepository.getConsultationResponsesCount(consultationId = consultationId)
            .also { logger.info("🧮 Aggregate results for consultation : ${it.size} réponses trouvées pour la consultation $consultationId") }
            .takeIf { it.isNotEmpty() }
            ?.let { consultationResponses ->
                consultationResultAggregatedRepository.insertConsultationResultAggregated(
                    consultationResults = mapper.toInserting(
                        consultationId = consultationId,
                        consultationResponses = consultationResponses,
                    )
                )
                logger.info("🧮 Aggregate results for consultation : suppression des réponses pour la consultation $consultationId")
                consultationResponseRepository.deleteConsultationResponses(consultationId = consultationId)
            }
    }
}
