package fr.gouv.agora.infrastructure.themeHebdo

import com.fasterxml.jackson.annotation.JsonProperty

data class ThemeHebdoJson(
        @JsonProperty("titre") val titre: String,
        @JsonProperty("sousTitre") val sousTitre: String,
        @JsonProperty("periode") val periode: String,
        @JsonProperty("theme") val theme: String,
        @JsonProperty("avatarUrl") val avatarUrl: String,
        @JsonProperty("nom") val nom: String,
        @JsonProperty("fonction") val fonction: String,
        @JsonProperty("prochainsThemes") val prochainsThemes: List<String>,
        @JsonProperty("titreCompteur") val titreCompteur: String,
        @JsonProperty("dateFinTheme") val dateFinTheme: String,
)
