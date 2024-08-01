package fr.gouv.agora.infrastructure.participationCharter.repository

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.AgoraFeature.StrapiParticipationCharter
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.participationCharter.repository.ParticipationCharterRepository
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class ParticipationCharterRepositoryImpl(
    private val cacheRepository: ParticipationCharterCacheRepository,
    private val databaseRepository: ParticipationCharterDatabaseRepository,
    private val featureFlagsRepository: FeatureFlagsRepository,
) : ParticipationCharterRepository {

    override fun getLatestParticipationCharter(): String {
        val cachedParticipationCharter = cacheRepository.getLatestParticipationCharterText()
        if (cachedParticipationCharter != null) {
            return cachedParticipationCharter
        }

        val participationCharterText = if (featureFlagsRepository.isFeatureEnabled(StrapiParticipationCharter)) {
            TODO()
        } else {
            databaseRepository.getLatestParticipationCharterText()
        }

        cacheRepository.initLatestParticipationCharterText(participationCharterText)

        return participationCharterText
    }

}
