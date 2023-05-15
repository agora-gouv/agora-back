package fr.social.gouv.agora.usecase.login

import fr.social.gouv.agora.domain.UserInfo
import fr.social.gouv.agora.usecase.login.repository.LoginRepository
import org.springframework.stereotype.Service

@Service
class LoginUseCase(private val loginRepository: LoginRepository) {

    fun login(userId: String): UserInfo? {
        return loginRepository.getUser(userId = userId)
    }

    fun loginOrRegister(deviceId: String, fcmToken: String): UserInfo? {
        return loginRepository.loginOrRegister(
            deviceId = deviceId,
            fcmToken = fcmToken
        )
    }

    fun getUser(deviceId: String): UserInfo? {
        return loginRepository.getUser(deviceId)
    }
}