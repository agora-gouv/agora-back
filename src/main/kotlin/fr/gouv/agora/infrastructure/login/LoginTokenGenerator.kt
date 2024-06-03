package fr.gouv.agora.infrastructure.login

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fr.gouv.agora.domain.LoginTokenData
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@Component
class LoginTokenGenerator {
    private val logger: Logger = LoggerFactory.getLogger(LoginTokenGenerator::class.java)

    fun buildLoginToken(loginTokenData: LoginTokenData): BuildResult {
        return try {
            val cipher = Cipher.getInstance(System.getenv("LOGIN_TOKEN_ENCODE_TRANSFORMATION"))
            cipher.init(Cipher.ENCRYPT_MODE, getEncodeCipherKey())

            val loginTokenDataJson = LoginTokenDataJson(userId = loginTokenData.userId)
            val byteArrayResult = cipher.doFinal(getObjectMapper().writeValueAsString(loginTokenDataJson).toByteArray())

            BuildResult.Success(loginToken = Base64.getEncoder().encodeToString(byteArrayResult))
        } catch (e: Exception) {
            logger.error("Exception while buildLoginToken: $e")
            BuildResult.Failure
        }
    }

    fun decodeLoginToken(encryptedMessage: String): DecodeResult {
        return try {
            val cipher = Cipher.getInstance(System.getenv("LOGIN_TOKEN_DECODE_TRANSFORMATION"))
            cipher.init(Cipher.DECRYPT_MODE, getDecodeCipherKey())

            val byteArrayResult = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage))
            val loginTokenDataJson = getObjectMapper().readValue(byteArrayResult, LoginTokenDataJson::class.java)

            DecodeResult.Success(LoginTokenData(userId = loginTokenDataJson.userId))
        } catch (e: Exception) {
            logger.error("Exception while decodeLoginToken: $e")
            DecodeResult.Failure
        }
    }

    private fun getEncodeCipherKey() =
        SecretKeySpec(Base64.getDecoder().decode(getBase64EncodeSecret()), getEncodeAlgorithm())

    private fun getBase64EncodeSecret() = System.getenv("LOGIN_TOKEN_ENCODE_SECRET") ?: ""
    private fun getEncodeAlgorithm() = System.getenv("LOGIN_TOKEN_ENCODE_ALGORITHM") ?: ""

    private fun getDecodeCipherKey() =
        SecretKeySpec(Base64.getDecoder().decode(getBase64DecodeSecret()), getDecodeAlgorithm())

    private fun getBase64DecodeSecret() = System.getenv("LOGIN_TOKEN_DECODE_SECRET") ?: ""
    private fun getDecodeAlgorithm() = System.getenv("LOGIN_TOKEN_DECODE_ALGORITHM") ?: ""

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
