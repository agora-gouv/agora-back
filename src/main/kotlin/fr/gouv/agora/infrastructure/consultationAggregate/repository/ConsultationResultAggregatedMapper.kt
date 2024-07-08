package fr.gouv.agora.infrastructure.consultationAggregate.repository

import fr.gouv.agora.domain.ConsultationResultAggregated
import fr.gouv.agora.domain.ConsultationResultAggregatedInserting
import fr.gouv.agora.infrastructure.consultationAggregate.dto.ConsultationResultAggregatedDTO
import org.springframework.stereotype.Component
import java.util.*

@Component
class ConsultationResultAggregatedMapper {

    fun toDto(domain: ConsultationResultAggregatedInserting): ConsultationResultAggregatedDTO? {
        return try {
            ConsultationResultAggregatedDTO(
                id = UUID.randomUUID(),
                consultationId = domain.consultationId,
                questionId = domain.questionId,
                choiceId = domain.choiceId,
                responseCount = domain.responseCount
            )
        } catch (e: IllegalArgumentException) {
            null
        }
    }
    
    fun toDomain(dto: ConsultationResultAggregatedDTO): ConsultationResultAggregated {
        return ConsultationResultAggregated(
            consultationId = dto.consultationId,
            questionId = dto.questionId,
            choiceId = dto.choiceId,
            responseCount = dto.responseCount
        )
    }
}
