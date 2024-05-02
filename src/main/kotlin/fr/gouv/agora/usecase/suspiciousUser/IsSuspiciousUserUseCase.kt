package fr.gouv.agora.usecase.suspiciousUser

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.SignupHistoryCount
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.login.repository.UserDataRepository
import fr.gouv.agora.usecase.suspiciousUser.repository.SignupCountRepository
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDate

@Component
class IsSuspiciousUserUseCase(
    private val clock: Clock,
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val signupCountRepository: SignupCountRepository,
    private val userDataRepository: UserDataRepository,
) {

    companion object {
        private const val SOFT_BAN_SIGNUP_COUNT = 10
    }

    fun isSuspiciousUser(ipAddressHash: String, userAgent: String): Boolean {
        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.SuspiciousUserDetection)) return false
        return (signupCountRepository.getTodaySignupCount(ipAddressHash = ipAddressHash, userAgent = userAgent)
            ?: buildTodaySignupCount(ipAddressHash = ipAddressHash, userAgent = userAgent)) >= SOFT_BAN_SIGNUP_COUNT
    }

    fun notifySignup(ipAddressHash: String, userAgent: String) {
        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.SuspiciousUserDetection)) return
        signupCountRepository.getTodaySignupCount(
            ipAddressHash = ipAddressHash,
            userAgent = userAgent,
        )?.let { signupCount ->
            signupCountRepository.initTodaySignupCount(
                ipAddressHash = ipAddressHash,
                userAgent = userAgent,
                todaySignupCount = signupCount + 1,
            )
        }
    }

    private fun buildTodaySignupCount(ipAddressHash: String, userAgent: String): Int {
        val dateNow = LocalDate.now(clock)
        val signupHistory = userDataRepository.getSignupHistory(ipAddressHash = ipAddressHash, userAgent = userAgent)

        return (signupHistory.find { historyEntry -> historyEntry.signupCount >= SOFT_BAN_SIGNUP_COUNT }
            ?: signupHistory.find { historyEntry -> historyEntry.date == dateNow }
            ?: SignupHistoryCount(date = LocalDate.MIN, signupCount = 0))
            .let { historyEntry ->
                signupCountRepository.initTodaySignupCount(
                    ipAddressHash = ipAddressHash,
                    userAgent = userAgent,
                    todaySignupCount = historyEntry.signupCount,
                )
                historyEntry.signupCount
            }
    }
}
