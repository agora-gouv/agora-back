package fr.social.gouv.agora.infrastructure.profile

import com.fasterxml.jackson.annotation.JsonProperty

data class ProfileJson(
    @JsonProperty("gender")
    val gender: String,
    @JsonProperty("yearOfBirth")
    val yearOfBirth: Int,
    @JsonProperty("department")
    val department: String,
    @JsonProperty("cityType")
    val cityType: String,
    @JsonProperty("jobCategory")
    val jobCategory: String,
    @JsonProperty("voteFrequency")
    val voteFrequency: String,
    @JsonProperty("publicMeetingFrequency")
    val publicMeetingFrequency: String,
    @JsonProperty("consultationFrequency")
    val consultationFrequency: String,
)