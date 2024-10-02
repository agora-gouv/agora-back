package fr.gouv.agora.usecase.concertations

import fr.gouv.agora.domain.Concertation
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.common.DateMapper
import fr.gouv.agora.infrastructure.concertations.ConcertationDatabaseDTO
import fr.gouv.agora.infrastructure.concertations.ConcertationJson
import fr.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import org.springframework.stereotype.Component

@Component
class ConcertationJsonMapper(
    private val thematiqueJsonMapper: ThematiqueJsonMapper,
    private val dateMapper: DateMapper,
) {
    fun toConcertationJson(concertations: List<Concertation>): List<ConcertationJson> {
        return concertations.map { concertation ->
            ConcertationJson(
                concertation.id,
                concertation.title,
                concertation.imageUrl,
                concertation.externalLink,
                thematiqueJsonMapper.toNoIdJson(concertation.thematique),
                concertation.updateLabel,
                dateMapper.toFormattedDate(concertation.updateDate),
                concertation.territory
            )
        }
    }
}
