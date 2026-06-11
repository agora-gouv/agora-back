package fr.gouv.agora.infrastructure.themeHebdo

import fr.gouv.agora.domain.ThemeHebdo
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import org.springframework.stereotype.Component

@Component
class ThemeHebdoJsonMapper {

    private val formatter =
            DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Europe/Paris"))

    fun toJson(domain: ThemeHebdo): ThemeHebdoJson {
        return ThemeHebdoJson(
                titre = domain.titre,
                sousTitre = domain.sousTitre,
                periode = domain.periode,
                theme = domain.theme,
                avatarUrl = domain.avatarUrl,
                nom = domain.nom,
                fonction = domain.fonction,
                prochainsThemes = domain.prochainsThemes,
                titreCompteur = domain.titreCompteur,
                dateFinTheme = formatter.format(domain.dateFinTheme?.toInstant()),
                dateDebutTheme = formatter.format(domain.dateDebutTheme?.toInstant()),
                estThemeLibre = domain.estThemeLibre,
        )
    }
}
