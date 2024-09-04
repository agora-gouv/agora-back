package fr.gouv.agora.infrastructure.participationCharter.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.common.StrapiRichText
import java.time.LocalDateTime

@JsonIgnoreProperties("createdAt", "updatedAt", "publishedAt")
data class ParticipationCharterStrapiDTO(
    @JsonProperty("charte")
    val charte: List<StrapiRichText>,
    @JsonProperty("charte_preview")
    val chartePreview: List<StrapiRichText>,
    @JsonProperty("datetime_debut")
    val datetimeDebut: LocalDateTime,
)
