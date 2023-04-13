package fr.social.gouv.agora.domain

data class ConsultationResult(
    val consultation: Consultation,
    val lastUpdate: ConsultationUpdate,
    val participantCount: Int,
    val results: List<QuestionResults>,
)

data class QuestionResults(
    val question: Question,
    val responses: List<QuestionResult>,
)

data class QuestionResult(
    val choixPossible: ChoixPossible,
    val ratio: Double,
)