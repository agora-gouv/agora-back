package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.domain.ConsultationPreviewFinishedInfo
import fr.social.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import org.springframework.stereotype.Component

@Component
class ConsultationPreviewFinishedInfoMapper {
    fun toDomain(dto: ConsultationDTO) = ConsultationPreviewFinishedInfo(
        id = dto.id.toString(),
        title = dto.title,
        coverUrl = dto.coverUrl,
        thematiqueId = dto.thematiqueId.toString(),
    )
}