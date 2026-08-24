package fr.gouv.agora.infrastructure.acme.stub

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

data class StubOrder(
    val id: String,
    val domain: String,
    val authzId: String,
    val challengeToken: String,
    var status: String = "pending",      // pending → ready → valid
    var certId: String? = null,
)

data class StubAuthz(
    val id: String,
    val orderId: String,
    val challengeToken: String,
    var status: String = "pending",
)

@Component
@ConditionalOnProperty(name = ["ACME_STUB_MODE"], havingValue = "true")
class AcmeStubOrderStore {
    val orders: ConcurrentHashMap<String, StubOrder> = ConcurrentHashMap()
    val authzs: ConcurrentHashMap<String, StubAuthz> = ConcurrentHashMap()
    // token → orderId (pour retrouver l'order depuis le challenge)
    val challengeToOrder: ConcurrentHashMap<String, String> = ConcurrentHashMap()
}
