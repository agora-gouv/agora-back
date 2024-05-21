package fr.gouv.agora.usecase.login.repository

import fr.gouv.agora.domain.LoginRequest
import fr.gouv.agora.domain.SignupRequest
import fr.gouv.agora.domain.SignupHistoryCount
import java.util.*

interface UserDataRepository {
    fun addUserData(loginRequest: LoginRequest)
    fun addUserData(signupRequest: SignupRequest, generatedUserId: String)
    fun deleteUsersData(userIDs: List<String>)
    fun getSignupHistory(ipAddressHash: String, userAgent: String): List<SignupHistoryCount>
    fun flagUsersWithSuspiciousActivity(
        softBanSignupCount: Int,
        startDate: Date,
        endDate: Date,
    ): Int
}