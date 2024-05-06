package fr.gouv.agora.domain

import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo

data class ConsultationResults(
    val consultation: ConsultationInfo,
    val participantCount: Int,
    val resultsWithChoices: List<QuestionResults>,
    val openQuestions: List<QuestionOpen>,
)

data class ConsultationResultsWithUpdate(
    val consultation: ConsultationInfo,
    val lastUpdate: ConsultationUpdate,
    val participantCount: Int,
    val results: List<QuestionResults>,
)

data class QuestionResults(
    val question: QuestionWithChoices,
    val responses: List<ChoiceResults>,
    val seenRatio: Double,
)

data class ChoiceResults(
    val choixPossible: ChoixPossible,
    val ratio: Double,
)