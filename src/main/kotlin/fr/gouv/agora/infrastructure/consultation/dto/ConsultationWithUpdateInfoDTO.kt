package fr.gouv.agora.infrastructure.consultation.dto

import org.springframework.data.rest.core.config.Projection
import java.util.*

@Projection(types = [ConsultationWithUpdateInfoDTO::class])
interface ConsultationWithUpdateInfoDTO {
    val id: UUID
    val title: String
    val coverUrl: String
    val thematiqueId: UUID
    val updateDate: Date
    val updateLabel: String?
}