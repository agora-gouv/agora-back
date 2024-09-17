package fr.gouv.agora.infrastructure.concertations

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.common.StrapiData
import fr.gouv.agora.infrastructure.thematique.dto.StrapiThematiqueDTO
import java.time.LocalDateTime

@JsonIgnoreProperties("createdAt", "updatedAt")
data class ConcertationStrapiDTO(
    @JsonProperty(value = "titre")
    val titre: String,
    @JsonProperty(value = "url")
    val urlExterne: String,
    @JsonProperty(value = "image_url")
    val urlImageDeCouverture: String,
    @JsonProperty(value = "datetime_publication")
    val dateDePublication: LocalDateTime,
    @JsonProperty(value = "thematique")
    val thematique: StrapiData<StrapiThematiqueDTO>,
    @JsonProperty("flamme_label")
    val flammeLabel: String?,
)
