package fr.gouv.agora.infrastructure.acme

import fr.gouv.agora.usecase.acme.AcmeCertificateRenewalUseCase
import fr.gouv.agora.usecase.acme.AcmeCertificateUploadUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@Tag(name = "Admin")
class AcmeAdminController(
    private val acmeCertificateRenewalUseCase: AcmeCertificateRenewalUseCase,
    private val acmeCertificateUploadUseCase: AcmeCertificateUploadUseCase,
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
}

data class AcmeCertificateUploadJson(
    val certificatePem: String,
    val privateKeyPem: String,
    val expiresAt: LocalDateTime,
)
