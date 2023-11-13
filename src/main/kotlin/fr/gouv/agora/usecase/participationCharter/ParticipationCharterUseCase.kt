package fr.gouv.agora.usecase.participationCharter

import fr.gouv.agora.usecase.participationCharter.repository.ParticipationCharterRepository
import org.springframework.stereotype.Service

@Service
class ParticipationCharterUseCase(private val repository: ParticipationCharterRepository) {

    fun getParticipationCharterText(): String {
        return repository.getLatestParticipationCharter()
    }

}