package fr.gouv.agora.domain

import java.util.Date

data class ThemeHebdo(
    var titre: String ="CETTE SEMAINE",
    var sousTitre: String = "Posez toutes vos questions sur",
    var periode: String = "",
    val theme: String ="Semaine libre",
    val avatarUrl: String? = "https://pub-6c821c1c547c4e3eaa97abd4b0ab8180.r2.dev/logo_agora_f524e2d9bd.png",
    val nom: String?  = "Ministre à révéler",
    val fonction: String?  = "Le ministre qui répondra dépend de votre question gagnante",
    var prochainsThemes: List<String> = emptyList(),
    var titreCompteur: String = "Fin des votes le",
    val dateDebutTheme: Date? = null,
    val dateFinTheme: Date? = null,
    val estThemeLibre: Boolean = true,
)
