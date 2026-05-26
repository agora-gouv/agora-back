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
            val attributes = item.attributes
            ThemeHebdo(
                    periode = attributes.periode?.toString()?: "",
                    theme = attributes.theme,
                    avatarUrl = attributes.photo,
                    nom = attributes.nom_ministre,
                    fonction = attributes.fonction,
                    dateDebutTheme = Date.from(OffsetDateTime.parse(attributes.date_debut).toInstant()),
                    dateFinTheme = Date.from(OffsetDateTime.parse(attributes.date_fin).toInstant()),
            )
        }
    }
}
