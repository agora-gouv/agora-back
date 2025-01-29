package fr.gouv.agora.usecase.concertations

import fr.gouv.agora.domain.Concertation
import fr.gouv.agora.infrastructure.concertations.ConcertationRepository
import org.springframework.stereotype.Component

@Component
class GetConcertationsUseCase(
    private val concertationRepository: ConcertationRepository,
) {
    fun execute(): List<Concertation> {
        return concertationRepository.getAll()
            .sortedByDescending { it.updateDate }
    }
}
