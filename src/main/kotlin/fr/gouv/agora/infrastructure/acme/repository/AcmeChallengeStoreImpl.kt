package fr.gouv.agora.infrastructure.acme.repository

import fr.gouv.agora.usecase.acme.repository.AcmeChallengeStore
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AcmeChallengeStoreImpl(
    private val jpaRepository: AcmeChallengeJpaRepository,
) : AcmeChallengeStore {

    override fun storeChallenge(token: String, keyAuthorization: String) {
        val dao = AcmeChallengeDAO(
            token = token,
            keyAuthorization = keyAuthorization,
            createdAt = LocalDateTime.now(),
        )
        jpaRepository.save(dao)
    }

    override fun getChallenge(token: String): String? =
        jpaRepository.findById(token).orElse(null)?.keyAuthorization

    override fun clearChallenge(token: String) {
        jpaRepository.deleteById(token)
    }
}
