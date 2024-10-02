package fr.gouv.agora.infrastructure.concertations

import fr.gouv.agora.domain.Concertation
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.common.StrapiDTO
import org.springframework.stereotype.Service

@Service
class ConcertationMapper {
    fun toConcertations(concertations: List<ConcertationDatabaseDTO>, thematiques: List<Thematique>): List<Concertation> {
        return concertations.mapNotNull { conceration ->
            val thematique = thematiques.firstOrNull { conceration.thematiqueId.toString() == it.id }
                ?: return@mapNotNull null

            Concertation(
                conceration.id.toString(),
                conceration.title,
                conceration.imageUrl,
                conceration.externalLink,
                thematique,
                conceration.updateLabel,
                conceration.lastUpdateDate.atStartOfDay(),
            )
        }
    }

    fun toConcertations(concertations: StrapiDTO<ConcertationStrapiDTO>, thematiques: List<Thematique>): List<Concertation> {
        return concertations.data.mapNotNull { conceration ->
            val thematique = thematiques.firstOrNull {
                it.id == conceration.attributes.thematique.data.attributes.databaseId
            } ?: return@mapNotNull null

            Concertation(
                conceration.id,
                conceration.attributes.titre,
                conceration.attributes.urlImageDeCouverture,
                conceration.attributes.urlExterne,
                thematique,
                conceration.attributes.flammeLabel,
                conceration.attributes.dateDePublication,
            )
        }
    }
}
