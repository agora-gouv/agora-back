package fr.gouv.agora.usecase.themeHebdo

import fr.gouv.agora.domain.ThematiqueHebdo
import java.util.Date
import org.springframework.stereotype.Service

@Service
class GetThemeHebdoUseCase {

    fun getThemeHebdo(): ThematiqueHebdo {
        return ThematiqueHebdo(
                titre = "Cette semaine",
                sousTitre = "Cette semaine posez vos questions sur...",
                periode = "19-25 mai 2026",
                theme = "Éducation",
                avatarUrl = "https://picsum.photos/40",
                nom = "Jean Dupont",
                fonction = "Ministre de l'Éducation nationale",
                prochainsThemes = listOf("Santé", "Environnement", "Économie"),
                titreCompteur = "Cloture des votes",
                dateFinTheme =
                        Date.from(
                                java.time.LocalDate.of(2026, 5, 25)
                                        .atStartOfDay(java.time.ZoneId.of("Europe/Paris"))
                                        .toInstant()
                        ),
        )
    }
}
