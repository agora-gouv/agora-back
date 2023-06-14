package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.usecase.qag.repository.QagModeratingLockRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class QagModeratingLockRepositoryImpl(
    private val cacheRepository: QagModeratingLockCacheRepository,
) : QagModeratingLockRepository {

    override fun isQagLocked(qagId: String): Boolean {
        return try {
            cacheRepository.isQagLocked(UUID.fromString(qagId))
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    override fun setQagLocked(qagId: String) {
        try {
            cacheRepository.setQagLocked(UUID.fromString(qagId))
        } catch (e: IllegalArgumentException) {
            //do nothing
        }
    }
}
