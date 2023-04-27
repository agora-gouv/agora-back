package fr.social.gouv.agora.usecase.login

import fr.social.gouv.agora.domain.UserInfo
import fr.social.gouv.agora.usecase.login.repository.LoginRepository
import org.springframework.stereotype.Service

@Service
class LoginUseCase(private val loginRepository: LoginRepository) {

    fun login(deviceId: String, fcmToken: String): UserInfo? {
        return loginRepository.loginOrRegister(
            deviceId = deviceId,
            fcmToken = fcmToken
        )
    }

}