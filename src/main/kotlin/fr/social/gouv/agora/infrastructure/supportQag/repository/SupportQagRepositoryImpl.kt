package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.domain.SupportQagDeleting
import fr.social.gouv.agora.domain.SupportQagInserting
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagResult
import org.springframework.stereotype.Component
import java.util.*

@Component
class SupportQagRepositoryImpl(
    private val databaseRepository: SupportQagDatabaseRepository,
    private val mapper: SupportQagMapper,
    private val supportQagCacheRepository: SupportQagCacheRepository,
) : SupportQagRepository {

    companion object {
        private const val MAX_INSERT_RETRY_COUNT = 3
    }

    override fun insertSupportQag(supportQagInserting: SupportQagInserting): SupportQagResult {
        repeat(MAX_INSERT_RETRY_COUNT) {
            mapper.toDto(supportQagInserting)?.let { supportQagDTO ->
                if (!databaseRepository.existsById(supportQagDTO.id)) {
                    databaseRepository.save(supportQagDTO)
                    supportQagCacheRepository.insertSupportQag(
                        qagId = supportQagDTO.qagId,
                        userId = supportQagDTO.userId,
                        supportQagDTO = supportQagDTO
                    )
                    return SupportQagResult.SUCCESS
                }
            }
        }
        return SupportQagResult.FAILURE
    }

    override fun deleteSupportQag(supportQagDeleting: SupportQagDeleting): SupportQagResult {
        try {
            UUID.fromString(supportQagDeleting.qagId)
            UUID.fromString(supportQagDeleting.userId)
        } catch (e: IllegalArgumentException) {
            return SupportQagResult.FAILURE
        }
        val qagId = UUID.fromString(supportQagDeleting.qagId)
        val userId = UUID.fromString(supportQagDeleting.userId)
        val resultDelete = databaseRepository.deleteSupportQag(userId, qagId)
        return if (resultDelete <= 0)
            SupportQagResult.FAILURE
        else {
            supportQagCacheRepository.insertSupportQag(qagId, userId, null)
            SupportQagResult.SUCCESS
        }
    }
}