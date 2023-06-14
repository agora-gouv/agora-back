package fr.social.gouv.agora.infrastructure.qag.repository

import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class QagModeratingLockCacheRepository(
    private val cacheManager: CacheManager,
) {
    companion object {
        private const val QAG_MODERATING_LOCK_CACHE_NAME = "QagModeratingLockCache"
        private const val QAG_MODERATING_LOCK_ID_LIST = "QagIdLockedList"
    }

    fun getLockedQagList(userUUID: UUID): QagLockList? {
        return try {
            getCache()?.get(userUUID.toString(), QagLockList::class.java)
        } catch (e: IllegalStateException) {
            null
        }
    }

    fun setLockedQagList(userUUID: UUID, qagUUIDList: List<UUID>) {
        var qagIdList = emptyList<String>()
        for (qagId in qagUUIDList)
            qagIdList = qagIdList + qagId.toString()
        getCache()?.put(
            userUUID.toString(),
            QagLockList(
                userId = userUUID.toString(),
                qagIdList = qagIdList,
                dateLockedAt = System.currentTimeMillis()
            )
        )
        val qagIdLockedList = getAllQagModeratingIdList() ?: emptyList()
        getCache()?.put(QAG_MODERATING_LOCK_ID_LIST, qagIdLockedList + qagIdList)
    }

    fun isQagIdListLocked(qagUUIDList: List<UUID>): Boolean {
        var qagIdList = emptyList<String>()
        for (qagId in qagUUIDList)
            qagIdList = qagIdList + qagId.toString()
        return getAllQagModeratingIdList()?.containsAll(qagIdList) ?: false
    }

    fun deleteQagLockedList(userUUID: UUID) {
        val userQagLockedList = getCache()?.get(userUUID.toString(), QagLockList::class.java)?.qagIdList
        val allQagModeratingIdList = getAllQagModeratingIdList()?.toMutableList()
        if (userQagLockedList != null && allQagModeratingIdList != null) {
            allQagModeratingIdList.removeAll(userQagLockedList)
            getCache()?.evict(userUUID.toString())
            getCache()?.put(QAG_MODERATING_LOCK_ID_LIST, allQagModeratingIdList)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getAllQagModeratingIdList() =
        getCache()?.get(QAG_MODERATING_LOCK_ID_LIST, List::class.java) as? List<String>

    private fun getCache() = cacheManager.getCache(QAG_MODERATING_LOCK_CACHE_NAME)
}

