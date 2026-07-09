package fr.gouv.agora.infrastructure.acme

import fr.gouv.agora.usecase.acme.AcmeCertificateRenewalUseCase
import fr.gouv.agora.usecase.acme.AcmeCertificateUploadUseCase
import fr.gouv.agora.usecase.acme.AcmeServerHealthCheckResult
import fr.gouv.agora.usecase.acme.AcmeServerHealthCheckUseCase
import fr.gouv.agora.usecase.acme.CloudflareHealthCheckResult
import fr.gouv.agora.usecase.acme.CloudflareHealthCheckUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@Tag(name = "Admin")
class AcmeAdminController(
    private val acmeCertificateRenewalUseCase: AcmeCertificateRenewalUseCase,
    private val acmeCertificateUploadUseCase: AcmeCertificateUploadUseCase,
    private val cloudflareHealthCheckUseCase: CloudflareHealthCheckUseCase,
    private val acmeServerHealthCheckUseCase: AcmeServerHealthCheckUseCase,
) {

    @Operation(
        summary = "Déclencher le renouvellement du certificat ACME",
        responses = [
            ApiResponse(responseCode = "200", description = "Traitement de renouvellement exécuté avec succès"),
            ApiResponse(responseCode = "401", description = "Unauthorized : droits administrateur requis"),
        ]
    )
    @PostMapping("/admin/acme/renew-certificate")
    fun renewCertificate(): ResponseEntity<String> {
        acmeCertificateRenewalUseCase.renewIfNeeded()
        return ResponseEntity.ok("Traitement de renouvellement du certificat ACME exécuté avec succès")
    }

    @Operation(
        summary = "Uploader manuellement un certificat TLS (statut TO_DEPLOY)",
        description = "Enregistre le certificat et la clé privée en base avec le statut TO_DEPLOY. " +
            "Le prochain appel à renewIfNeeded() déploiera ce certificat sur Cloudflare sans contacter le serveur ACME.",
        responses = [
            ApiResponse(responseCode = "200", description = "Certificat enregistré avec succès"),
            ApiResponse(responseCode = "400", description = "Corps de la requête invalide"),
            ApiResponse(responseCode = "401", description = "Unauthorized : droits administrateur requis"),
        ]
    )
    @PostMapping("/admin/acme/upload-certificate")
    fun uploadCertificate(@RequestBody body: AcmeCertificateUploadJson): ResponseEntity<String> {
        acmeCertificateUploadUseCase.uploadCertificate(
            certificatePem = body.certificatePem,
            privateKeyPem = body.privateKeyPem,
            expiresAt = body.expiresAt,
        )
        return ResponseEntity.ok("Certificat enregistré avec succès avec le statut TO_DEPLOY")
    }

    @Operation(
        summary = "Vérifier le paramétrage Cloudflare (health check zone)",
        description = "Interroge l'API Cloudflare pour récupérer les informations de la zone configurée. " +
            "Permet de vérifier que le CLOUDFLARE_ZONE_ID et le CLOUDFLARE_API_TOKEN sont correctement configurés.",
        responses = [
            ApiResponse(responseCode = "200", description = "Informations de la zone Cloudflare récupérées avec succès"),
            ApiResponse(responseCode = "401", description = "Unauthorized : droits administrateur requis"),
            ApiResponse(responseCode = "503", description = "Erreur lors de l'appel à l'API Cloudflare"),
        ]
    )
    @GetMapping("/admin/acme/cloudflare/zone-info")
    fun getCloudflareZoneInfo(): ResponseEntity<Any> {
        return when (val result = cloudflareHealthCheckUseCase.getZoneInfo()) {
            is CloudflareHealthCheckResult.ZoneInfo -> ResponseEntity.ok(
                CloudflareZoneInfoJson(
                    zoneId = result.info.zoneId,
                    name = result.info.name,
                    status = result.info.status,
                    plan = result.info.plan,
                )
            )
            is CloudflareHealthCheckResult.Disabled -> ResponseEntity.ok(
                mapOf("message" to result.message)
            )
        }
    }

    @Operation(
        summary = "Vérifier la configuration du serveur ACME (health check répertoire)",
        description = "Interroge le répertoire de découverte du serveur ACME configuré (RFC 8555). " +
            "Permet de vérifier que ACME_SERVER_URL est correct et que le serveur ACME est joignable.",
        responses = [
            ApiResponse(responseCode = "200", description = "Informations du répertoire ACME récupérées avec succès"),
            ApiResponse(responseCode = "401", description = "Unauthorized : droits administrateur requis"),
            ApiResponse(responseCode = "503", description = "Erreur lors de l'appel au serveur ACME"),
        ]
    )
    @GetMapping("/admin/acme/server/directory")
    fun getAcmeServerDirectory(): ResponseEntity<Any> {
        return when (val result = acmeServerHealthCheckUseCase.getDirectoryInfo()) {
            is AcmeServerHealthCheckResult.DirectoryInfo -> ResponseEntity.ok(
                AcmeServerDirectoryJson(
                    serverUrl = result.info.serverUrl,
                    newAccountUrl = result.info.newAccountUrl,
                    newOrderUrl = result.info.newOrderUrl,
                    termsOfServiceUrl = result.info.termsOfServiceUrl,
                )
            )
            is AcmeServerHealthCheckResult.Disabled -> ResponseEntity.ok(
                mapOf("message" to result.message)
            )
        }
    }
}

data class AcmeCertificateUploadJson(
    val certificatePem: String,
    val privateKeyPem: String,
    val expiresAt: LocalDateTime,
)

data class CloudflareZoneInfoJson(
    val zoneId: String,
    val name: String,
    val status: String,
    val plan: String,
)

data class AcmeServerDirectoryJson(
    val serverUrl: String,
    val newAccountUrl: String,
    val newOrderUrl: String,
    val termsOfServiceUrl: String?,
)
