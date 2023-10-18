package fr.social.gouv.agora.usecase.supportQag

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.domain.SupportQagDeleting
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagWithSupportCountCacheRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagResult
import org.springframework.stereotype.Service

@Service
class DeleteSupportQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val getSupportQagRepository: GetSupportQagRepository,
    private val supportQagRepository: SupportQagRepository,
    private val qagWithSupportCountCacheRepository: QagWithSupportCountCacheRepository,
) {
    fun deleteSupportQag(supportQagDeleting: SupportQagDeleting): SupportQagResult {
        // TODO
//        if (getSupportQagRepository.getSupportQag(
//                qagId = supportQagDeleting.qagId,
//                userId = supportQagDeleting.userId,
//            )?.isSupportedByUser == false
//        ) {
//            println("⚠️ Remove support error: already unsupported")
//            return SupportQagResult.FAILURE
//        }

        val qagInfo = qagInfoRepository.getQagInfo(supportQagDeleting.qagId)
        return when (qagInfo?.status) {
            null, QagStatus.ARCHIVED, QagStatus.MODERATED_REJECTED, QagStatus.SELECTED_FOR_RESPONSE -> {
                println("⚠️ Remove support error: QaG with invalid status")
                SupportQagResult.FAILURE
            }
            QagStatus.OPEN, QagStatus.MODERATED_ACCEPTED -> {
                val supportResult = supportQagRepository.deleteSupportQag(supportQagDeleting)
                if (supportResult == SupportQagResult.SUCCESS) {
                    updateCache(qagInfo, supportQagDeleting.userId)
                } else {
                    println("⚠️ Remove support error")
                }
                supportResult
            }
        }
    }

    private fun updateCache(qagInfo: QagInfo, userId: String) {
        qagWithSupportCountCacheRepository.decrementQagPopularSupportCount(thematiqueId = null, qagId = qagInfo.id)
        qagWithSupportCountCacheRepository.decrementQagPopularSupportCount(
            thematiqueId = qagInfo.thematiqueId,
            qagId = qagInfo.id,
        )
        qagWithSupportCountCacheRepository.decrementQagLatestSupportCount(thematiqueId = null, qagId = qagInfo.id)
        qagWithSupportCountCacheRepository.decrementQagLatestSupportCount(
            thematiqueId = qagInfo.thematiqueId,
            qagId = qagInfo.id,
        )
        qagWithSupportCountCacheRepository.decrementSupportedSupportCount(
            thematiqueId = null,
            qagId = qagInfo.id,
            userId = userId,
        )
        qagWithSupportCountCacheRepository.decrementSupportedSupportCount(
            thematiqueId = qagInfo.thematiqueId,
            qagId = qagInfo.id,
            userId = userId,
        )
    }

}