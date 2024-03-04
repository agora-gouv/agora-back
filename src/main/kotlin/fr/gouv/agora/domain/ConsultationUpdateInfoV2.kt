package fr.gouv.agora.domain

import java.util.*

data class ConsultationUpdateInfoV2(
    val id: String,
    val updateDate: Date,
    val shareTextTemplate: String,
    val hasQuestionsInfo: Boolean,
    val hasParticipationInfo: Boolean,
    val responsesInfo: ResponsesInfo?,
    val infoHeader: InfoHeader?,
    val body: List<Section>,
    val bodyPreview: List<Section>,
    val downloadAnalysisUrl: String?,
    val footer: Footer?,
) {

    data class ResponsesInfo(
        val picto: String,
        val description: String,
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
                val date: Date,
            )
        }

        data class FocusNumber(val title: String, val description: String) : Section()
        data class Quote(val description: String) : Section()

        // TODO Accordion
        //data class Accordion(val title: String, val description: String) : Section()
    }

    // TODO Feedback Question & results
//    sealed class Feedback {
//        abstract val title: String
//        abstract val picto: String
//        abstract val description: String
//
//        data class Question(
//            override val title: String,
//            override val picto: String,
//            override val description: String,
//        ): Feedback()
//
//        data class ResultsWithoutInfo(
//            override val title: String,
//            override val picto: String,
//            override val description: String,
//        ): Feedback()
//    }

    data class Footer(val title: String?, val description: String)
}
