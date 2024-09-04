package fr.gouv.agora.infrastructure.participationCharter.repository

import fr.gouv.agora.domain.AgoraFeature.StrapiParticipationCharter
import fr.gouv.agora.infrastructure.common.toHtml
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.participationCharter.ParticipationCharter
import fr.gouv.agora.usecase.participationCharter.repository.ParticipationCharterRepository
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class ParticipationCharterRepositoryImpl(
    private val databaseRepository: ParticipationCharterDatabaseRepository,
    private val participationCharterStrapiRepository: ParticipationCharterStrapiRepository,
    private val clock: Clock,
    private val featureFlagsRepository: FeatureFlagsRepository,
) : ParticipationCharterRepository {

    override fun getLatestParticipationCharter(): ParticipationCharter {
        val now = LocalDateTime.now(clock)
        return if (featureFlagsRepository.isFeatureEnabled(StrapiParticipationCharter)) {
            val charte = participationCharterStrapiRepository.getLastParticipationCharter(now)
                ?.attributes?.charte?.toHtml()?.let { "<body>$it</body>" } ?: ""
            val preview = participationCharterStrapiRepository.getLastParticipationCharter(now)
                ?.attributes?.chartePreview?.toHtml()?.let { "<body>$it</body>" } ?: ""
            return ParticipationCharter(charte, preview)
        } else {
            return ParticipationCharter(databaseRepository.getLatestParticipationCharterText(now), "")
        }
    }
}
