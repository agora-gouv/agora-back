package fr.gouv.agora.usecase.suspiciousUser

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.login.repository.UserDataRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDate

@Component
class SuspiciousActivityDetectUseCase(
    private val clock: Clock,
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val userDataRepository: UserDataRepository,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun flagSuspiciousUsers() {
        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.SuspiciousUserDetection)) return
        logger.info("🏴 Flag users with suspicious activity")
        val dateNow = LocalDate.now(clock)
        val twoWeeksAgo = dateNow.minusWeeks(2)

        val suspiciousUsersFlaggedCount = userDataRepository.flagUsersWithSuspiciousActivity(
            softBanSignupCount = 3,
            startDate = twoWeeksAgo.toDate(),
            endDate = dateNow.toDate(),
        )
        logger.info("🏴 $suspiciousUsersFlaggedCount users has been flagged for suspicious activity")
    }
}
