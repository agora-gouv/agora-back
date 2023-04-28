package fr.social.gouv.agora.infrastructure.consultation

import com.fasterxml.jackson.annotation.JsonProperty
import fr.social.gouv.agora.infrastructure.thematique.ThematiqueJson

data class ConsultationPreviewJson (
    @JsonProperty("ongoing")
    val ongoingList: List<ConsultationOngoingJson>,
    @JsonProperty("finished")
    val finishedList: List<ConsultationOngoingJson>, //TODO à remplacer par ConsultationFinishedJson
    @JsonProperty("answered")
    val answeredList: List<ConsultationOngoingJson>, //TODO à remplacer par ConsultationAnsweredJson
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
    val thematique: ThematiqueJson?,
    @JsonProperty("hasAnswered")
    val hasAnswered: Boolean,
)