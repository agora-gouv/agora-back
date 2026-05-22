package fr.gouv.agora.domain

import java.util.Date

data class ThemeHebdo(
        var titre: String,
        var sousTitre: String,
        val periode: String,
        val theme: String,
        val avatarUrl: String,
        val nom: String?,
        val fonction: String?,
        var prochainsThemes: List<String>,
        var titreCompteur: String,
        val dateDebutTheme: Date,
        val dateFinTheme: Date,
)
