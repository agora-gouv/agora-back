package fr.gouv.agora.usecase.login.repository

import fr.gouv.agora.domain.LoginRequest
import fr.gouv.agora.domain.SignupRequest
import fr.gouv.agora.domain.SignupHistoryCount

interface UserDataRepository {
    fun addUserData(loginRequest: LoginRequest)
    fun addUserData(signupRequest: SignupRequest, generatedUserId: String)
    fun deleteUsersData(userIDs: List<String>)
    fun getSignupHistory(ipAddressHash: String): List<SignupHistoryCount>
}