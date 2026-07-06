package fr.gouv.agora.infrastructure.acme.repository

import fr.gouv.agora.usecase.acme.repository.AcmeChallengeStore
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class AcmeChallengeStoreImpl : AcmeChallengeStore {

    private val store = ConcurrentHashMap<String, String>()

    override fun storeChallenge(token: String, keyAuthorization: String) {
        store[token] = keyAuthorization
    }

    override fun getChallenge(token: String): String? = store[token]

    override fun clearChallenge(token: String) {
        store.remove(token)
    }
}
