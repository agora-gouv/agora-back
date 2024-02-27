package fr.gouv.agora.infrastructure.consultationUpdates.dto

import org.springframework.data.rest.core.config.Projection
import java.util.*

@Projection(types = [ConsultationUpdateHistoryWithDateDTO::class])
interface ConsultationUpdateHistoryWithDateDTO {
    val stepNumber: Int
    val type: String
    val consultationUpdateId: UUID?
    val title: String
    val updateDate: Date?
    val actionText: String?
}

