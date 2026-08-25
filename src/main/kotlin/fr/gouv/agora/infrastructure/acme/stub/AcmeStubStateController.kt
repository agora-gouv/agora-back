package fr.gouv.agora.infrastructure.acme.stub

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Stub — État", description = "Inspection de l'état des bouchons ACME/Cloudflare — actif uniquement si ACME_STUB_MODE=true")
@ConditionalOnProperty(name = ["ACME_STUB_MODE"], havingValue = "true")
class AcmeStubStateController(private val stubStore: AcmeStubStore) {

    @Operation(
        summary = "[STUB] Lister tous les appels reçus par les bouchons",
        description = "Retourne l'historique de tous les appels HTTP reçus par /stub/acme/** et /stub/cloudflare/**.",
    )
    @GetMapping("/admin/stub/acme/calls")
    fun getCalls(): ResponseEntity<List<AcmeStubStore.StubCall>> =
        ResponseEntity.ok(stubStore.calls.toList())

    @Operation(
        summary = "[STUB] Réinitialiser l'historique des bouchons",
        description = "Vide l'historique des appels et le dernier déploiement Cloudflare enregistré.",
    )
    @DeleteMapping("/admin/stub/acme/calls/reset")
    fun resetCalls(): ResponseEntity<String> {
        stubStore.reset()
        return ResponseEntity.ok("Historique des bouchons réinitialisé")
    }

    @Operation(
        summary = "[STUB] Dernier certificat 'déployé' sur Cloudflare",
        description = "Retourne un aperçu du dernier certificat reçu par le stub Cloudflare.",
    )
    @GetMapping("/admin/stub/acme/last-deploy")
    fun getLastDeploy(): ResponseEntity<Map<String, Any?>> =
        ResponseEntity.ok(
            mapOf(
                "lastDeployedCertPreview" to stubStore.lastDeployedCertPreview,
                "lastDeployedAt" to stubStore.lastDeployedAt,
            )
        )
}
