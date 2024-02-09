package fr.gouv.agora.usecase.login.repository

import fr.gouv.agora.domain.LoginRequest
import fr.gouv.agora.domain.SignupRequest

interface UserDataRepository {
    fun addUserData(loginRequest: LoginRequest)
    fun addUserData(signupRequest: SignupRequest, generatedUserId: String)
    fun deleteUsersData(userIDs: List<String>)
}