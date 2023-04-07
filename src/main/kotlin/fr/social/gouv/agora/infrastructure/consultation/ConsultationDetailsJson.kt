package fr.social.gouv.agora.infrastructure.consultation

import com.fasterxml.jackson.annotation.JsonProperty

data class ConsultationDetailsJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("thematique_id")
    val id_thematique: String,
    @JsonProperty("cover")
    val cover: String,
    @JsonProperty("end_date")
    val end_date: String,
    @JsonProperty("question_count")
    val question_count: String,
    @JsonProperty("estimated_time")
    val estimated_time: String,
    @JsonProperty("participant_count")
    val participant_count: Int,
    @JsonProperty("participant_count_goal")
    val participant_count_goal: Int,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("tips_description")
    val tips_description: String,
)