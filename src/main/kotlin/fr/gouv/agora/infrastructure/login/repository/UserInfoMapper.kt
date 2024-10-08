package fr.gouv.agora.infrastructure.login.repository

import fr.gouv.agora.domain.LoginRequest
import fr.gouv.agora.domain.SignupRequest
import fr.gouv.agora.domain.UserAuthorization
import fr.gouv.agora.domain.UserInfo
import fr.gouv.agora.infrastructure.login.dto.UserDTO
import fr.gouv.agora.infrastructure.login.repository.AuthorizationLevel.*
import fr.gouv.agora.infrastructure.utils.UuidUtils
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserInfoMapper {

    companion object {
        private const val DEFAULT_PASSWORD = ""

        private const val IS_BANNED_FALSE_VALUE = 0
    }

    fun toDomain(dto: UserDTO): UserInfo {
        return UserInfo(
            userId = dto.id.toString(),
            fcmToken = dto.fcmToken,
            isBanned = dto.userIsBanned != IS_BANNED_FALSE_VALUE,
            authorizationList = when (dto.authorizationLevel) {
                DEFAULT_AUTHORIZATION_LEVEL.value -> UserAuthorization.getUserAuthorizations()
                MODERATOR_AUTHORIZATION_LEVEL.value -> UserAuthorization.getModeratorAuthorizations()
                PUBLISHER_AUTHORIZATION_LEVEL.value -> UserAuthorization.getPublisherAuthorizations()
                ADMIN_AUTHORIZATION_LEVEL.value -> UserAuthorization.geAdminAuthorizations()
                else -> emptyList()
            }
        )
    }

    fun generateDto(signupRequest: SignupRequest): UserDTO {
        return UserDTO(
            id = UuidUtils.NOT_FOUND_UUID,
            password = DEFAULT_PASSWORD,
            fcmToken = signupRequest.fcmToken,
            createdDate = Date(),
            authorizationLevel = DEFAULT_AUTHORIZATION_LEVEL.value,
            userIsBanned = IS_BANNED_FALSE_VALUE,
            lastConnectionDate = Date(),
        )
    }

    fun updateDto(dto: UserDTO, loginRequest: LoginRequest): UserDTO {
        return dto.copy(
            fcmToken = loginRequest.fcmToken,
            lastConnectionDate = Date(),
        )
    }

}
