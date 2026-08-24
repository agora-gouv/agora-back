package fr.gouv.agora.infrastructure.acme.stub

import fr.gouv.agora.config.AcmeConfig
import fr.gouv.agora.infrastructure.acme.repository.AcmeCryptoHelper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.shredzone.acme4j.util.KeyPairUtils
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.io.StringWriter
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@RestController
@Tag(
    name = "Stub — ACME",
    description = "Simulation locale du serveur ACME (RFC 8555) compatible acme4j — actif uniquement si ACME_STUB_MODE=true",
)
@ConditionalOnProperty(name = ["ACME_STUB_MODE"], havingValue = "true")
class AcmeStubController(
    private val acmeConfig: AcmeConfig,
    private val stubStore: AcmeStubStore,
    private val nonceStore: AcmeStubNonceStore,
    private val orderStore: AcmeStubOrderStore,
    private val certGenerator: AcmeStubCertificateGenerator,
    private val cryptoHelper: AcmeCryptoHelper,
) {
    private val logger = LoggerFactory.getLogger(AcmeStubController::class.java)

    // Cache des certificats générés (certId → GeneratedCertificate)
    private val generatedCerts: ConcurrentHashMap<String, GeneratedCertificate> = ConcurrentHashMap()

    private fun baseUrl() = "http://localhost:${acmeConfig.port}/stub/acme"

    // ──────────────────────────────────────────────────────────────────────────
    // KEYPAIR GENERATION (utilitaire pour les scripts de test)
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Génère une keypair EC P-256 compatible acme4j via KeyPairUtils,
     * la chiffre avec AcmeCryptoHelper et renvoie le résultat chiffré en base64.
     * Utilisé par les scripts de test pour garantir la compatibilité acme4j.
     */
    @Operation(summary = "[STUB] Générer une keypair EC acme4j-compatible (chiffrée)")
    @PostMapping("/stub/acme/generate-keypair")
    fun generateKeypair(): ResponseEntity<Map<String, String>> {
        val keyPair = KeyPairUtils.createKeyPair(2048)
        val writer = StringWriter()
        KeyPairUtils.writeKeyPair(keyPair, writer)
        val pemPkcs8 = writer.toString()
        val encrypted = cryptoHelper.encrypt(pemPkcs8)
        logger.info("[STUB] generate-keypair → keypair générée et chiffrée")
        return ResponseEntity.ok(mapOf("encryptedKeyPair" to encrypted))
    }

    // ──────────────────────────────────────────────────────────────────────────
    // DIRECTORY
    // ──────────────────────────────────────────────────────────────────────────

    @Operation(summary = "[STUB] Répertoire ACME (discovery endpoint RFC 8555)")
    @GetMapping("/stub/acme/directory")
    fun directory(): ResponseEntity<Map<String, Any>> {
        val base = baseUrl()
        logger.info("[STUB] ACME GET /directory")
        stubStore.record("GET", "/stub/acme/directory", "directory")

        val body = mapOf(
            "newNonce" to "$base/new-nonce",
            "newAccount" to "$base/new-account",
            "newOrder" to "$base/new-order",
            "revokeCert" to "$base/revoke-cert",
            "keyChange" to "$base/key-change",
            "meta" to mapOf(
                "termsOfService" to "$base/terms",
                "website" to "http://localhost:8080",
                "caaIdentities" to listOf("localhost"),
                "externalAccountRequired" to false,
            ),
        )
        return ResponseEntity.ok(body)
    }

    // ──────────────────────────────────────────────────────────────────────────
    // NONCE
    // ──────────────────────────────────────────────────────────────────────────

    @Operation(summary = "[STUB] Obtenir un nouveau Replay-Nonce (HEAD)")
    @RequestMapping("/stub/acme/new-nonce", method = [RequestMethod.HEAD])
    fun newNonceHead(): ResponseEntity<Void> {
        val nonce = nonceStore.generateNonce()
        logger.info("[STUB] ACME HEAD /new-nonce → $nonce")
        return ResponseEntity.noContent()
            .header("Replay-Nonce", nonce)
            .header("Cache-Control", "no-store")
            .build()
    }

    @Operation(summary = "[STUB] Obtenir un nouveau Replay-Nonce (GET)")
    @GetMapping("/stub/acme/new-nonce")
    fun newNonceGet(): ResponseEntity<Void> {
        val nonce = nonceStore.generateNonce()
        logger.info("[STUB] ACME GET /new-nonce → $nonce")
        return ResponseEntity.noContent()
            .header("Replay-Nonce", nonce)
            .header("Cache-Control", "no-store")
            .build()
    }

    // ──────────────────────────────────────────────────────────────────────────
    // ACCOUNT
    // ──────────────────────────────────────────────────────────────────────────

    @Operation(summary = "[STUB] Récupérer un compte ACME existant (utilisé par acme4j login + bindOrder)")
    @PostMapping("/stub/acme/account/{accountId}", consumes = ["application/jose+json", "application/json"])
    fun getAccount(@PathVariable accountId: String, @RequestBody body: String): ResponseEntity<Map<String, Any>> {
        val base = baseUrl()
        logger.info("[STUB] ACME POST /account/$accountId → returning existing account")
        val nonce = nonceStore.generateNonce()
        val responseBody = mapOf(
            "status" to "valid",
            "contact" to emptyList<String>(),
            "orders" to "$base/account/$accountId/orders",
        )
        return ResponseEntity(responseBody, HttpHeaders().apply { set("Replay-Nonce", nonce) }, HttpStatus.OK)
    }

    @Operation(summary = "[STUB] Créer ou récupérer un compte ACME")
    @PostMapping("/stub/acme/new-account", consumes = ["application/jose+json", "application/json"])
    fun newAccount(@RequestBody body: String): ResponseEntity<Map<String, Any>> {
        val accountId = "stub-account-001"
        val base = baseUrl()
        logger.info("[STUB] ACME POST /new-account → account $accountId")
        stubStore.record("POST", "/stub/acme/new-account", "newAccount id=$accountId")

        val nonce = nonceStore.generateNonce()
        val responseBody = mapOf(
            "status" to "valid",
            "contact" to emptyList<String>(),
            "orders" to "$base/account/$accountId/orders",
        )

        val headers = HttpHeaders().apply {
            set("Replay-Nonce", nonce)
            set("Location", "$base/account/$accountId")
        }
        return ResponseEntity(responseBody, headers, HttpStatus.CREATED)
    }

    // ──────────────────────────────────────────────────────────────────────────
    // ORDER
    // ──────────────────────────────────────────────────────────────────────────

    @Operation(summary = "[STUB] Créer un nouvel order ACME")
    @PostMapping("/stub/acme/new-order", consumes = ["application/jose+json", "application/json"])
    fun newOrder(@RequestBody body: String): ResponseEntity<Map<String, Any>> {
        val domain = acmeConfig.domain.ifBlank { "stub.local" }
        val orderId = UUID.randomUUID().toString().take(8)
        val authzId = UUID.randomUUID().toString().take(8)
        val token = UUID.randomUUID().toString().replace("-", "").take(22)
        val base = baseUrl()

        orderStore.orders[orderId] = StubOrder(
            id = orderId, domain = domain,
            authzId = authzId, challengeToken = token,
        )
        orderStore.authzs[authzId] = StubAuthz(
            id = authzId, orderId = orderId, challengeToken = token,
        )
        orderStore.challengeToOrder[token] = orderId

        logger.info("[STUB] ACME POST /new-order → orderId=$orderId authzId=$authzId token=$token")
        stubStore.record("POST", "/stub/acme/new-order", "newOrder orderId=$orderId domain=$domain")

        val nonce = nonceStore.generateNonce()
        val responseBody = mapOf(
            "status" to "pending",
            "identifiers" to listOf(mapOf("type" to "dns", "value" to domain)),
            "authorizations" to listOf("$base/authz/$authzId"),
            "finalize" to "$base/order/$orderId/finalize",
        )

        val headers = HttpHeaders().apply {
            set("Replay-Nonce", nonce)
            set("Location", "$base/order/$orderId")
        }
        return ResponseEntity(responseBody, headers, HttpStatus.CREATED)
    }

    @Operation(summary = "[STUB] Récupérer le statut d'un order")
    @PostMapping("/stub/acme/order/{orderId}", consumes = ["application/jose+json", "application/json"])
    fun getOrder(@PathVariable orderId: String, @RequestBody body: String): ResponseEntity<Map<String, Any>> {
        val base = baseUrl()
        val order = orderStore.orders[orderId]
        logger.info("[STUB] ACME POST /order/$orderId → status=${order?.status}")
        stubStore.record("POST", "/stub/acme/order/$orderId", "getOrder orderId=$orderId status=${order?.status}")
        val nonce = nonceStore.generateNonce()

        val responseBody: Map<String, Any> = if (order != null) {
            val fields = mutableMapOf<String, Any>(
                "status" to order.status,
                "identifiers" to listOf(mapOf("type" to "dns", "value" to order.domain)),
                "authorizations" to listOf("$base/authz/${order.authzId}"),
                "finalize" to "$base/order/${order.id}/finalize",
            )
            order.certId?.let { fields["certificate"] = "$base/certificate/$it" }
            fields
        } else {
            mapOf("status" to "invalid")
        }

        return ResponseEntity(responseBody, HttpHeaders().apply { set("Replay-Nonce", nonce) }, HttpStatus.OK)
    }

    @Operation(summary = "[STUB] Finaliser un order (soumettre le CSR)")
    @PostMapping("/stub/acme/order/{orderId}/finalize", consumes = ["application/jose+json", "application/json"])
    fun finalizeOrder(@PathVariable orderId: String, @RequestBody body: String): ResponseEntity<Map<String, Any>> {
        val base = baseUrl()
        val order = orderStore.orders[orderId]
        val domain = order?.domain ?: acmeConfig.domain.ifBlank { "stub.local" }

        logger.info("[STUB] ACME POST /order/$orderId/finalize")
        stubStore.record("POST", "/stub/acme/order/$orderId/finalize", "finalizeOrder orderId=$orderId")

        // Générer le certificat auto-signé
        val certId = UUID.randomUUID().toString().take(8)
        val generated = certGenerator.generate(domain)
        generatedCerts[certId] = generated
        order?.status = "valid"
        order?.certId = certId

        val nonce = nonceStore.generateNonce()
        val responseBody = mapOf(
            "status" to "valid",
            "certificate" to "$base/certificate/$certId",
        )

        return ResponseEntity(responseBody, HttpHeaders().apply { set("Replay-Nonce", nonce) }, HttpStatus.OK)
    }

    // ──────────────────────────────────────────────────────────────────────────
    // AUTHORIZATION
    // ──────────────────────────────────────────────────────────────────────────

    @Operation(summary = "[STUB] Récupérer ou mettre à jour une authorization ACME")
    @PostMapping("/stub/acme/authz/{authzId}", consumes = ["application/jose+json", "application/json"])
    fun getAuthz(@PathVariable authzId: String, @RequestBody body: String): ResponseEntity<Map<String, Any>> {
        val base = baseUrl()
        val authz = orderStore.authzs[authzId]
        val token = authz?.challengeToken ?: "stub-token-fallback"
        val status = authz?.status ?: "valid"
        val domain = acmeConfig.domain.ifBlank { "stub.local" }

        logger.info("[STUB] ACME POST /authz/$authzId → status=$status token=$token")
        stubStore.record("POST", "/stub/acme/authz/$authzId", "getAuthz authzId=$authzId status=$status")
        val nonce = nonceStore.generateNonce()

        val challengeStatus = if (status == "valid") "valid" else "pending"
        val responseBody = mapOf(
            "status" to status,
            "identifier" to mapOf("type" to "dns", "value" to domain),
            "challenges" to listOf(
                mapOf(
                    "type" to "http-01",
                    "url" to "$base/challenge/$token",
                    "token" to token,
                    "status" to challengeStatus,
                    "validated" to if (challengeStatus == "valid") "2026-01-01T00:00:00Z" else null,
                ).filterValues { it != null }
            ),
        )

        return ResponseEntity(responseBody, HttpHeaders().apply { set("Replay-Nonce", nonce) }, HttpStatus.OK)
    }

    // ──────────────────────────────────────────────────────────────────────────
    // CHALLENGE
    // ──────────────────────────────────────────────────────────────────────────

    @Operation(summary = "[STUB] Déclencher ou interroger un challenge HTTP-01")
    @PostMapping("/stub/acme/challenge/{token}", consumes = ["application/jose+json", "application/json"])
    fun challenge(@PathVariable token: String, @RequestBody body: String): ResponseEntity<Map<String, Any>> {
        logger.info("[STUB] ACME POST /challenge/$token → VALID (stub toujours valide)")
        stubStore.record("POST", "/stub/acme/challenge/$token", "challenge token=$token status=valid")

        // Marquer l'authz correspondante comme VALID
        val orderId = orderStore.challengeToOrder[token]
        if (orderId != null) {
            val order = orderStore.orders[orderId]
            order?.let { orderStore.authzs[it.authzId]?.status = "valid" }
        }

        val base = baseUrl()
        val nonce = nonceStore.generateNonce()
        val responseBody = mapOf(
            "type" to "http-01",
            "url" to "$base/challenge/$token",
            "status" to "valid",
            "token" to token,
            "validated" to "2026-01-01T00:00:00Z",
        )

        return ResponseEntity(responseBody, HttpHeaders().apply { set("Replay-Nonce", nonce) }, HttpStatus.OK)
    }

    // ──────────────────────────────────────────────────────────────────────────
    // CERTIFICATE
    // ──────────────────────────────────────────────────────────────────────────

    @Operation(summary = "[STUB] Télécharger le certificat émis")
    @PostMapping(
        "/stub/acme/certificate/{certId}",
        consumes = ["application/jose+json", "application/json"],
    )
    fun getCertificate(@PathVariable certId: String, @RequestBody body: String): ResponseEntity<ByteArray> {
        val generated = generatedCerts[certId]
        logger.info("[STUB] ACME POST /certificate/$certId → ${if (generated != null) "found" else "not found"}")
        stubStore.record("POST", "/stub/acme/certificate/$certId", "downloadCertificate certId=$certId")

        return if (generated != null) {
            ResponseEntity.ok()
                .header("Replay-Nonce", nonceStore.generateNonce())
                .header("Content-Type", "application/pem-certificate-chain")
                .body(generated.certPem.toByteArray(Charsets.UTF_8))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // TERMS (optionnel mais utile pour le health check)
    // ──────────────────────────────────────────────────────────────────────────

    @Operation(summary = "[STUB] Termes de service ACME (stub)")
    @GetMapping("/stub/acme/terms")
    fun terms(): ResponseEntity<String> =
        ResponseEntity.ok("Stub ACME Terms of Service — local test only")
}
