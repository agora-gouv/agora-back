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

    override fun insertSupportQag(supportQagInserting: SupportQagInserting): SupportQagResult {
        return mapper.toDto(supportQagInserting)?.let { supportQagDTO ->
            val savedSupportQagDTO = databaseRepository.save(supportQagDTO)
            supportQagCacheRepository.insertSupportQag(
                qagId = savedSupportQagDTO.qagId,
                userId = savedSupportQagDTO.userId,
                supportQagDTO = savedSupportQagDTO,
            )
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
                supportQagCacheRepository.insertSupportQag(qagId = qagId, userId = userId, supportQagDTO = null)
                SupportQagResult.SUCCESS
            }
        } catch (e: IllegalArgumentException) {
            SupportQagResult.FAILURE
        }
    }
}