package fr.gouv.agora.infrastructure.headerQag.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

@JsonIgnoreProperties("createdAt", "updatedAt", "publishedAt")
data class HeaderQagStrapiDTO(
    @JsonProperty("titre")
    val titre: String,
    @JsonProperty("message")
    val message: String,
    @JsonProperty("type")
    val type: String,
    @JsonProperty("datetime_publication")
    val datetimePublication: LocalDateTime,
)
