package fr.social.gouv.agora.usecase.login.repository

import fr.social.gouv.agora.domain.UserInfo

interface LoginRepository {
    fun getUser(userId: String): UserInfo?
    fun login(userId: String, fcmToken: String): UserInfo?
    fun signUp(deviceId: String, fcmToken: String): UserInfo?
}