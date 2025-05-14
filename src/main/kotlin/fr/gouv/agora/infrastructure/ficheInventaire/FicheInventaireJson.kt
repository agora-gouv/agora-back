package fr.gouv.agora.infrastructure.ficheInventaire

import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.thematique.ThematiqueNoIdJson

data class FicheInventaireJson(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("etapeLancementHtml")
    val etapeLancementHtml: String,
    @JsonProperty("etapeAnalyseHtml")
    val etapeAnalyseHtml: String,
    @JsonProperty("etapeSuiviHtml")
    val etapeSuiviHtml: String,
    @JsonProperty("titre")
    val titre: String,
    @JsonProperty("debut")
    val debut: String,
    @JsonProperty("fin")
    val fin: String,
    @JsonProperty("porteur")
    val porteur: String,
    @JsonProperty("lienSite")
    val lienSite: String,
    @JsonProperty("conditionParticipation")
    val conditionParticipation: String,
    @JsonProperty("modaliteParticipation")
    val modaliteParticipation: String,
    @JsonProperty("thematique")
    val thematique: ThematiqueNoIdJson,
    @JsonProperty("illustrationUrl")
    val illustrationUrl: String,
    @JsonProperty("etape")
    val etape: String,
    @JsonProperty("anneeDeLancement")
    val anneeDeLancement: String,
    @JsonProperty("type")
    val type: String,
)
