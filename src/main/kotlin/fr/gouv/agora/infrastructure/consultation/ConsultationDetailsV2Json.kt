package fr.gouv.agora.infrastructure.consultation

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.thematique.ThematiqueNoIdJson

@JsonIgnoreProperties(ignoreUnknown = true)
data class ConsultationDetailsV2Json(
    @JsonProperty("title")
    val title: String,
    @JsonProperty("coverUrl")
    val coverUrl: String,
    @JsonProperty("shareText")
    val shareText: String,
    @JsonProperty("thematique")
    val thematique: ThematiqueNoIdJson,
    @JsonProperty("questionsInfo")
    val questionsInfo: QuestionsInfo?,
    @JsonProperty("consultationDates")
    val consultationDates: ConsultationDates?,
    @JsonProperty("responsesInfo")
    val responsesInfo: ResponsesInfo?,
    @JsonProperty("infoHeader")
    val infoHeader: InfoHeader?,
    @JsonProperty("body")
    val body: Body,
    @JsonProperty("participationInfo")
    val participationInfo: ParticipationInfo?,
    @JsonProperty("downloadAnalysisUrl")
    val downloadAnalysisUrl: String?,
    // TODO feedbacks
    @JsonProperty("footer")
    val footer: Footer?,
    @JsonProperty("history")
    val history: List<History>?,
) {

    data class QuestionsInfo(
        @JsonProperty("endDate")
        val endDate: String,
        @JsonProperty("questionCount")
        val questionCount: String,
        @JsonProperty("estimatedTime")
        val estimatedTime: String,
        @JsonProperty("participantCount")
        val participantCount: Int,
        @JsonProperty("participantCountGoal")
        val participantCountGoal: Int,
    )

    data class ConsultationDates(
        @JsonProperty("startDate")
        val startDate: String,
        @JsonProperty("endDate")
        val endDate: String,
    )

    data class ResponsesInfo(
        @JsonProperty("picto")
        val picto: String,
        @JsonProperty("description")
        val description: String,
    )

    data class InfoHeader(
        @JsonProperty("picto")
        val picto: String,
        @JsonProperty("description")
        val description: String,
    )

    data class Body(
        @JsonProperty("sectionsPreview")
        val sectionsPreview: List<Section>,
        @JsonProperty("sections")
        val sections: List<Section>,
    )

    sealed class Section(
        @JsonProperty("type")
        val type: String
    ) {
        data class Title(
            @JsonProperty("title")
            val title: String,
        ) : Section(type = "title")

        data class RichText(
            @JsonProperty("description")
            val description: String,
        ) : Section(type = "richText")

        data class Image(
            @JsonProperty("url")
            val url: String,
            @JsonProperty("contentDescription")
            val contentDescription: String?
        ) : Section(type = "image")

        data class Video(
            @JsonProperty("url")
            val url: String,
            @JsonProperty("videoWidth")
            val width: Int,
            @JsonProperty("videoHeight")
            val height: Int,
            @JsonProperty("authorInfo")
            val authorInfo: AuthorInfo?,
            @JsonProperty("transcription")
            val transcription: String,
        ) : Section(type = "video") {
            data class AuthorInfo(
                @JsonProperty("name")
                val name: String,
                @JsonProperty("message")
                val message: String,
            )
        }

        data class FocusNumber(
            @JsonProperty("title")
            val title: String,
            @JsonProperty("description")
            val description: String,
        ) : Section(type = "focusNumber")

        data class Quote(
            @JsonProperty("description")
            val description: String,
        ) : Section(type = "quote")
    }

    data class ParticipationInfo(
        @JsonProperty("participantCount")
        val participantCount: Int,
        @JsonProperty("participantCountGoal")
        val participantCountGoal: Int,
    )

    data class Footer(
        @JsonProperty("title")
        val title: String?,
        @JsonProperty("description")
        val description: String,
    )

    data class History(
        @JsonProperty("updateId")
        val updateId: String?,
        @JsonProperty("type")
        val type: String,
        @JsonProperty("status")
        val status: String,
        @JsonProperty("title")
        val title: String,
        @JsonProperty("date")
        val date: String?,
        @JsonProperty("actionText")
        val actionText: String?,
    )
}