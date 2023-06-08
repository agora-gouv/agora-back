package fr.social.gouv.agora.infrastructure.reponseConsultation

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ReponsesConsultationJson(
    @JsonProperty("consultationId")
    val consultationId: String,
    @JsonProperty("responses")
    val responses: List<ReponseConsultationJson>,
)

data class ReponseConsultationJson(
    @JsonProperty("questionId")
    val questionId: String,
    @JsonProperty("choiceIds")
    val choiceIds: List<String>?,
    @JsonProperty("responseText")
    val responseText: String?,
)

data class ResponseConsultationResultJson(
    @JsonProperty("askDemographicInfo")
    val askDemographicInfo: Boolean,
)