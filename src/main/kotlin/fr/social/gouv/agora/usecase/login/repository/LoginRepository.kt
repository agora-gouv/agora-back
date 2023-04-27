package fr.social.gouv.agora.usecase.login.repository

import fr.social.gouv.agora.domain.UserInfo

interface LoginRepository {
    fun loginOrRegister(deviceId: String, fcmToken: String): UserInfo?
}