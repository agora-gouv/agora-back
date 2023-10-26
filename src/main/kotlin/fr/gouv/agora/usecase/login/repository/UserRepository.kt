package fr.gouv.agora.usecase.login.repository

import fr.gouv.agora.domain.UserInfo

interface UserRepository {
    fun getUserById(userId: String): UserInfo?
    fun updateUser(userId: String, fcmToken: String): UserInfo?
    fun generateUser(fcmToken: String): UserInfo
    fun getAllUsers(): List<UserInfo>
}