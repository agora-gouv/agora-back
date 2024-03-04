package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.domain.ConsultationPreviewOngoingInfo
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import org.springframework.stereotype.Component

@Component
class ConsultationPreviewOngoingInfoMapper {

    fun toDomain(dto: ConsultationDTO) = ConsultationPreviewOngoingInfo(
        id = dto.id.toString(),
        title = dto.title,
        coverUrl = dto.coverUrl,
        thematiqueId = dto.thematiqueId.toString(),
        startDate = dto.startDate,
        endDate = dto.endDate,
    )

}