package fr.gouv.agora.usecase.consultationAggregate

import fr.gouv.agora.usecase.consultationAggregate.repository.ConsultationResultAggregatedRepository
import fr.gouv.agora.usecase.consultationResponse.repository.GetConsultationResponseRepository
import org.springframework.stereotype.Component

@Component
class AggregateConsultationResultUseCase(
    private val consultationResponseRepository: GetConsultationResponseRepository,
    private val consultationResultAggregatedRepository: ConsultationResultAggregatedRepository,
    private val mapper: AggregateConsultationResultMapper,
) {
    fun aggregateConsultationResults(consultationId: String) {
        consultationResponseRepository.getConsultationResponsesCount(consultationId = consultationId)
            .takeIf { it.isNotEmpty() }
            ?.let { consultationResponses ->
                consultationResultAggregatedRepository.insertConsultationResultAggregated(
                    consultationResults = mapper.toInserting(
                        consultationId = consultationId,
                        consultationResponses = consultationResponses,
                    )
                )
                consultationResponseRepository.deleteConsultationResponses(consultationId = consultationId)
            }
    }
}