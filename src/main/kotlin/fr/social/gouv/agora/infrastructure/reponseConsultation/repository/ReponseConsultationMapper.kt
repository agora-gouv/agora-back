package fr.social.gouv.agora.infrastructure.reponseConsultation.repository

import fr.social.gouv.agora.domain.ReponseConsultation
import fr.social.gouv.agora.infrastructure.reponseConsultation.dto.ReponseConsultationDTO
import org.springframework.stereotype.Component
import java.util.*

@Component
class ReponseConsultationMapper {

    fun toDto(domain: ReponseConsultation): List<ReponseConsultationDTO> {
        if (domain.choiceIds.isNullOrEmpty()) {
            return listOf(
                ReponseConsultationDTO(
                    id = UUID.fromString(domain.id),
                    consultationId = UUID.fromString(domain.consultationId),
                    questionId = UUID.fromString(domain.questionId),
                    choiceId = null,
                    responseText = domain.responseText,
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
                )
            }
        }
    }
}