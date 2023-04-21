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
        var supportQagDTO = mapper.toDto(supportQagInserting)
        if (supportQagDTO != null)
            repeat(3) {
                if (databaseRepository.existsById(supportQagDTO!!.id)) {
                    supportQagDTO = mapper.toDto(supportQagInserting)
                } else {
                    databaseRepository.save(supportQagDTO!!)
                    supportQagCacheRepository.insertSupportQag(
                        supportQagDTO!!.qagId,
                        supportQagDTO!!.userId,
                        supportQagDTO
                    )
                    return SupportQagResult.SUCCESS
                }
            }
        return SupportQagResult.FAILURE
    }

    override fun deleteSupportQag(supportQagDeleting: SupportQagDeleting): SupportQagResult {
        try {
            UUID.fromString(supportQagDeleting.qagId)
        } catch (e: IllegalArgumentException) {
            return SupportQagResult.FAILURE
        }
        return try {
            val qagId = UUID.fromString(supportQagDeleting.qagId)
            databaseRepository.deleteSupportQag(supportQagDeleting.userId, qagId)
            supportQagCacheRepository.insertSupportQag(qagId, supportQagDeleting.userId, null)
            SupportQagResult.SUCCESS
        } catch (e: Exception) {
            SupportQagResult.SUCCESS
        }
    }
}