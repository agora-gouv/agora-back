package fr.gouv.agora.domain

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

data class ConsultationUpdateInfoV2(
    val id: String,
    val updateDate: LocalDateTime,
    val shareTextTemplate: String,
    val hasQuestionsInfo: Boolean,
    val hasParticipationInfo: Boolean,
    val responsesInfo: ResponsesInfo?,
    val infoHeader: InfoHeader?,
    val sectionsHeader: List<Section>,
    val body: List<Section>,
    val bodyPreview: List<Section>,
    val downloadAnalysisUrl: String?,
    val feedbackQuestion: FeedbackQuestion?,
    val footer: Footer?,
    val goals: List<Goal>?,
) {

    data class ResponsesInfo(
        val picto: String,
        val description: String,
        val actionText: String,
    )

    data class InfoHeader(
        val picto: String,
        val description: String,
    )

    sealed class Section {
        data class Title(val title: String) : Section()
        data class RichText(val description: String) : Section()
        data class Image(val url: String, val contentDescription: String?) : Section()
        data class Video(
            val url: String,
            val width: Int,
            val height: Int,
            val authorInfo: AuthorInfo?,
            val transcription: String,
        ) : Section() {
            data class AuthorInfo(
                val name: String,
                val message: String,
                val date: LocalDate,
            )
        }

        data class FocusNumber(val title: String, val description: String) : Section()
        data class Quote(val description: String) : Section()

        data class Accordion(val title: String, val sections: List<Section>) : Section()
    }

    data class FeedbackQuestion(
        val consultationUpdateId: String,
        val title: String,
        val picto: String,
        val description: String,
    )

    data class Footer(val title: String?, val description: String)

    data class Goal(val picto: String, val description: String)
}
