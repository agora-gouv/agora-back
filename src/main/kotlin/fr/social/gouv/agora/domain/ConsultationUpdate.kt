package fr.social.gouv.agora.domain

data class ConsultationUpdate(
    val status: ConsultationStatus,
    val description: String,
    val explanationsTitle: String?,
    val explanations: List<Explanation>,
    val videoTitle: String?,
    val videoIntro: String?,
    val videoUrl: String?,
    val videoWidth: String?,
    val videoHeight: String?,
    val videoTranscription: String?,
    val conclusionTitle: String?,
    val conclusionDescription: String?,
)