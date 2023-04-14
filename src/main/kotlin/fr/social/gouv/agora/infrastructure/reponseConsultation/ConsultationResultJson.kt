package fr.social.gouv.agora.infrastructure.reponseConsultation

import com.fasterxml.jackson.annotation.JsonProperty

data class ConsultationResultJson(
    @JsonProperty("title")
    val title: String,
    @JsonProperty("participantCount")
    val participantCount: Int,
    @JsonProperty("results")
    val results: List<QuestionResultJson>,
    @JsonProperty("etEnsuite")
    val lastUpdate: ConsultationUpdatesJson,
)

data class QuestionResultJson(
    @JsonProperty("questionTitle")
    val questionTitle: String,
    @JsonProperty("responses")
    val responses: List<ChoiceResultJson>,
)

data class ChoiceResultJson(
    @JsonProperty("label")
    val label: String,
    @JsonProperty("ratio")
    val ratio: Int,
)

data class ConsultationUpdatesJson(
    @JsonProperty("step")
    val step: Int,
    @JsonProperty("description")
    val description: String,
)