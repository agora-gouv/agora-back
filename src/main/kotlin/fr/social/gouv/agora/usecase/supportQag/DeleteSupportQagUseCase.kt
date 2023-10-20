package fr.social.gouv.agora.usecase.supportQag

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.domain.SupportQagDeleting
import fr.social.gouv.agora.usecase.qag.repository.QagDetailsCacheRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagPreviewCacheRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagCacheRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagResult
import org.springframework.stereotype.Service

@Service
class DeleteSupportQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val getSupportQagRepository: GetSupportQagRepository,
    private val supportQagRepository: SupportQagRepository,
    private val qagPreviewCacheRepository: QagPreviewCacheRepository,
    private val qagDetailsCacheRepository: QagDetailsCacheRepository,
    private val supportQagCacheRepository: SupportQagCacheRepository,
) {
    fun deleteSupportQag(supportQagDeleting: SupportQagDeleting): SupportQagResult {
        if (getSupportQagRepository.isQagSupported(
                qagId = supportQagDeleting.qagId,
                userId = supportQagDeleting.userId,
            )
        ) {
            println("⚠️ Remove support error: already unsupported")
            return SupportQagResult.FAILURE
        }

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
        qagPreviewCacheRepository.decrementQagPopularSupportCount(thematiqueId = null, qagId = qagInfo.id)
        qagPreviewCacheRepository.decrementQagPopularSupportCount(
            thematiqueId = qagInfo.thematiqueId,
            qagId = qagInfo.id,
        )
        qagPreviewCacheRepository.decrementQagLatestSupportCount(thematiqueId = null, qagId = qagInfo.id)
        qagPreviewCacheRepository.decrementQagLatestSupportCount(
            thematiqueId = qagInfo.thematiqueId,
            qagId = qagInfo.id,
        )
        qagPreviewCacheRepository.decrementSupportedSupportCount(
            thematiqueId = null,
            qagId = qagInfo.id,
            userId = userId,
        )
        qagPreviewCacheRepository.decrementSupportedSupportCount(
            thematiqueId = qagInfo.thematiqueId,
            qagId = qagInfo.id,
            userId = userId,
        )
        qagDetailsCacheRepository.decrementSupportCount(qagInfo.id)
        supportQagCacheRepository.removeSupportedQagIds(userId = userId, qagId = qagInfo.id)
    }

}