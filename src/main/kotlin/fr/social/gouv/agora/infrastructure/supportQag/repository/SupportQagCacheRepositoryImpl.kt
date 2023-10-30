package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagCacheRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component
import java.util.*

@Component
@Suppress("unused")
class SupportQagCacheRepositoryImpl(
    @Qualifier("shortTermCacheManager") private val cacheManager: CacheManager,
) : SupportQagCacheRepository {

    companion object {
        const val SUPPORT_QAG_CACHE_NAME = "userSupportQags"
    }

    @Suppress("UNCHECKED_CAST")
    override fun getUserSupportedQagIds(userId: String): List<String>? {
        return try {
            getShortTermCache()?.get(userId, List::class.java) as? List<String>
        } catch (e: IllegalStateException) {
            null
        }
    }

    override fun initUserSupportedQagIds(userId: String, userSupportedQagIds: List<String>) {
        getShortTermCache()?.put(userId, userSupportedQagIds)
    }

    override fun addSupportedQagIds(userId: String, qagId: String) {
        getUserSupportedQagIds(userId)?.let { userSupportedQagIds ->
            initUserSupportedQagIds(userId, userSupportedQagIds + qagId)
        }
    }

    override fun removeSupportedQagIds(userId: String, qagId: String) {
        getUserSupportedQagIds(userId)?.let { userSupportedQagIds ->
            val filteredQagIds = userSupportedQagIds.filter { supportedQagId -> supportedQagId != qagId }
            if (filteredQagIds.size < userSupportedQagIds.size) {
                initUserSupportedQagIds(userId, filteredQagIds)
            }
        }
    }

    private fun getShortTermCache() = cacheManager.getCache(SUPPORT_QAG_CACHE_NAME)

}