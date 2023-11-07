package fr.gouv.agora.infrastructure.login.repository

import fr.gouv.agora.domain.LoginRequest
import fr.gouv.agora.domain.SignupRequest
import fr.gouv.agora.infrastructure.login.dto.UserDataDTO
import fr.gouv.agora.infrastructure.utils.UuidUtils
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

@Component
class UserDataMapper {

    fun toDto(loginRequest: LoginRequest): UserDataDTO {
        return UserDataDTO(
            id = UuidUtils.NOT_FOUND_UUID,
            eventType = "login",
            eventDate = Date(),
            userId = loginRequest.userId,
            remoteAddressHash = hashRemoteAddress(loginRequest.remoteAddress),
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
            remoteAddressHash = hashRemoteAddress(signupRequest.remoteAddress),
            userAgent = signupRequest.userAgent,
            fcmToken = signupRequest.fcmToken,
            platform = signupRequest.platform,
            versionName = signupRequest.versionName,
            versionCode = signupRequest.versionCode,
        )
    }

    private fun hashRemoteAddress(remoteAddress: String): String {
        val iterations = System.getenv("REMOTE_ADDRESS_HASH_ITERATIONS").toIntOrNull()
            ?: throw Exception("Invalid remoteAddress hash iterations number")
        val keyLength = System.getenv("REMOTE_ADDRESS_HASH_KEY_LENGTH").toIntOrNull()
            ?: throw Exception("Invalid remoteAddress hash keyLength number")
        val salt = System.getenv("REMOTE_ADDRESS_HASH_SALT").toByteArray()
        val factory = SecretKeyFactory.getInstance(System.getenv("REMOTE_ADDRESS_SECRET_KEY_ALGORITHM"))

        val spec = PBEKeySpec(remoteAddress.toCharArray(), salt, iterations, keyLength)
        val key = factory.generateSecret(spec)

        return HexFormat.of().formatHex(key.encoded)
    }

}