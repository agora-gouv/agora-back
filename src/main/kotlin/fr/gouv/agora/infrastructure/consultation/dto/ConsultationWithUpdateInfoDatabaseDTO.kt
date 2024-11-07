package fr.gouv.agora.infrastructure.consultation.dto

import java.util.*

interface ConsultationWithUpdateInfoDatabaseDTO {
    val id: UUID
    val slug: String
    val title: String
    val coverUrl: String
    val thematiqueId: String
    val endDate: Date
    val updateDate: Date
    val updateLabel: String?
}
