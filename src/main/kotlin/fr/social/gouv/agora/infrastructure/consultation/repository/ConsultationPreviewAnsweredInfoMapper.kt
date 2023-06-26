package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.domain.ConsultationPreviewAnsweredInfo
import fr.social.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import org.springframework.stereotype.Component

@Component
class ConsultationPreviewAnsweredInfoMapper {
    fun toDomain(dto: ConsultationDTO) = ConsultationPreviewAnsweredInfo(
        id = dto.id.toString(),
        title = dto.title,
        coverUrl = dto.coverUrl,
        thematiqueId = dto.thematiqueId.toString(),
    )
}