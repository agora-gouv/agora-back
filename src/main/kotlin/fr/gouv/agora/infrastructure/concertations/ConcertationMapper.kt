package fr.gouv.agora.infrastructure.concertations

import fr.gouv.agora.domain.Concertation
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.common.StrapiDTO
import org.springframework.stereotype.Service

@Service
class ConcertationMapper {
    fun toConcertations(concertations: StrapiDTO<ConcertationStrapiDTO>, thematiques: List<Thematique>): List<Concertation> {
        return concertations.data.mapNotNull { conceration ->
            val thematique = thematiques.firstOrNull {
                it.id == conceration.thematique.documentId
            } ?: return@mapNotNull null

            Concertation(
                conceration.documentId,
                conceration.titre,
                conceration.getUrlImageCouverture(),
                conceration.urlExterne,
                thematique,
                conceration.flammeLabel,
                conceration.dateDePublication,
            )
        }
    }
}
