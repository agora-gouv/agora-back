package fr.gouv.agora.usecase.themeHebdo

import fr.gouv.agora.domain.ThematiqueHebdo
import java.util.Date
import org.springframework.stereotype.Service

@Service
class GetThemeHebdoUseCase {

    fun getThemeHebdo(): ThematiqueHebdo {
        return ThematiqueHebdo(
                titre = "Le thème hebdo de la semaine",
                sousTitre = "Découvrez le sujet de la semaine",
                periode = "Du 19 au 25 mai 2026",
                theme = "Éducation",
                avatarUrl = "https://example.com/avatar.png",
                nom = "Jean Dupont",
                fonction = "Ministre de l'Éducation nationale",
                prochainsThemes = listOf("Santé", "Environnement", "Économie"),
                titreCompteur = "questions posées cette semaine",
                dateFinTheme =
                        Date.from(
                                java.time.LocalDate.of(2026, 5, 25)
                                        .atStartOfDay(java.time.ZoneId.of("Europe/Paris"))
                                        .toInstant()
                        ),
        )
    }
}
