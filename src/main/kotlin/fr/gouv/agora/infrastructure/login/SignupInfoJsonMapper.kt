package fr.gouv.agora.infrastructure.login

import fr.gouv.agora.domain.LoginTokenData
import fr.gouv.agora.domain.UserInfo
import fr.gouv.agora.security.jwt.JwtTokenUtils
import org.springframework.stereotype.Component

@Component
class SignupInfoJsonMapper(private val loginTokenGenerator: LoginTokenGenerator) {

    fun toJson(domain: UserInfo): SignupInfoJson? {
        val loginTokenResult = loginTokenGenerator.buildLoginToken(
            LoginTokenData(
                userId = domain.userId,
            )
        )

        return when (loginTokenResult) {
            BuildResult.Failure -> null
            is BuildResult.Success -> {
                val (jwtToken, expirationEpochMilli) = JwtTokenUtils.generateToken(userId = domain.userId)
                SignupInfoJson(
                    userId = domain.userId,
                    jwtToken = jwtToken,
                    jwtExpirationEpochMilli = expirationEpochMilli,
                    loginToken = loginTokenResult.loginToken,
                    isModerator = false,
                )
            }
        }
    }
}