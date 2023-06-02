package fr.social.gouv.agora.infrastructure.login

import fr.social.gouv.agora.domain.UserInfo
import fr.social.gouv.agora.security.cipher.BuildResult
import fr.social.gouv.agora.domain.LoginTokenData
import fr.social.gouv.agora.domain.UserAuthorization
import fr.social.gouv.agora.security.cipher.LoginTokenGenerator
import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import org.springframework.stereotype.Component

@Component
class SignupInfoJsonMapper {

    fun toJson(domain: UserInfo, deviceId: String): SignupInfoJson? {
        val loginTokenResult = LoginTokenGenerator.buildLoginToken(
            LoginTokenData(
                deviceId = deviceId,
                userId = domain.userId,
            )
        )

        return when (loginTokenResult) {
            BuildResult.Failure -> null
            is BuildResult.Success -> SignupInfoJson(
                jwtToken = JwtTokenUtils.generateToken(userId = domain.userId),
                loginToken = loginTokenResult.loginToken,
                isModerator = domain.authorizationList.contains(UserAuthorization.MODERATE_QAG)
            )
        }
    }
}