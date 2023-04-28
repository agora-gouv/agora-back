package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.domain.ConsultationPreviewOngoingInfo
import fr.social.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import org.springframework.stereotype.Component

@Component
class ConsultationPreviewOngoingMapper {
    fun toDomain(dto: ConsultationDTO) = ConsultationPreviewOngoingInfo(
        id = dto.id.toString(),
        title = dto.title,
        coverUrl = dto.coverUrl,
        endDate = dto.endDate,
        thematiqueId = dto.thematiqueId.toString(),
        hasAnswered = false, //TODO Feat 99
    )
}