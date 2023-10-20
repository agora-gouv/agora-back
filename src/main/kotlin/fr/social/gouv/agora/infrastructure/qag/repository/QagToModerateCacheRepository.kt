package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class QagToModerateCacheRepository(
    @Qualifier("shortTermCacheManager") private val cacheManager: CacheManager,
) {

    companion object {
        private const val QAGS_TO_MODERATE_CACHE_NAME = "qagToModerate"
        private const val QAGS_TO_MODERATE_KEY = "qagToModerateList"
    }

    sealed class CacheResult {
        data class CachedQagList(val qagToModerateList: List<QagDTO>) : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    fun initializeCache(qagToModerateList: List<QagDTO>) {
        getCache()?.put(QAGS_TO_MODERATE_KEY, qagToModerateList)
    }

    @Suppress("UNCHECKED_CAST")
    fun getQagToModerateList(): CacheResult {
        val qagToModerateList = try {
            getCache()?.get(QAGS_TO_MODERATE_CACHE_NAME, List::class.java) as? List<QagDTO>
        } catch (e: IllegalStateException) {
            null
        }

        return when (qagToModerateList) {
            null -> CacheResult.CacheNotInitialized
            else -> CacheResult.CachedQagList(qagToModerateList)
        }
    }

    private fun getCache() = cacheManager.getCache(QAGS_TO_MODERATE_CACHE_NAME)

}