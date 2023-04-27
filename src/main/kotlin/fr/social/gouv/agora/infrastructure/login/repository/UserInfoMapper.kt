package fr.social.gouv.agora.infrastructure.login.repository

import fr.social.gouv.agora.domain.UserInfo
import fr.social.gouv.agora.infrastructure.login.dto.UserDTO
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserInfoMapper {

    companion object {
        private const val DEFAULT_AUTHORIZATION_LEVEL = 0
        private const val DEFAULT_PASSWORD = ""

        private const val IS_BANNED_FALSE_VALUE = 0
    }

    fun toDomain(dto: UserDTO): UserInfo {
        return UserInfo(userId = dto.id.toString())
    }

    fun generateDto(deviceId: String, fcmToken: String): UserDTO {
        return UserDTO(
            id = UUID.randomUUID(),
            deviceId = deviceId,
            password = DEFAULT_PASSWORD,
            fcmToken = fcmToken,
            createdDate = Date(),
            authorizationLevel = DEFAULT_AUTHORIZATION_LEVEL,
            isBanned = IS_BANNED_FALSE_VALUE,
        )
    }

}