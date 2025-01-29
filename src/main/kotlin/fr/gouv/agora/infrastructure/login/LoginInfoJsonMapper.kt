package fr.gouv.agora.infrastructure.login

import fr.gouv.agora.domain.UserInfo
import fr.gouv.agora.security.jwt.JwtTokenUtils
import org.springframework.stereotype.Component

@Component
class LoginInfoJsonMapper {

    fun toJson(domain: UserInfo): LoginInfoJson {
        val (jwtToken, expirationEpochMilli) = JwtTokenUtils.generateToken(userId = domain.userId)
        return LoginInfoJson(
            jwtToken = jwtToken,
            jwtExpirationEpochMilli = expirationEpochMilli,
            isModerator = false,
        )
    }

}
