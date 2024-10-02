package fr.gouv.agora.usecase.supportQag

import fr.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.gouv.agora.usecase.supportQag.repository.SupportQagCacheRepository
import org.springframework.stereotype.Service

@Service
class SupportQagUseCase(
    private val supportQagRepository: GetSupportQagRepository,
) {

    fun getUserSupportedQagIds(userId: String): List<String> {
        return supportQagRepository.getUserSupportedQags(userId = userId)
    }

    fun getSupportedQagCount(userId: String, thematiqueId: String?): Int {
        return supportQagRepository.getSupportedQagCount(userId = userId, thematiqueId = thematiqueId)
    }
}
