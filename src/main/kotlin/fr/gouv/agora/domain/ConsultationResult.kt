package fr.gouv.agora.domain

import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo

data class ConsultationResult(
    val consultation: ConsultationInfo,
    val lastUpdate: ConsultationUpdate,
    val participantCount: Int,
    val results: List<QuestionResult>,
)

data class QuestionResult(
    val question: Question,
    val responses: List<ChoiceResult>,
)

data class ChoiceResult(
    val choixPossible: ChoixPossible,
    val ratio: Double,
)