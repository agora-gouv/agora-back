package fr.gouv.agora.infrastructure.consultationResults.dto

import java.util.UUID

interface ResponseConsultationCountDTO {
    val questionId: UUID
    val choiceId: UUID
    val responseCount: Int
}
