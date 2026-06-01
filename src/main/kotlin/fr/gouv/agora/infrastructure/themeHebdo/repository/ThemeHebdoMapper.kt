package fr.gouv.agora.infrastructure.themeHebdo.repository

import fr.gouv.agora.domain.ThemeHebdo
import fr.gouv.agora.infrastructure.common.StrapiDTO
import java.time.OffsetDateTime
import java.util.Date
import org.springframework.stereotype.Component

@Component
class ThemeHebdoMapper {
    fun toDomain(strapiDTO: StrapiDTO<ThemeHebdoStrapiDTO>): List<ThemeHebdo> {
        return strapiDTO.data.map { item ->
            ThemeHebdo(
                    periode = item.periode?.toString() ?: "",
                    theme = item.theme,
                    avatarUrl = item.photo?.mediaUrl(),
                    nom = item.nom_ministre,
                    fonction = item.fonction,
                    dateDebutTheme = Date.from(OffsetDateTime.parse(item.date_debut).toInstant()),
                    dateFinTheme = Date.from(OffsetDateTime.parse(item.date_fin).toInstant()),
                    estThemeLibre = item.est_theme_libre,
            )
        }
    }
}
