package fr.social.gouv.agora.usecase.moderatus

import fr.social.gouv.agora.domain.UserAuthorization
import fr.social.gouv.agora.domain.UserInfo
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
            is DecodeResult.Success -> userRepository.getUserById(decodeResult.loginTokenData.userId)
                ?.let(::canModerateQag) ?: false
        }
    }

    private fun canModerateQag(userInfo: UserInfo) = userInfo.authorizationList.contains(UserAuthorization.MODERATE_QAG)


}