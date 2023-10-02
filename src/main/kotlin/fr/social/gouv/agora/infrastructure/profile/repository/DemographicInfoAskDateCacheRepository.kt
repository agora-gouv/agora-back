package fr.social.gouv.agora.infrastructure.profile.repository

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

@Repository
class DemographicInfoAskDateCacheRepository(
    @Qualifier("shortTermCacheManager") private val cacheManager: CacheManager,
    private val dateMapper: DateMapper,
) {
    companion object {
        private const val DEMOGRAPHIC_INFO_ASK_DATE_CACHE_NAME = "demographicInfoAskDate"
    }

    fun getDate(userId: UUID): LocalDate? {
        return try {
            getCache()?.get(userId.toString(), String::class.java)?.let { dateMapper.toLocalDate(it) }
        } catch (e: IllegalStateException) {
            null
        }
    }

    fun insertDate(userId: UUID, askDate: LocalDate) {
        getCache()?.put(userId.toString(), askDate.toString())
    }

    fun deleteDate(userId: UUID) {
        getCache()?.evict(userId.toString())
    }

    private fun getCache() = cacheManager.getCache(DEMOGRAPHIC_INFO_ASK_DATE_CACHE_NAME)
}