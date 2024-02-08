package fr.gouv.agora.usecase.consultationResult

import fr.gouv.agora.usecase.reponseConsultation.repository.ConsultationResultAggregatedRepository
import fr.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
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