package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfo
import org.springframework.stereotype.Component

@Component
class ConsultationInfoMapper {

    fun toDomain(dto: ConsultationDTO) = ConsultationInfo(
        id = dto.id.toString(),
        title = dto.title,
        coverUrl = dto.coverUrl,
        startDate = dto.startDate,
        endDate = dto.endDate,
        questionCount = dto.questionCount,
        estimatedTime = dto.estimatedTime,
        participantCountGoal = dto.participantCountGoal,
        description = dto.description,
        tipsDescription = dto.tipsDescription,
        thematiqueId = dto.thematiqueId.toString(),
    )

}