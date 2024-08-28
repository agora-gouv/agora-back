package fr.gouv.agora.infrastructure.participationCharter.repository

import fr.gouv.agora.domain.AgoraFeature.StrapiParticipationCharter
import fr.gouv.agora.infrastructure.common.toHtml
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.participationCharter.repository.ParticipationCharterRepository
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class ParticipationCharterRepositoryImpl(
    private val cacheRepository: ParticipationCharterCacheRepository,
    private val databaseRepository: ParticipationCharterDatabaseRepository,
    private val participationCharterStrapiRepository: ParticipationCharterStrapiRepository,
    private val clock: Clock,
    private val featureFlagsRepository: FeatureFlagsRepository,
) : ParticipationCharterRepository {

    override fun getLatestParticipationCharter(): String {
        val cachedParticipationCharter = cacheRepository.getLatestParticipationCharterText()
        if (cachedParticipationCharter != null) {
            return cachedParticipationCharter
        }

        val now = LocalDateTime.now(clock)
        val participationCharterText = if (featureFlagsRepository.isFeatureEnabled(StrapiParticipationCharter)) {
            participationCharterStrapiRepository.getLastParticipationCharter(now)
                ?.attributes?.charte?.toHtml()?.let { "<body>$it</body>" } ?: ""
        } else {
            databaseRepository.getLatestParticipationCharterText(now)
        }

        cacheRepository.initLatestParticipationCharterText(participationCharterText)

        return participationCharterText
    }

}
