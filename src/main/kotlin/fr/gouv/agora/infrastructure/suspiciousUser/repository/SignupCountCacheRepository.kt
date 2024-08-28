package fr.gouv.agora.infrastructure.suspiciousUser.repository

import fr.gouv.agora.usecase.suspiciousUser.repository.SignupCountRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
class SignupCountCacheRepository(
    @Qualifier("longTermCacheManager")
    private val cacheManager: CacheManager,
) : SignupCountRepository {

    companion object {
        private const val SIGNUP_COUNT_CACHE_NAME = "signupCount"
    }

    override fun getTodaySignupCount(ipAddressHash: String, userAgent: String): Int? {
        return try {
            getCache()?.get("$ipAddressHash/$userAgent")?.get() as? Int
        } catch (e: Exception) {
            null
        }
    }

    override fun initTodaySignupCount(ipAddressHash: String, userAgent: String, todaySignupCount: Int) {
        getCache()?.put("$ipAddressHash/$userAgent", todaySignupCount)
    }

    private fun getCache() = cacheManager.getCache(SIGNUP_COUNT_CACHE_NAME)
}
