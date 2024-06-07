package fr.gouv.agora.usecase.concertations

import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.common.DateMapper
import fr.gouv.agora.infrastructure.concertations.ConcertationDTO
import fr.gouv.agora.infrastructure.concertations.ConcertationJson
import fr.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import org.springframework.stereotype.Component

@Component
class ConcertationJsonMapper(
    private val thematiqueJsonMapper: ThematiqueJsonMapper,
    private val dateMapper: DateMapper,
) {
    fun toConcertationJson(concertations: List<ConcertationDTO>, thematiques: List<Thematique>): List<ConcertationJson> {
        return concertations.mapNotNull { concertation ->
            val thematique = thematiques.firstOrNull { it.id == concertation.thematiqueId.toString() }
                ?: return@mapNotNull null
            val thematiqueNoIdJson = thematiqueJsonMapper.toNoIdJson(thematique)

            ConcertationJson(
                concertation.id.toString(),
                concertation.title,
                concertation.imageUrl,
                concertation.externalLink,
                thematiqueNoIdJson,
                concertation.updateLabel,
                dateMapper.toFormattedDate(concertation.lastUpdateDate),
            )
        }
    }
}
