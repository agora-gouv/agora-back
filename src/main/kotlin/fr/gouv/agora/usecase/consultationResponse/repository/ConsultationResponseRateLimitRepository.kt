package fr.gouv.agora.usecase.consultationResponse.repository

interface ConsultationResponseRateLimitRepository {
    fun isIpRateLimited(ipAddressHash: String): Boolean
    fun incrementIpCount(ipAddressHash: String)
}
