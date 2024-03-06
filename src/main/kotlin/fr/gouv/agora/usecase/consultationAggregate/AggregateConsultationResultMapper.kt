package fr.gouv.agora.usecase.consultationAggregate

import fr.gouv.agora.domain.ConsultationResultAggregatedInserting
import fr.gouv.agora.domain.ResponseConsultationCount
import org.springframework.stereotype.Component

@Component
class AggregateConsultationResultMapper {
    fun toInserting(
        consultationId: String,
        consultationResponses: List<ResponseConsultationCount>,
    ): List<ConsultationResultAggregatedInserting> {
        return consultationResponses.map { consultationResponseCount ->
            ConsultationResultAggregatedInserting(
                consultationId = consultationId,
                questionId = consultationResponseCount.questionId,
                choiceId = consultationResponseCount.choiceId,
                responseCount = consultationResponseCount.responseCount,
            )
        }
    }
}