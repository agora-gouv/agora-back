package fr.gouv.agora.usecase.supportQag

import fr.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.gouv.agora.usecase.supportQag.repository.SupportQagCacheRepository
import org.springframework.stereotype.Service

@Service
class SupportQagUseCase(
    private val supportQagRepository: GetSupportQagRepository,
    private val cacheRepository: SupportQagCacheRepository,
) {

    fun getUserSupportedQagIds(userId: String): List<String> {
        return cacheRepository.getUserSupportedQagIds(userId = userId)
            ?: supportQagRepository.getUserSupportedQags(userId = userId).also { userSupportedQagIds ->
                cacheRepository.initUserSupportedQagIds(userId, userSupportedQagIds)
            }
    }

}