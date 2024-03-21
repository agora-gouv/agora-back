package fr.gouv.agora.infrastructure.login.repository

import fr.gouv.agora.domain.LoginRequest
import fr.gouv.agora.domain.SignupRequest
import fr.gouv.agora.infrastructure.login.dto.UserDataDTO
import fr.gouv.agora.infrastructure.utils.UuidUtils
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserDataMapper {

    fun toDto(loginRequest: LoginRequest): UserDataDTO {
        return UserDataDTO(
            id = UuidUtils.NOT_FOUND_UUID,
            eventType = "login",
            eventDate = Date(),
            userId = loginRequest.userId,
            ipAddressHash = loginRequest.ipAddressHash,
            userAgent = loginRequest.userAgent,
            fcmToken = loginRequest.fcmToken,
            platform = loginRequest.platform,
            versionName = loginRequest.versionName,
            versionCode = loginRequest.versionCode,
        )
    }

    fun toDto(signupRequest: SignupRequest, userId: String): UserDataDTO {
        return UserDataDTO(
            id = UuidUtils.NOT_FOUND_UUID,
            eventType = "signup",
            eventDate = Date(),
            userId = userId,
            ipAddressHash = signupRequest.ipAddressHash,
            userAgent = signupRequest.userAgent,
            fcmToken = signupRequest.fcmToken,
            platform = signupRequest.platform,
            versionName = signupRequest.versionName,
            versionCode = signupRequest.versionCode,
        )
    }

}