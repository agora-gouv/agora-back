package fr.social.gouv.agora.infrastructure.login

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fr.social.gouv.agora.domain.LoginTokenData
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object LoginTokenGenerator {

    private const val CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding"

    fun buildLoginToken(loginTokenData: LoginTokenData): BuildResult {
        return try {
            val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, getCipherKey())

            val loginTokenDataJson = LoginTokenDataJson(userId = loginTokenData.userId)
            val byteArrayResult = cipher.doFinal(getObjectMapper().writeValueAsString(loginTokenDataJson).toByteArray())

            BuildResult.Success(loginToken = Base64.getEncoder().encodeToString(byteArrayResult))
        } catch (e: Exception) {
            println("Exception while buildLoginToken: $e")
            BuildResult.Failure
        }
    }

    fun decodeLoginToken(encryptedMessage: String): DecodeResult {
        return try {
            val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, getCipherKey())

            val byteArrayResult = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage))
            val loginTokenDataJson = getObjectMapper().readValue(byteArrayResult, LoginTokenDataJson::class.java)

            DecodeResult.Success(LoginTokenData(userId = loginTokenDataJson.userId))
        } catch (e: Exception) {
            println("Exception while decodeLoginToken: $e")
            DecodeResult.Failure
        }
    }

    private fun getCipherKey() = SecretKeySpec(Base64.getDecoder().decode(getBase64CipherKey()), "AES")
    private fun getBase64CipherKey() = System.getenv("LOGIN_TOKEN_SECRET") ?: ""

    private fun getObjectMapper() = jacksonObjectMapper()

}

@JsonIgnoreProperties(ignoreUnknown = true)
data class LoginTokenDataJson(
    @JsonProperty("userId")
    val userId: String,
)

sealed class BuildResult {
    object Failure : BuildResult()
    data class Success(val loginToken: String) : BuildResult()
}

sealed class DecodeResult {
    object Failure : DecodeResult()
    data class Success(val loginTokenData: LoginTokenData) : DecodeResult()
}