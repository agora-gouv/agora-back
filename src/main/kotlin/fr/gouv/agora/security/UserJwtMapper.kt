package fr.gouv.agora.security

import fr.gouv.agora.domain.UserAuthorization
import fr.gouv.agora.domain.UserInfo
import org.springframework.stereotype.Component

@Component
class UserJwtMapper {

    fun toJwt(domain: UserInfo): UserInfoJwt {
        return UserInfoJwt(
            userId = domain.userId,
            isBanned = domain.isBanned,
            authorizationList = domain.authorizationList.map(::toJwt)
        )
    }

    private fun toJwt(domain: UserAuthorization): UserAuthorizationJWT {
        return when (domain) {
            UserAuthorization.VIEW_CONSULTATION -> UserAuthorizationJWT.VIEW_CONSULTATION
            UserAuthorization.ANSWER_CONSULTATION -> UserAuthorizationJWT.ANSWER_CONSULTATION
            UserAuthorization.VIEW_QAG -> UserAuthorizationJWT.VIEW_QAG
            UserAuthorization.SUPPORT_QAG -> UserAuthorizationJWT.SUPPORT_QAG
            UserAuthorization.FEEDBACK_QAG_RESPONSE -> UserAuthorizationJWT.FEEDBACK_QAG_RESPONSE
            UserAuthorization.ADD_QAG -> UserAuthorizationJWT.ADD_QAG
            UserAuthorization.MODERATE_QAG -> UserAuthorizationJWT.MODERATE_QAG
            UserAuthorization.ADMIN_APIS -> UserAuthorizationJWT.ADMIN_APIS
        }
    }

}