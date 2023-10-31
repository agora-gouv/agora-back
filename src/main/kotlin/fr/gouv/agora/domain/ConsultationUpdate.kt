package fr.gouv.agora.domain

data class ConsultationUpdate(
    val consultationId: String,
    val status: ConsultationStatus,
    val description: String,
    val explanationsTitle: String?,
    val explanations: List<Explanation>?,
    val video: Video?,
    val conclusion: Conclusion?,
)

data class Video(
    val title: String,
    val intro: String,
    val url: String,
    val width: Int,
    val height: Int,
    val transcription: String,
)

data class Conclusion(
    val title: String,
    val description: String,
)