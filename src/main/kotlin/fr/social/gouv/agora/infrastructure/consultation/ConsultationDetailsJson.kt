package fr.social.gouv.agora.infrastructure.consultation

import com.fasterxml.jackson.annotation.JsonProperty

data class ConsultationDetailsJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("thematiqueId")
    val thematiqueId: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("cover")
    val cover: String,
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
    @JsonProperty("description")
    val description: String,
    @JsonProperty("tipsDescription")
    val tipsDescription: String,
)