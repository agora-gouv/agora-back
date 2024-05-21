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

    companion object {
        private const val SOFT_BAN_SIGNUP_COUNT = 10
    }

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun flagSuspiciousUsers() {
        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.SuspiciousUserDetection)) return
        logger.info("🏴 Flag users with suspicious activity")
        val dateNow = LocalDate.now(clock)
        val dateYesterday = dateNow.minusDays(1)

        val suspiciousUsersFlaggedCount = userDataRepository.flagUsersWithSuspiciousActivity(
            softBanSignupCount = SOFT_BAN_SIGNUP_COUNT,
            startDate = dateYesterday.toDate(),
            endDate = dateNow.toDate(),
        )
        logger.info("🏴 $suspiciousUsersFlaggedCount users has been flagged for suspicious activity")
    }
}