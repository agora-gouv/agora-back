package fr.gouv.agora.infrastructure.consultationResult.repository

import fr.gouv.agora.domain.ConsultationResultAggregated
import fr.gouv.agora.domain.ConsultationResultAggregatedInserting
import fr.gouv.agora.infrastructure.consultationResult.dto.ConsultationResultAggregatedDTO
import org.springframework.stereotype.Component
import java.util.*

@Component
class ConsultationResultAggregatedMapper {

    fun toDto(domain: ConsultationResultAggregatedInserting): ConsultationResultAggregatedDTO? {
        return try {
            ConsultationResultAggregatedDTO(
                id = UUID.randomUUID(),
                consultationId = UUID.fromString(domain.consultationId),
                questionId = UUID.fromString(domain.questionId),
                choiceId = UUID.fromString(domain.choiceId),
                responseCount = domain.responseCount
            )
        } catch (e: IllegalArgumentException) {
            null
        }
    }
    
    fun toDomain(dto: ConsultationResultAggregatedDTO): ConsultationResultAggregated {
        return ConsultationResultAggregated(
            consultationId = dto.consultationId.toString(),
            questionId = dto.questionId.toString(),
            choiceId = dto.choiceId.toString(),
            responseCount = dto.responseCount
        )
    }
}
