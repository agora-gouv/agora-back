package fr.gouv.agora.domain

import java.util.Date

data class ThemeHebdo(
        var titre: String ="Cette semaine",
        var sousTitre: String = "Cette semaine posez vos questions sur...",
        val periode: String = "",
        val theme: String ="Thème libre",
        val avatarUrl: String? = null,
        val nom: String?  = null,
        val fonction: String?  = null,
        var prochainsThemes: List<String> = emptyList(),
        var titreCompteur: String = "Cloture des votes",
        val dateDebutTheme: Date? = null,
        val dateFinTheme: Date? = null,
)
