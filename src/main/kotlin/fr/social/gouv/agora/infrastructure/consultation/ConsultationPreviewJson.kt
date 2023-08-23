package fr.social.gouv.agora.infrastructure.consultation

import com.fasterxml.jackson.annotation.JsonProperty
import fr.social.gouv.agora.infrastructure.thematique.ThematiqueNoIdJson

data class ConsultationPreviewJson(
    @JsonProperty("ongoing")
    val ongoingList: List<ConsultationOngoingJson>,
    @JsonProperty("finished")
    val finishedList: List<ConsultationFinishedJson>,
    @JsonProperty("answered")
    val answeredList: List<ConsultationAnsweredJson>,
)

data class ConsultationOngoingJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("coverUrl")
    val coverUrl: String,
    @JsonProperty("endDate")
    val endDate: String,
    @JsonProperty("thematique")
    val thematique: ThematiqueNoIdJson,
    @JsonProperty("highlightLabel")
    val highlightLabel: String?,
//    @JsonProperty("hasAnswered")
//    val hasAnswered: Boolean,
)

data class ConsultationFinishedJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("coverUrl")
    val coverUrl: String,
    @JsonProperty("thematique")
    val thematique: ThematiqueNoIdJson,
    @JsonProperty("step")
    val step: Int,
)

data class ConsultationAnsweredJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("coverUrl")
    val coverUrl: String,
    @JsonProperty("thematique")
    val thematique: ThematiqueNoIdJson,
    @JsonProperty("step")
    val step: Int,
)