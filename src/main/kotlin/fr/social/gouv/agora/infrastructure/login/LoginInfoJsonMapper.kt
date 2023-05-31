package fr.social.gouv.agora.infrastructure.login

import fr.social.gouv.agora.domain.UserAuthorization
import fr.social.gouv.agora.domain.UserInfo
import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import org.springframework.stereotype.Component

@Component
class LoginInfoJsonMapper {

    fun toJson(domain: UserInfo): LoginInfoJson {
        return LoginInfoJson(
            jwtToken = JwtTokenUtils.generateToken(userId = domain.userId),
            isModerator = domain.authorizationList.contains(
                UserAuthorization.MODERATE_QAG
            )
        )
    }

}