package fr.gouv.agora.infrastructure.concertations

import fr.gouv.agora.domain.AgoraFeature.StrapiConcertations
import fr.gouv.agora.domain.Concertation
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Repository

@Repository
class ConcertationRepository(
    val concertationDatabaseRepository: ConcertationDatabaseRepository,
    val concertationStrapiRepository: ConcertationStrapiRepository,
    val concertationMapper: ConcertationMapper,
    val thematiqueRepository: ThematiqueRepository,
    val featureFlagsRepository: FeatureFlagsRepository,
) {
    fun getAll(): List<Concertation> {
        val thematiques = thematiqueRepository.getThematiqueList()

        val databaseConcertations = concertationDatabaseRepository.findAll()
            .let { concertationMapper.toConcertations(it, thematiques) }
        if (!featureFlagsRepository.isFeatureEnabled(StrapiConcertations)) {
            return databaseConcertations
        }

        val strapiConcertations = concertationStrapiRepository.getAll()
            .let { concertationMapper.toConcertations(it, thematiques) }

        return strapiConcertations + databaseConcertations
    }
}
