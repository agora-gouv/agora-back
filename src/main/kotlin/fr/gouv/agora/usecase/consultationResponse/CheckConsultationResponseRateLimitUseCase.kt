package fr.gouv.agora.usecase.consultationResponse

import fr.gouv.agora.usecase.consultationResponse.repository.ConsultationResponseRateLimitRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CheckConsultationResponseRateLimitUseCase(
    private val rateLimitRepository: ConsultationResponseRateLimitRepository,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Returns true if the IP is rate limited (request must be rejected with 429).
     * Increments the counter when not limited.
     */
    fun isRateLimited(ipAddressHash: String): Boolean {
        if (rateLimitRepository.isIpRateLimited(ipAddressHash)) {
            logger.warn("🚫 Consultation response rate limit reached for IP hash: $ipAddressHash")
            return true
        }
        rateLimitRepository.incrementIpCount(ipAddressHash)
        return false
    }
}
