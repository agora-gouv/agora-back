package fr.gouv.agora.infrastructure.themeHebdo.repository

import fr.gouv.agora.domain.ThematiqueHebdo
import fr.gouv.agora.infrastructure.common.StrapiDTO
import java.time.OffsetDateTime
import java.util.Date
import org.springframework.stereotype.Component

@Component
class ThemeHebdoMapper {
    fun toDomain(strapiDTO: StrapiDTO<ThemeHebdoStrapiDTO>): List<ThematiqueHebdo> {
        return strapiDTO.data.map { item ->
            val attributes = item.attributes
            ThematiqueHebdo(
                    titre = attributes.titre,
                    sousTitre = attributes.sousTitre,
                    periode = attributes.periode,
                    theme = attributes.theme,
                    avatarUrl = attributes.avatarUrl,
                    nom = attributes.nom,
                    fonction = attributes.fonction,
                    prochainsThemes = attributes.prochainsThemes,
                    titreCompteur = attributes.titreCompteur,
                    dateFinTheme =
                            Date.from(OffsetDateTime.parse(attributes.dateFinTheme).toInstant()),
            )
        }
    }
}
