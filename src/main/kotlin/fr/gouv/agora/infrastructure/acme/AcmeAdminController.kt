package fr.gouv.agora.infrastructure.acme

import fr.gouv.agora.usecase.acme.AcmeCertificateRenewalUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Admin")
class AcmeAdminController(
    private val acmeCertificateRenewalUseCase: AcmeCertificateRenewalUseCase,
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
}
