package fr.gouv.agora.infrastructure.participationCharter.repository

import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
class ParticipationCharterCacheRepository(
    private val cacheManager: CacheManager,
) {

    companion object {
        private const val PARTICIPATION_CHARTER_CACHE_NAME = "participationCharter"
        private const val LATEST_PARTICIPATION_CHARTER_TEXT_KEY = "latest"
    }

    fun getLatestParticipationCharterText(): String? {
        return getCache()?.get(LATEST_PARTICIPATION_CHARTER_TEXT_KEY, String::class.java)
    }

    fun initLatestParticipationCharterText(participationCharterText: String) {
        getCache()?.put(LATEST_PARTICIPATION_CHARTER_TEXT_KEY, participationCharterText)
    }

    private fun getCache() = cacheManager.getCache(PARTICIPATION_CHARTER_CACHE_NAME)

}