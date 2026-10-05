package fr.gouv.agora.infrastructure.consultationResponse.repository

import fr.gouv.agora.usecase.consultationResponse.repository.ConsultationResponseRateLimitRepository
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository

@Repository
class ConsultationResponseRateLimitRepositoryImpl(
    private val cacheManager: CacheManager,
) : ConsultationResponseRateLimitRepository {

    companion object {
        private const val CACHE_NAME = "consultationResponseRateLimit"
        private const val DEFAULT_MAX_SUBMISSIONS = 20

        private fun maxSubmissions(): Int =
            System.getenv("CONSULTATION_RESPONSE_RATE_LIMIT_PER_HOUR")?.toIntOrNull()
                ?: DEFAULT_MAX_SUBMISSIONS
    }

    override fun isIpRateLimited(ipAddressHash: String): Boolean {
        val max = maxSubmissions()
        if (max == 0) return false // 0 = rate limiting désactivé
        return getCurrentCount(ipAddressHash) >= max
    }

    override fun incrementIpCount(ipAddressHash: String) {
        val newCount = getCurrentCount(ipAddressHash) + 1
        getCache()?.put(ipAddressHash, newCount)
    }

    private fun getCurrentCount(ipAddressHash: String): Int {
        return try {
            getCache()?.get(ipAddressHash)?.get() as? Int ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun getCache() = cacheManager.getCache(CACHE_NAME)
}
