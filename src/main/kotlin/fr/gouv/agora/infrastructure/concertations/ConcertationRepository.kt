package fr.gouv.agora.infrastructure.concertations

import fr.gouv.agora.domain.Concertation
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Repository

@Repository
class ConcertationRepository(
    val concertationStrapiRepository: ConcertationStrapiRepository,
    val concertationMapper: ConcertationMapper,
    val thematiqueRepository: ThematiqueRepository,
    val featureFlagsRepository: FeatureFlagsRepository,
) {
    fun getAll(): List<Concertation> {
        val thematiques = thematiqueRepository.getThematiqueList()

        val strapiConcertations = concertationStrapiRepository.getAll()
            .let { concertationMapper.toConcertations(it, thematiques) }

        return strapiConcertations
    }
}
