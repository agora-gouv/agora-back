package fr.gouv.agora.infrastructure.themeHebdo.repository

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties("createdAt", "updatedAt", "publishedAt")
data class ThemeHebdoStrapiDTO(
        @JsonProperty(value = "titre") val titre: String,
        @JsonProperty(value = "sous_titre") val sousTitre: String,
        @JsonProperty(value = "periode") val periode: String,
        @JsonProperty(value = "theme") val theme: String,
        @JsonProperty(value = "avatar_url") val avatarUrl: String,
        @JsonProperty(value = "nom") val nom: String,
        @JsonProperty(value = "fonction") val fonction: String,
        @JsonProperty(value = "prochains_themes") val prochainsThemes: List<String>,
        @JsonProperty(value = "titre_compteur") val titreCompteur: String,
        @JsonProperty(value = "date_fin_theme") val dateFinTheme: String
)
