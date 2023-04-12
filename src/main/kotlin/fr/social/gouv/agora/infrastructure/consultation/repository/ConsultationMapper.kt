package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.domain.Consultation
import fr.social.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import org.springframework.stereotype.Component

@Component
class ConsultationMapper {

    fun toDomain(dto: ConsultationDTO) = Consultation(
        id = dto.id.toString(),
        title = dto.title,
        abstract = dto.abstract,
        startDate = dto.startDate,
        endDate = dto.endDate,
        coverUrl = dto.coverUrl,
        questionCount = dto.questionCount,
        estimatedTime = dto.estimatedTime,
        participantCountGoal = dto.participantCountGoal,
        description = dto.description,
        tipsDescription = dto.tipsDescription,
        thematiqueId = dto.thematiqueId.toString(),
    )

}