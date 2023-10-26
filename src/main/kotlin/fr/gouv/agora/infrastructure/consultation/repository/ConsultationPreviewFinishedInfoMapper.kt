package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.domain.ConsultationPreviewFinishedInfo
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import org.springframework.stereotype.Component

@Component
class ConsultationPreviewFinishedInfoMapper {
    fun toDomain(dto: ConsultationDTO) = ConsultationPreviewFinishedInfo(
        id = dto.id.toString(),
        title = dto.title,
        coverUrl = dto.coverUrl,
        thematiqueId = dto.thematiqueId.toString(),
        startDate = dto.startDate,
        endDate = dto.endDate,
    )
}