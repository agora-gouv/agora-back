package fr.gouv.agora.domain

data class ConsultationResultAggregated(
    val consultationId: String,
    val questionId: String,
    val choiceId: String,
    val responseCount: Int,
)

data class ConsultationResultAggregatedInserting(
    val consultationId: String,
    val questionId: String,
    val choiceId: String,
    val responseCount: Int,
)
