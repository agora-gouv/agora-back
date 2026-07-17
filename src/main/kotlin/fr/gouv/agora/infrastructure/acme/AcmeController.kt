package fr.gouv.agora.infrastructure.acme

import fr.gouv.agora.usecase.acme.repository.AcmeChallengeStore
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class AcmeController(private val challengeStore: AcmeChallengeStore) {

    @GetMapping("/.well-known/acme-challenge/{token}")
    fun getChallenge(@PathVariable token: String): ResponseEntity<String> {
        val keyAuthorization = challengeStore.getChallenge(token)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(keyAuthorization)
    }
}
