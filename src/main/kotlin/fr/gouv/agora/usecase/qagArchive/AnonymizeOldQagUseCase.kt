package fr.gouv.agora.usecase.qagArchive

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalDateTime

@Service
class AnonymizeOldQagUseCase(
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val qagInfoRepository: QagInfoRepository,
    private val clock: Clock,
) {
    private val logger: Logger = LoggerFactory.getLogger(AnonymizeOldQagUseCase::class.java)

    fun anonymizeOldQag(): AnonymizeQagListResult {
        if (featureFlagsRepository.isFeatureEnabled(AgoraFeature.QagAnonymization).not()) return AnonymizeQagListResult.FAILURE

        logger.info("🕵️ Anonymizing old QaGs...")
        val threeWeeksAgo = LocalDateTime.now(clock).minusWeeks(3)
        qagInfoRepository.anonymizeOldQags(threeWeeksAgo.toDate())
        logger.info("🕵️ Anonymizing old QaGs finished !")

        return AnonymizeQagListResult.SUCCESS
    }
}

enum class AnonymizeQagListResult { SUCCESS, FAILURE }
