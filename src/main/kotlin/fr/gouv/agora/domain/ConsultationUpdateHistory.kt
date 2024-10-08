package fr.gouv.agora.domain

import java.util.*

enum class ConsultationUpdateHistoryType {
    UPDATE, RESULTS
}

enum class ConsultationUpdateHistoryStatus {
    DONE, CURRENT, INCOMING
}

data class ConsultationUpdateHistory(
    val type: ConsultationUpdateHistoryType,
    val consultationUpdateId: String?,
    val status: ConsultationUpdateHistoryStatus,
    val title: String,
    val slug: String?,
    val updateDate: Date?,
    val actionText: String?,
)
