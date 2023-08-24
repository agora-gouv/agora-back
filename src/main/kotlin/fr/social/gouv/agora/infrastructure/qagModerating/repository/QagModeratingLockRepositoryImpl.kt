package fr.social.gouv.agora.infrastructure.qagModerating.repository

import fr.social.gouv.agora.usecase.qagModerating.repository.QagModeratingLockRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
@Suppress("unused")
class QagModeratingLockRepositoryImpl(
    private val cacheRepository: QagModeratingLockCacheRepository,
) : QagModeratingLockRepository {

    override fun getUserIdForQagLocked(qagId: String): String? {
        return try {
            cacheRepository.getUserIdForQagLocked(UUID.fromString(qagId))
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
