package fr.gouv.agora.usecase.acme.repository

import fr.gouv.agora.domain.AcmeAccount

interface AcmeAccountRepository {
    fun loadAccount(serverUrl: String): AcmeAccount?  // null si pas encore de compte créé pour cette CA
    fun saveAccount(account: AcmeAccount)
}
