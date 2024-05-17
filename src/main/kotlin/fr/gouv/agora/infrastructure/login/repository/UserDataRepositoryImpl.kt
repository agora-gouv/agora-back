package fr.gouv.agora.infrastructure.login.repository

import fr.gouv.agora.domain.LoginRequest
import fr.gouv.agora.domain.SignupHistoryCount
import fr.gouv.agora.domain.SignupRequest
import fr.gouv.agora.usecase.login.repository.UserDataRepository
import org.springframework.stereotype.Component
import java.util.Date

@Component
@Suppress("unused")
class UserDataRepositoryImpl(
    private val databaseRepository: UserDataDatabaseRepository,
    private val mapper: UserDataMapper,
) : UserDataRepository {

    override fun addUserData(loginRequest: LoginRequest) {
        databaseRepository.save(mapper.toDto(loginRequest = loginRequest))
    }

    override fun addUserData(signupRequest: SignupRequest, generatedUserId: String) {
        databaseRepository.save(mapper.toDto(signupRequest = signupRequest, userId = generatedUserId))
    }

    override fun deleteUsersData(userIDs: List<String>) {
        databaseRepository.deleteUsersData(userIDs)
    }

    override fun getSignupHistory(ipAddressHash: String, userAgent: String): List<SignupHistoryCount> {
        return databaseRepository.getIpHashSignupHistory(
            ipAddressHash = ipAddressHash,
            userAgent = userAgent,
        ).map { signupEntry ->
            SignupHistoryCount(
                date = signupEntry.date,
                signupCount = signupEntry.signupCount,
            )
        }
    }

    override fun flagUsersWithSuspiciousActivity(softBanSignupCount: Int, startDate: Date, endDate: Date): Int {
        return databaseRepository.flagUsersWithSuspiciousActivity(
            softBanSignupCount = softBanSignupCount,
            startDate = startDate,
            endDate = endDate,
        )
    }

}