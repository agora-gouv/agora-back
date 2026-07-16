package fr.gouv.agora.infrastructure.acme.stub

import fr.gouv.agora.config.AcmeConfig
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@Tag(name = "Stub — Cloudflare", description = "Simulation locale de l'API Cloudflare — actif uniquement si ACME_STUB_MODE=true")
@ConditionalOnProperty(name = ["ACME_STUB_MODE"], havingValue = "true")
class CloudflareStubController(
    private val acmeConfig: AcmeConfig,
    private val stubStore: AcmeStubStore,
) {
    private val logger = LoggerFactory.getLogger(CloudflareStubController::class.java)

    /**
     * Simule GET https://api.cloudflare.com/client/v4/zones/{zoneId}
     * Utilisé par CloudflareZoneCheckerImpl
     */
    @Operation(
        summary = "[STUB] Récupérer les informations d'une zone Cloudflare",
        description = "Simule GET /client/v4/zones/{zoneId}. Retourne une zone factice avec status=active.",
    )
    @GetMapping("/stub/cloudflare/zones/{zoneId}")
    fun getZone(@PathVariable zoneId: String): ResponseEntity<Map<String, Any>> {
        logger.info("[STUB] Cloudflare GET /zones/$zoneId")
        stubStore.record("GET", "/stub/cloudflare/zones/$zoneId", "getZone zoneId=$zoneId")

        val response = mapOf(
            "success" to true,
            "errors" to emptyList<Any>(),
            "result" to mapOf(
                "id" to zoneId,
                "name" to acmeConfig.domain.ifBlank { "stub.local" },
                "status" to "active",
                "plan" to mapOf("name" to "stub-enterprise"),
            ),
        )
        return ResponseEntity.ok(response)
    }

    /**
     * Simule POST https://api.cloudflare.com/client/v4/zones/{zoneId}/custom_certificates
     * Utilisé par CloudflareCertificateDeployerImpl
     */
    @Operation(
        summary = "[STUB] Déployer un certificat custom sur une zone Cloudflare",
        description = "Simule POST /client/v4/zones/{zoneId}/custom_certificates. " +
            "Enregistre le déploiement dans AcmeStubStore sans contacter Cloudflare.",
    )
    @PostMapping("/stub/cloudflare/zones/{zoneId}/custom_certificates")
    fun deployCertificate(
        @PathVariable zoneId: String,
        @RequestBody body: Map<String, Any>,
    ): ResponseEntity<Map<String, Any>> {
        val certPem = body["certificate"] as? String ?: ""
        val certPreview = certPem.lines()
            .firstOrNull { it.isNotBlank() && !it.startsWith("-----") }?.take(40) ?: "N/A"

        logger.info("[STUB] Cloudflare POST /zones/$zoneId/custom_certificates (cert preview: $certPreview...)")
        stubStore.record(
            "POST",
            "/stub/cloudflare/zones/$zoneId/custom_certificates",
            "deployCertificate zoneId=$zoneId certPreview=$certPreview",
        )
        stubStore.lastDeployedCertPreview = certPreview
        stubStore.lastDeployedAt = LocalDateTime.now()

        val response = mapOf(
            "success" to true,
            "errors" to emptyList<Any>(),
            "result" to mapOf(
                "id" to "stub-cert-deploy-${System.currentTimeMillis()}",
                "status" to "active",
                "bundle_method" to (body["bundle_method"] ?: "ubiquitous"),
            ),
        )
        return ResponseEntity.ok(response)
    }
}
