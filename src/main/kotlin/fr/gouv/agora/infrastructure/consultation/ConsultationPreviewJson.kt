package fr.gouv.agora.infrastructure.consultation

import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.thematique.ThematiqueNoIdJson

data class ConsultationPreviewJson(
    @JsonProperty("ongoing")
    val ongoingList: List<ConsultationOngoingJson>,
    @JsonProperty("finished")
    val finishedList: List<ConsultationFinishedJson>,
    @JsonProperty("answered")
    val answeredList: List<ConsultationFinishedJson>,
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
    @JsonProperty("updateLabel")
    val updateLabel: String?,
    @JsonProperty("updateDate")
    val updateDate: String,
)
