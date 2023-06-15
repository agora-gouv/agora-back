package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.usecase.qag.repository.QagModeratingLockRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class QagModeratingLockRepositoryImpl(
    private val cacheRepository: QagModeratingLockCacheRepository,
) : QagModeratingLockRepository {

    override fun getQagLocked(qagId: String): String? {
        return try {
            cacheRepository.getQagLocked(UUID.fromString(qagId))
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    override fun setQagLocked(qagId: String, userId: String) {
        try {
            cacheRepository.setQagLocked(qagUUID = UUID.fromString(qagId), userUUID = UUID.fromString(userId))
        } catch (e: IllegalArgumentException) {
            //do nothing
        }
    }
}
