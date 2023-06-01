package fr.social.gouv.agora.usecase.supportQag

import fr.social.gouv.agora.domain.SupportQagInserting
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagResult
import org.springframework.stereotype.Service

@Service
class InsertSupportQagUseCase(private val repository: SupportQagRepository) {
    fun insertSupportQag(supportQagInserting: SupportQagInserting): SupportQagResult {
        return repository.insertSupportQag(
            SupportQagInserting(
                qagId = supportQagInserting.qagId,
                userId = supportQagInserting.userId,
            )
        )
    }
}