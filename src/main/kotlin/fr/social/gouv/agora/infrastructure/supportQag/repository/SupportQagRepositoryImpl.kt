package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.domain.SupportQagDeleting
import fr.social.gouv.agora.domain.SupportQagInserting
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagResult
import org.springframework.stereotype.Component
import java.util.*

@Component
class SupportQagRepositoryImpl(
    private val cacheRepository: SupportQagCacheRepository,
    private val databaseRepository: SupportQagDatabaseRepository,
    private val mapper: SupportQagMapper,
) : SupportQagRepository {

    override fun insertSupportQag(supportQagInserting: SupportQagInserting): SupportQagResult {
        return mapper.toDto(supportQagInserting)?.let { supportQagDTO ->
            val savedSupportQagDTO = databaseRepository.save(supportQagDTO)
            try {
                cacheRepository.insertSupportQag(supportQagDTO = savedSupportQagDTO)
            } catch (e: IllegalStateException) {
                initializeCache()
            }
            SupportQagResult.SUCCESS
        } ?: SupportQagResult.FAILURE
    }

    override fun deleteSupportQag(supportQagDeleting: SupportQagDeleting): SupportQagResult {
        return try {
            val qagId = UUID.fromString(supportQagDeleting.qagId)
            val userId = UUID.fromString(supportQagDeleting.userId)
            val resultDelete = databaseRepository.deleteSupportQag(userId = userId, qagId = qagId)
            if (resultDelete <= 0)
                SupportQagResult.FAILURE
            else {
                try {
                    cacheRepository.deleteSupportQag(qagId = qagId, userId = userId)
                } catch (e: IllegalStateException) {
                    initializeCache()
                }
                SupportQagResult.SUCCESS
            }
        } catch (e: IllegalArgumentException) {
            SupportQagResult.FAILURE
        }
    }

    private fun initializeCache() {
        cacheRepository.initializeCache(databaseRepository.getAllSupportQagList())
    }
}