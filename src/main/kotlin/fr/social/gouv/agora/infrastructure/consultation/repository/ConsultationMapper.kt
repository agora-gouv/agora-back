package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.domain.Consultation
import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfo
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ConsultationMapper {

    fun toDomain(dto: ConsultationDTO) = ConsultationInfo(
        id = dto.id.toString(),
        title = dto.title,
        coverUrl = dto.coverUrl,
        abstract = dto.abstract,
        startDate = dto.startDate,
        endDate = dto.endDate,
        questionCount = dto.questionCount,
        estimatedTime = dto.estimatedTime,
        participantCountGoal = dto.participantCountGoal,
        description = dto.description,
        tipsDescription = dto.tipsDescription,
        thematiqueId =  dto.thematiqueId.toString(),
    )

}