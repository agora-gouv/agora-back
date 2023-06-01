package fr.social.gouv.agora.usecase.supportQag

import fr.social.gouv.agora.domain.SupportQagDeleting
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagResult
import org.springframework.stereotype.Service

@Service
class DeleteSupportQagUseCase(private val repository: SupportQagRepository) {
    fun deleteSupportQag(supportQagDeleting: SupportQagDeleting): SupportQagResult {
        return repository.deleteSupportQag(
            SupportQagDeleting(
                qagId = supportQagDeleting.qagId,
                userId = supportQagDeleting.userId,
            )
        )
    }
}