package fr.gouv.agora.infrastructure.welcomePage.repository

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.common.StrapiRichText
import java.time.LocalDateTime

@JsonIgnoreProperties("createdAt", "updatedAt", "publishedAt")
data class NewsStrapiDTO(
    @JsonProperty("message")
    val message: List<StrapiRichText>,
    @JsonProperty("call_to_action")
    val callToAction: String,
    @JsonProperty("date_de_debut")
    val dateDeDebut: LocalDateTime,
    @JsonProperty("page_route_mobile")
    val pageRouteMobile: String,
    @JsonProperty("page_route_argument_mobile")
    val pageRouteArgumentMobile: String?,
)
