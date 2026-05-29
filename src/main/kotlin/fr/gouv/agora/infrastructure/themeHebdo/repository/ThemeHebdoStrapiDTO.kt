package fr.gouv.agora.infrastructure.themeHebdo.repository

import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.agora.infrastructure.common.StrapiDataNullable
import fr.gouv.agora.infrastructure.common.StrapiMediaPicture

data class ThemeHebdoStrapiDTO(
        val theme: String,
        val periode: String?,
        val photo: StrapiDataNullable<StrapiMediaPicture>,
        val nom_ministre: String? = null,
        val fonction: String? = null,
        val date_fin: String,
        val date_debut: String,
        val est_theme_libre: Boolean = false,
)
