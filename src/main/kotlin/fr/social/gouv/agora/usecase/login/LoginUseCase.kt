package fr.social.gouv.agora.usecase.login

import fr.social.gouv.agora.domain.LoginTokenData
import fr.social.gouv.agora.domain.UserInfo
import fr.social.gouv.agora.usecase.login.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class LoginUseCase(private val userRepository: UserRepository) {

    fun findUser(userId: String): UserInfo? {
        return userRepository.getUserById(userId = userId)
    }

    fun login(deviceId: String, loginTokenData: LoginTokenData, fcmToken: String): UserInfo? {
        if (deviceId != loginTokenData.deviceId) {
            return null
        }
        return userRepository.getUserById(loginTokenData.userId)?.let { userInfo ->
            if (userInfo.fcmToken != fcmToken) {
                userRepository.updateUserFcmToken(userId = loginTokenData.userId, fcmToken = fcmToken)
            } else userInfo
        }
    }

    fun signUp(deviceId: String, fcmToken: String): UserInfo? {
        val userInfo = userRepository.getUserByDeviceId(deviceId)
        if (userInfo != null) return null

        return userRepository.generateUser(deviceId = deviceId, fcmToken = fcmToken)
    }

}