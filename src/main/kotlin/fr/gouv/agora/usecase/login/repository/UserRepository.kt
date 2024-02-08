package fr.gouv.agora.usecase.login.repository

import fr.gouv.agora.domain.LoginRequest
import fr.gouv.agora.domain.SignupRequest
import fr.gouv.agora.domain.UserInfo

interface UserRepository {
    fun getUserById(userId: String): UserInfo?
    fun updateUser(loginRequest: LoginRequest): UserInfo?
    fun generateUser(signupRequest: SignupRequest): UserInfo
    fun getAllUsers(): List<UserInfo>
    fun getUsersNotAnsweredConsultation(consultationId: String): List<UserInfo>
    fun deleteUsers(userIDs: List<String>)
}