package fr.social.gouv.agora.infrastructure.profile.repository

import org.springframework.cache.CacheManager
import org.springframework.cache.set
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

@Repository
class DemographicInfoAskDateCacheRepository(private val cacheManager: CacheManager, private val dateMapper: DateMapper) {
    companion object {
        private const val DEMOGRAPHIC_INFO_ASK_DATE_CACHE_NAME = "demoInfoAskDateCache"
    }

    fun getDate(userUUID: UUID): LocalDate? {
        return try {
            getCache()?.get(userUUID.toString(), String::class.java)?.let { dateMapper.toLocalDate(it) }
        } catch (e: IllegalStateException) {
            null
        }
    }

    fun insertDate(userUUID: UUID) {
        getCache()?.put(userUUID.toString(), LocalDate.now().toString())
    }

    fun updateDate(userUUID: UUID) {
        getCache()?.set(userUUID.toString(), LocalDate.now().toString())
    }

    fun deleteDate(userId: UUID) {
        //TODO doit etre appeler quand un user rentre ses donnees demographiques
        getCache()?.evict(userId.toString())
    }

    private fun getCache() = cacheManager.getCache(DEMOGRAPHIC_INFO_ASK_DATE_CACHE_NAME)
}