package fr.gouv.agora.infrastructure.ficheInventaire

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.common.StrapiData
import fr.gouv.agora.infrastructure.common.StrapiMediaPicture
import fr.gouv.agora.infrastructure.common.StrapiRichText
import fr.gouv.agora.infrastructure.thematique.dto.ThematiqueStrapiDTO
import java.time.LocalDate

@JsonIgnoreProperties("createdAt", "updatedAt", "publishedAt")
data class FicheInventaireStrapiDTO(
    @JsonProperty(value = "etape_1_lancement")
    val etapeLancement: List<StrapiRichText>,
    @JsonProperty(value = "etape_2_analyse")
    val etapeAnalyse: List<StrapiRichText>,
    @JsonProperty(value = "etape_3_suivi")
    val etapeSuivi: List<StrapiRichText>,
    @JsonProperty(value = "titre")
    val titre: String,
    @JsonProperty(value = "debut")
    val debut: LocalDate,
    @JsonProperty(value = "fin")
    val fin: LocalDate,
    @JsonProperty(value = "porteur")
    val porteur: String,
    @JsonProperty(value = "lien_site")
    val lienSite: String,
    @JsonProperty(value = "condition_participation")
    val conditionParticipation: String,
    @JsonProperty(value = "modalite_participation")
    val modaliteParticipation: String,
    @JsonProperty(value = "thematique")
    val thematique: StrapiData<ThematiqueStrapiDTO>,
    @JsonProperty("illustration")
    val illustration: StrapiData<StrapiMediaPicture>,
    @JsonProperty("etape")
    val etape: String,
    @JsonProperty("annee_de_lancement")
    val anneeDeLancement: String,
    @JsonProperty("type")
    val type: String,
)
