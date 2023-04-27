package fr.social.gouv.agora.infrastructure.login

import fr.social.gouv.agora.domain.UserInfo
import org.springframework.stereotype.Component

@Component
class UserInfoJsonMapper {

    fun toJson(domain: UserInfo): UserInfoJson {
        return UserInfoJson(userId = domain.userId)
    }

}