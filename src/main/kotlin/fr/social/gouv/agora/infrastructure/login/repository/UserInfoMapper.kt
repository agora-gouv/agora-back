package fr.social.gouv.agora.infrastructure.login.repository

import fr.social.gouv.agora.domain.UserAuthorization
import fr.social.gouv.agora.domain.UserInfo
import fr.social.gouv.agora.infrastructure.login.dto.UserDTO
import fr.social.gouv.agora.infrastructure.utils.UuidUtils
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserInfoMapper {

    companion object {
        private const val DEFAULT_AUTHORIZATION_LEVEL = 0
        private const val MODERATOR_AUTHORIZATION_LEVEL = 42
        private const val ADMIN_AUTHORIZATION_LEVEL = 1337
        private const val DEFAULT_PASSWORD = ""

        private const val IS_BANNED_FALSE_VALUE = 0
    }

    fun toDomain(dto: UserDTO): UserInfo {
        return UserInfo(
            userId = dto.id.toString(),
            fcmToken = dto.fcmToken,
            isBanned = dto.isBanned == IS_BANNED_FALSE_VALUE,
            authorizationList = when (dto.authorizationLevel) {
                DEFAULT_AUTHORIZATION_LEVEL -> UserAuthorization.getUserAuthorizations()
                MODERATOR_AUTHORIZATION_LEVEL -> UserAuthorization.getModeratorAuthorizations()
                ADMIN_AUTHORIZATION_LEVEL -> UserAuthorization.geAdminAuthorizations()
                else -> emptyList()
            }
        )
    }

    fun generateDto(fcmToken: String): UserDTO {
        return UserDTO(
            id = UuidUtils.NOT_FOUND_UUID,
            password = DEFAULT_PASSWORD,
            fcmToken = fcmToken,
            createdDate = Date(),
            authorizationLevel = DEFAULT_AUTHORIZATION_LEVEL,
            isBanned = IS_BANNED_FALSE_VALUE,
        )
    }

    fun updateDto(dto: UserDTO, fcmToken: String): UserDTO {
        return dto.copy(fcmToken = fcmToken)
    }

}