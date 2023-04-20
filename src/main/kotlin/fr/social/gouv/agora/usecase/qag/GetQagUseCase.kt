package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.Qag
import fr.social.gouv.agora.usecase.qag.repository.GetQagRepository
import org.springframework.stereotype.Service

@Service
class GetQagUseCase(private val qagRepository: GetQagRepository) {
    fun getQag(qagId: String): Qag? {
        return qagRepository.getQag(qagId)
    }
}
