package fr.social.gouv.agora.infrastructure.reponseConsultation.repository

import fr.social.gouv.agora.domain.ReponseConsultation
import fr.social.gouv.agora.infrastructure.reponseConsultation.dto.ReponseConsultationDTO
import org.springframework.stereotype.Component

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

}