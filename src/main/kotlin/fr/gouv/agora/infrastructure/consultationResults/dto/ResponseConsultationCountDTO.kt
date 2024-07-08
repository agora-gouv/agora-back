package fr.gouv.agora.infrastructure.consultationResults.dto

interface ResponseConsultationCountDTO {
    val questionId: String
    val choiceId: String
    val responseCount: Int
}
