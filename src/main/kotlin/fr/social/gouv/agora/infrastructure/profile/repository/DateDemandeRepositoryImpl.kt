package fr.social.gouv.agora.infrastructure.profile.repository

import fr.social.gouv.agora.usecase.profile.repository.DateDemandeRepository
import org.springframework.cache.CacheManager
import org.springframework.cache.set
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

@Repository
class DateDemandeRepositoryImpl(private val cacheManager: CacheManager) : DateDemandeRepository {
    companion object {
        private const val DATE_DEMANDE_CACHE_NAME = "dateDemandeCache"
    }

    override fun getDate(userUUID: UUID): String? {
        return try {
            getCache()?.get(userUUID.toString(), String::class.java)
        } catch (e: IllegalStateException) {
            null
        }
    }

    override fun insertDate(userUUID: UUID) {
        getCache()?.put(userUUID.toString(), LocalDate.now().toString())
    }

    override fun updateDate(userUUID: UUID) {
        getCache()?.set(userUUID.toString(), LocalDate.now().toString())
    }

    override fun deleteDate(userId: UUID) {
        getCache()?.evict(userId.toString())
    }

    private fun getCache() = cacheManager.getCache(DATE_DEMANDE_CACHE_NAME)
}