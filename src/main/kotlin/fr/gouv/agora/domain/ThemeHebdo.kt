package fr.gouv.agora.domain

import java.util.Date

data class ThematiqueHebdo(
        val titre: String,
        val sousTitre: String,
        val periode: String,
        val theme: String,
        val avatarUrl: String,
        val nom: String,
        val fonction: String,
        val prochainsThemes: List<String>,
        val titreCompteur: String,
        val dateFinTheme: Date,
)
