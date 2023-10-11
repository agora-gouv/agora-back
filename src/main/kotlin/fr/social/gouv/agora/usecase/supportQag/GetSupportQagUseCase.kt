package fr.social.gouv.agora.usecase.supportQag

import fr.social.gouv.agora.infrastructure.supportQag.repository.SupportQagCacheRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import org.springframework.stereotype.Service

@Service
class SupportQagUseCase(
    private val supportQagRepository: GetSupportQagRepository,
    private val cacheRepository: SupportQagCacheRepository,
) {

    fun getUserSupportedQagIds(userId: String): List<String> {
        // TODO tests
        return cacheRepository.getUserSupportedQagIds(userId = userId)
            ?: supportQagRepository.getUserSupportedQags(userId = userId).also { userSupportedQagIds ->
                cacheRepository.initUserSupportedQagIds(userId, userSupportedQagIds)
            }
    }

}