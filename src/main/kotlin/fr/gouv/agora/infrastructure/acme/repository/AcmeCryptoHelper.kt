package fr.gouv.agora.infrastructure.acme.repository

import fr.gouv.agora.config.AcmeConfig
import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

@Component
class AcmeCryptoHelper(private val acmeConfig: AcmeConfig) {

    companion object {
        private const val ALGORITHM = "AES/GCM/NoPadding"
        private const val KEY_ALGORITHM = "AES"
        private const val GCM_IV_LENGTH = 12   // 96 bits recommandé pour GCM
        private const val GCM_TAG_LENGTH = 128 // 128 bits
    }

    /**
     * Chiffre le texte en clair avec AES-256-GCM.
     * Le résultat est encodé en base64 : [IV (12 bytes) | ciphertext + tag (16 bytes)].
     * ⚠️ Ne jamais logger la valeur retournée.
     */
    fun encrypt(plainText: String): String {
        val key = resolveKey()
        val iv = ByteArray(GCM_IV_LENGTH).also { SecureRandom().nextBytes(it) }
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(GCM_TAG_LENGTH, iv))
        val cipherText = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        val combined = iv + cipherText
        return Base64.getEncoder().encodeToString(combined)
    }

    /**
     * Déchiffre une valeur chiffrée avec [encrypt].
     * ⚠️ Ne jamais logger la valeur retournée.
     */
    fun decrypt(cipherTextBase64: String): String {
        val key = resolveKey()
        val combined = Base64.getDecoder().decode(cipherTextBase64)
        val iv = combined.copyOfRange(0, GCM_IV_LENGTH)
        val cipherText = combined.copyOfRange(GCM_IV_LENGTH, combined.size)
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(GCM_TAG_LENGTH, iv))
        return String(cipher.doFinal(cipherText), Charsets.UTF_8)
    }

    private fun resolveKey(): SecretKeySpec {
        val encryptionKey = acmeConfig.encryptionKey
        if (encryptionKey.isBlank()) {
            throw IllegalStateException("ACME_ENCRYPTION_KEY is not configured. Cannot encrypt/decrypt ACME private keys.")
        }
        val keyBytes = try {
            Base64.getDecoder().decode(encryptionKey)
        } catch (e: IllegalArgumentException) {
            throw IllegalStateException("ACME_ENCRYPTION_KEY is not a valid base64 string.", e)
        }
        if (keyBytes.size != 32) {
            throw IllegalStateException("ACME_ENCRYPTION_KEY must be exactly 32 bytes (256 bits) when decoded. Got ${keyBytes.size} bytes.")
        }
        return SecretKeySpec(keyBytes, KEY_ALGORITHM)
    }
}
