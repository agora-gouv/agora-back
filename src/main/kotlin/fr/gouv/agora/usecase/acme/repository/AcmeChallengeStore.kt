package fr.gouv.agora.usecase.acme.repository

interface AcmeChallengeStore {
    fun storeChallenge(token: String, keyAuthorization: String)
    fun getChallenge(token: String): String?  // null si token inconnu ou expiré
    fun clearChallenge(token: String)
}
