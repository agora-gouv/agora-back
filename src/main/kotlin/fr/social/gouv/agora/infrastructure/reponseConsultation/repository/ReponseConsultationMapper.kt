package fr.social.gouv.agora.infrastructure.reponseConsultation.repository

import fr.social.gouv.agora.domain.ReponseConsultation
import fr.social.gouv.agora.domain.ReponseConsultationInserting
import fr.social.gouv.agora.infrastructure.reponseConsultation.dto.ReponseConsultationDTO
import org.springframework.stereotype.Component
import java.util.*

@Component
class ReponseConsultationMapper {

    fun toDomain(dto: ReponseConsultationDTO): ReponseConsultation {
        return ReponseConsultation(
            id = dto.id.toString(),
            questionId = dto.questionId.toString(),
            choiceId = dto.choiceId.toString(),
            participationId = dto.participationId.toString(),
        )
    }

    fun toDto(domain: ReponseConsultationInserting): List<ReponseConsultationDTO> {
        if (domain.choiceIds.isNullOrEmpty()) {
            return listOf(
                ReponseConsultationDTO(
                    id = UUID.fromString(domain.id),
                    consultationId = UUID.fromString(domain.consultationId),
                    questionId = UUID.fromString(domain.questionId),
                    choiceId = null,
                    responseText = domain.responseText,
                    participationId = UUID.fromString(domain.participationId)
                )
            )
        } else {
            return domain.choiceIds.map { choiceId ->
                ReponseConsultationDTO(
                    id = UUID.fromString(domain.id),
                    consultationId = UUID.fromString(domain.consultationId),
                    questionId = UUID.fromString(domain.questionId),
                    choiceId = UUID.fromString(choiceId),
                    responseText = domain.responseText,
                    participationId = UUID.fromString(domain.participationId)
                )
            }
        }
    }
}