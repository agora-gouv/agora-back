package fr.gouv.agora.infrastructure.suspiciousUser.repository

import fr.gouv.agora.usecase.suspiciousUser.repository.SignupCountRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class SignupCountCacheRepository(
    @Qualifier("longTermCacheManager")
    private val cacheManager: CacheManager,
) : SignupCountRepository {

    companion object {
        private const val SIGNUP_COUNT_CACHE_NAME = "signupCount"
    }

    override fun getTodaySignupCount(ipAddressHash: String): Int? {
        return getCache()?.get(ipAddressHash, Int::class.java)
    }

    override fun initTodaySignupCount(ipAddressHash: String, todaySignupCount: Int) {
        getCache()?.put(ipAddressHash, todaySignupCount)
    }

    private fun getCache() = cacheManager.getCache(SIGNUP_COUNT_CACHE_NAME)
}