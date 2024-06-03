package fr.gouv.agora.usecase.concertations

import fr.gouv.agora.infrastructure.concertations.ConcertationDatabaseRepository
import fr.gouv.agora.infrastructure.concertations.ConcertationJson
import fr.gouv.agora.infrastructure.thematique.repository.ThematiqueRepositoryImpl
import org.springframework.stereotype.Component

@Component
class GetConcertationsUseCase(
    private val concertationDatabaseRepository: ConcertationDatabaseRepository,
    private val thematiqueRepository: ThematiqueRepositoryImpl,
    private val concertationJsonMapper: ConcertationJsonMapper,
) {
    fun execute(): List<ConcertationJson> {
        val concertations = concertationDatabaseRepository.findAll()
        val thematiques = thematiqueRepository.getThematiqueList()

        return concertationJsonMapper.toConcertationJson(concertations, thematiques)
    }
}
