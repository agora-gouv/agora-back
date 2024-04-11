package fr.gouv.agora.usecase.suspiciousUser

import fr.gouv.agora.domain.SignupHistoryCount
import fr.gouv.agora.usecase.login.repository.UserDataRepository
import fr.gouv.agora.usecase.suspiciousUser.repository.SignupCountRepository
import java.time.Clock
import java.time.LocalDate

class IsSuspiciousUserUseCase(
    private val clock: Clock,
    private val signupCountRepository: SignupCountRepository,
    private val userDataRepository: UserDataRepository,
) {

    companion object {
        private const val SOFT_BAN_SIGNUP_COUNT = 10
    }

    fun isSuspiciousUser(ipAddressHash: String): Boolean {
        return (signupCountRepository.getTodaySignupCount(ipAddressHash)
            ?: buildTodaySignupCount(ipAddressHash)) >= SOFT_BAN_SIGNUP_COUNT
    }

    fun notifySignup(ipAddressHash: String) {
        signupCountRepository.getTodaySignupCount(ipAddressHash)?.let { signupCount ->
            signupCountRepository.initTodaySignupCount(
                ipAddressHash = ipAddressHash,
                todaySignupCount = signupCount + 1,
            )
        }
    }

    private fun buildTodaySignupCount(ipAddressHash: String): Int {
        val dateNow = LocalDate.now(clock)
        val signupHistory = userDataRepository.getSignupHistory(ipAddressHash)

        return (signupHistory.find { historyEntry -> historyEntry.signupCount >= SOFT_BAN_SIGNUP_COUNT }
            ?: signupHistory.find { historyEntry -> historyEntry.date == dateNow }
            ?: SignupHistoryCount(date = LocalDate.MIN, signupCount = 0))
            .let { historyEntry ->
                signupCountRepository.initTodaySignupCount(
                    ipAddressHash = ipAddressHash,
                    todaySignupCount = historyEntry.signupCount,
                )
                historyEntry.signupCount
            }
    }
}
