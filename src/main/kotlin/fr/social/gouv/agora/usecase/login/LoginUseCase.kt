package fr.social.gouv.agora.usecase.login

import fr.social.gouv.agora.domain.LoginTokenData
import fr.social.gouv.agora.domain.UserInfo
import fr.social.gouv.agora.usecase.login.repository.LoginRepository
import org.springframework.stereotype.Service

@Service
class LoginUseCase(private val loginRepository: LoginRepository) {

    fun findUser(userId: String): UserInfo? {
        return loginRepository.getUser(userId = userId)
    }

    fun login(deviceId: String, loginTokenData: LoginTokenData, fcmToken: String): UserInfo? {
        if (deviceId != loginTokenData.deviceId) {
            return null
        }
        return loginRepository.login(userId = loginTokenData.userId, fcmToken = fcmToken)
    }

    fun signUp(deviceId: String, fcmToken: String): UserInfo? {
        return loginRepository.signUp(
            deviceId = deviceId,
            fcmToken = fcmToken
        )
    }

    fun getUser(deviceId: String): UserInfo? {
        return loginRepository.getUser(deviceId)
    }
}