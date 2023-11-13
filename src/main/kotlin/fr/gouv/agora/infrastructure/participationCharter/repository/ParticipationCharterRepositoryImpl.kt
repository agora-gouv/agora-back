package fr.gouv.agora.infrastructure.participationCharter.repository

import fr.gouv.agora.usecase.participationCharter.repository.ParticipationCharterRepository
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class ParticipationCharterRepositoryImpl(
    private val cacheRepository: ParticipationCharterCacheRepository,
    private val databaseRepository: ParticipationCharterDatabaseRepository,
) : ParticipationCharterRepository {

    override fun getLatestParticipationCharter(): String {
        return cacheRepository.getLatestParticipationCharterText()
            ?: databaseRepository.getLatestParticipationCharterText().also { participationCharterText ->
                cacheRepository.initLatestParticipationCharterText(participationCharterText)
            }
    }

}