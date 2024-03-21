package fr.gouv.agora.infrastructure.utils

import jakarta.servlet.http.HttpServletRequest
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object IpAddressUtils {

    fun retrieveIpAddressHash(request: HttpServletRequest): String {
        return hash(retrieveIpAddress(request))
    }

    private fun retrieveIpAddress(request: HttpServletRequest): String {
        return (
                request.getHeader("X-Forwarded-For")?.takeIf { it.isNotBlank() }
                    ?.let { addresses -> addresses.split(",").firstOrNull { address -> address.trim().isNotBlank() } }
                    ?: request.getHeader("X-Remote-Address")?.takeIf { it.isNotBlank() }
                    ?: request.remoteAddr
                ).trim()
    }

    private fun hash(text: String): String {
        val iterations = System.getenv("REMOTE_ADDRESS_HASH_ITERATIONS").toIntOrNull()
            ?: throw Exception("Invalid remoteAddress hash iterations number")
        val keyLength = System.getenv("REMOTE_ADDRESS_HASH_KEY_LENGTH").toIntOrNull()
            ?: throw Exception("Invalid remoteAddress hash keyLength number")
        val salt = System.getenv("REMOTE_ADDRESS_HASH_SALT").toByteArray()
        val factory = SecretKeyFactory.getInstance(System.getenv("REMOTE_ADDRESS_SECRET_KEY_ALGORITHM"))

        val spec = PBEKeySpec(text.toCharArray(), salt, iterations, keyLength)
        val key = factory.generateSecret(spec)

        return HexFormat.of().formatHex(key.encoded)
    }

}