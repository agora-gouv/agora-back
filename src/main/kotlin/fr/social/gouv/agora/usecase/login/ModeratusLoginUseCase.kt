package fr.social.gouv.agora.usecase.login

import fr.social.gouv.agora.infrastructure.login.DecodeResult
import fr.social.gouv.agora.infrastructure.login.LoginTokenGenerator
import fr.social.gouv.agora.usecase.login.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class ModeratusLoginUseCase(
    private val loginTokenGenerator: LoginTokenGenerator,
    private val userRepository: UserRepository,
) {

    fun login(loginToken: String): Boolean {
        return when (val decodeResult = loginTokenGenerator.decodeLoginToken(loginToken)) {
            DecodeResult.Failure -> false
            is DecodeResult.Success -> userRepository.getUserById(decodeResult.loginTokenData.userId) != null
        }
    }

}