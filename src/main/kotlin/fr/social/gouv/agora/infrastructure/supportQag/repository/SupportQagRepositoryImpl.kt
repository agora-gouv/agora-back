package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.domain.SupportQagDeleting
import fr.social.gouv.agora.domain.SupportQagInserting
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagResult
import org.springframework.stereotype.Component

@Component
class SupportQagRepositoryImpl(
    private val databaseRepository: SupportQagDatabaseRepository,
    private val mapper: SupportQagMapper,
    private val supportQagCacheRepository: SupportQagCacheRepository,
) : SupportQagRepository {

    override fun insertSupportQag(supportQagInserting: SupportQagInserting): SupportQagResult {
        var supportQagDTO = mapper.toDto(supportQagInserting)
        var generationIdCounter = 0
        return if (supportQagDTO != null) {
            while (generationIdCounter < 3) {
                if (databaseRepository.existsById(supportQagDTO!!.id)) {
                    supportQagDTO = mapper.toDto(supportQagInserting)
                    generationIdCounter++
                } else
                    break
            }//pour 3
            databaseRepository.save(supportQagDTO!!)
            supportQagCacheRepository.insertSupportQag(supportQagDTO.qagId, supportQagDTO.userId, supportQagDTO)
            SupportQagResult.SUCCESS
        } else
            SupportQagResult.FAILURE
    }

    override fun deleteSupportQag(supportQagDeleting: SupportQagDeleting): SupportQagResult {
        val supportQagDTO = mapper.toDto(supportQagDeleting)
        return if (supportQagDTO != null) {
            databaseRepository.deleteSupportQag(supportQagDTO.userId, supportQagDTO.qagId)
            supportQagCacheRepository.insertSupportQag(supportQagDTO.qagId, supportQagDTO.userId,null)
            SupportQagResult.SUCCESS
        } else
            SupportQagResult.FAILURE
    }
}