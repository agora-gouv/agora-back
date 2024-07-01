package fr.gouv.agora.infrastructure.consultationUpdates.dto

import java.util.*

interface ConsultationUpdateHistoryWithDateDTO {
    val stepNumber: Int
    val type: String
    val consultationUpdateId: UUID?
    val title: String
    val updateDate: Date?
    val actionText: String?
}

