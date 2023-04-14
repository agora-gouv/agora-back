package fr.social.gouv.agora.domain

data class ConsultationResult(
    val consultation: Consultation,
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