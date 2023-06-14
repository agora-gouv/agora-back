package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.usecase.qag.repository.QagModeratingLockRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class QagModeratingLockRepositoryImpl(
    private val cacheRepository: QagModeratingLockCacheRepository,
) : QagModeratingLockRepository {

    override fun getLockedQagList(userId: String): List<String> {
        return try {
            cacheRepository.getLockedQagList(UUID.fromString(userId))
        } catch (e: IllegalArgumentException) {
            emptyList()
        }
    }

    override fun setLockedQagList(userId: String, qagList: List<String>) {
        try {
            var qagUUIDList = emptyList<UUID>()
            for (qagId in qagList)
                qagUUIDList = qagUUIDList + UUID.fromString(qagId)
            cacheRepository.setLockedQagList(userUUID = UUID.fromString(userId), qagUUIDList = qagUUIDList)
        } catch (e: IllegalArgumentException) {
            //do nothing
        }
    }

    override fun isQagIdListLocked(qagList: List<String>): Boolean {
        return try {
            var qagUUIDList = emptyList<UUID>()
            for (qagId in qagList)
                qagUUIDList = qagUUIDList + UUID.fromString(qagId)
            cacheRepository.isQagIdListLocked(qagUUIDList = qagUUIDList)
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    override fun deleteQagLockedList(userId: String) {
        try {
            cacheRepository.deleteQagLockedList(userUUID = UUID.fromString(userId))
        } catch (e: IllegalArgumentException) {
            //do nothing
        }
    }
}
