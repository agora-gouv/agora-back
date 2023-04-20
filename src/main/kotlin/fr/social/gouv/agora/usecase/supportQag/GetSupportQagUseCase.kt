package fr.social.gouv.agora.usecase.supportQag

import fr.social.gouv.agora.domain.SupportQag
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import org.springframework.stereotype.Service

@Service
class GetSupportQagUseCase(private val repository: GetSupportQagRepository) {
    fun getSupportQag(qagId: String, deviceId: String): SupportQag? {
        return repository.getSupportQag(qagId, deviceId)
    }
}