package fr.gouv.agora.infrastructure.acme.stub

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.util.Base64
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

@Component
@ConditionalOnProperty(name = ["ACME_STUB_MODE"], havingValue = "true")
class AcmeStubNonceStore {

    // Nonces générés mais pas encore consommés
    private val availableNonces: MutableSet<String> = Collections.newSetFromMap(ConcurrentHashMap())

    fun generateNonce(): String {
        val bytes = ByteArray(16).also { java.security.SecureRandom().nextBytes(it) }
        val nonce = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
        availableNonces += nonce
        return nonce
    }

    /** Pour les stubs on accepte n'importe quel nonce (simplification) */
    fun isValid(nonce: String): Boolean = true
}
