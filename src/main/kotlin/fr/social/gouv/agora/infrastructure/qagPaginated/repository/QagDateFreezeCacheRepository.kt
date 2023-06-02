package fr.social.gouv.agora.infrastructure.qagPaginated.repository

import fr.social.gouv.agora.infrastructure.profile.repository.DateMapper
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class QagDateFreezeCacheRepository(
    private val cacheManager: CacheManager,
    private val dateMapper: DateMapper,
) {

    companion object {
        private const val PAGINATED_QAG_DATE_FREEZE_CACHE_NAME = "qagDateFreezeCache"
    }

    fun getQagDateFreeze(userId: String, thematiqueId: String?): LocalDateTime? {
        return try {
            getCache()?.get(buildCacheKey(userId = userId, thematiqueId = thematiqueId), String::class.java)
                ?.let(dateMapper::toLocalDateTime)
        } catch (e: IllegalStateException) {
            null
        }
    }

    fun putQagDateFreeze(userId: String, thematiqueId: String?, date: LocalDateTime) {
        getCache()?.put(buildCacheKey(userId = userId, thematiqueId = thematiqueId), date.toString())
    }

    private fun getCache() = cacheManager.getCache(PAGINATED_QAG_DATE_FREEZE_CACHE_NAME)
    private fun buildCacheKey(userId: String, thematiqueId: String?) = "$userId/$thematiqueId"

}