package fr.social.gouv.agora.infrastructure.reponseConsultation

import com.fasterxml.jackson.annotation.JsonProperty

data class ConsultationResultJson(
    @JsonProperty("title")
    val title: String,
    @JsonProperty("participantCount")
    val participantCount: Int,
    @JsonProperty("resultsUniqueChoice")
    val resultsUniqueChoice: List<QuestionResultJson>,
    @JsonProperty("resultsMultipleChoice")
    val resultsMultipleChoice: List<QuestionResultJson>,
    @JsonProperty("etEnsuite")
    val lastUpdate: ConsultationUpdatesJson,
)

data class QuestionResultJson(
    @JsonProperty("questionTitle")
    val questionTitle: String,
    @JsonProperty("order")
    val order: Int,
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