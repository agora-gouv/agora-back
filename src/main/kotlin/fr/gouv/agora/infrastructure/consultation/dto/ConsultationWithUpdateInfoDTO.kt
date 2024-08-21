package fr.gouv.agora.infrastructure.consultation.dto

import java.util.*

interface ConsultationWithUpdateInfoDTO {
    val id: UUID
    val slug: String
    val title: String
    val coverUrl: String
    val thematiqueId: UUID
    val endDate: Date
    val updateDate: Date
    val updateLabel: String?
}
