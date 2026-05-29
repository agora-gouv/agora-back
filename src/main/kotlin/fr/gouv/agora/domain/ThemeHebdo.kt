package fr.gouv.agora.domain

import java.util.Date

data class ThemeHebdo(
    var titre: String ="CETTE SEMAINE",
    var sousTitre: String = "Posez toutes vos questions sur",
    var periode: String = "",
    val theme: String ="Semaine libre",
    val avatarUrl: String? = null,
    val nom: String?  = null,
    val fonction: String?  = null,
    var prochainsThemes: List<String> = emptyList(),
    var titreCompteur: String = "Fin des votes le",
    val dateDebutTheme: Date? = null,
    val dateFinTheme: Date? = null,
    val estThemeLibre: Boolean = true,
)
