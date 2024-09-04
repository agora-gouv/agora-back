package fr.gouv.agora.usecase.participationCharter

import fr.gouv.agora.usecase.participationCharter.repository.ParticipationCharterRepository
import org.springframework.stereotype.Service

@Service
class ParticipationCharterUseCase(private val repository: ParticipationCharterRepository) {
    fun getParticipationCharterText(): ParticipationCharter {
        return repository.getLatestParticipationCharter()
    }
}

data class ParticipationCharter(val text: String, val preview: String)
