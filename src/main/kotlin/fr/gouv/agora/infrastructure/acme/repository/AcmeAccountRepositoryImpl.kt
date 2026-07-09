package fr.gouv.agora.infrastructure.acme.repository

import fr.gouv.agora.domain.AcmeAccount
import fr.gouv.agora.usecase.acme.repository.AcmeAccountRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AcmeAccountRepositoryImpl(
    private val jpaRepository: AcmeAccountJpaRepository,
    private val cryptoHelper: AcmeCryptoHelper,
) : AcmeAccountRepository {

    override fun loadAccount(serverUrl: String): AcmeAccount? {
        val dao = jpaRepository.findFirstByServerUrlOrderByCreatedAtDesc(serverUrl) ?: return null
        return AcmeAccount(
            serverUrl = dao.serverUrl,
            accountUrl = dao.accountUrl,
            keyPem = cryptoHelper.decrypt(dao.keyPem),
        )
    }

    override fun saveAccount(account: AcmeAccount) {
        val encryptedKeyPem = cryptoHelper.encrypt(account.keyPem)
        val existing = jpaRepository.findFirstByServerUrlOrderByCreatedAtDesc(account.serverUrl)
        if (existing != null) {
            val updated = existing.copy(
                accountUrl = account.accountUrl,
                keyPem = encryptedKeyPem,
            )
            jpaRepository.save(updated)
        } else {
            val dao = AcmeAccountDAO(
                serverUrl = account.serverUrl,
                accountUrl = account.accountUrl,
                keyPem = encryptedKeyPem,
                createdAt = LocalDateTime.now(),
            )
            jpaRepository.save(dao)
        }
    }
}
