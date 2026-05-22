package fr.gouv.agora.infrastructure.themeHebdo.repository

import java.util.Date

data class ThemeHebdoStrapiDTO(
        val theme: String,
        val periode: String,
        val photo: String,
        val nom_ministre: String? = null,
        val fonction: String? = null,
        val date_fin: String,
        val date_debut: String
)
