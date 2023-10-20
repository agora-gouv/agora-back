package fr.social.gouv.agora.usecase.supportQag

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.domain.SupportQagInserting
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
class InsertSupportQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val getSupportQagRepository: GetSupportQagRepository,
    private val supportQagRepository: SupportQagRepository,
    private val qagPreviewCacheRepository: QagPreviewCacheRepository,
    private val qagDetailsCacheRepository: QagDetailsCacheRepository,
    private val supportQagCacheRepository: SupportQagCacheRepository,
) {
    fun insertSupportQag(supportQagInserting: SupportQagInserting): SupportQagResult {
        if (getSupportQagRepository.isQagSupported(
                qagId = supportQagInserting.qagId,
                userId = supportQagInserting.userId,
            )
        ) {
            println("⚠️ Add support error: already supported")
            return SupportQagResult.FAILURE
        }

        val qagInfo = qagInfoRepository.getQagInfo(supportQagInserting.qagId)
        return when (qagInfo?.status) {
            null, QagStatus.ARCHIVED, QagStatus.MODERATED_REJECTED, QagStatus.SELECTED_FOR_RESPONSE -> {
                println("⚠️ Add support error: QaG with invalid status")
                SupportQagResult.FAILURE
            }
            QagStatus.OPEN, QagStatus.MODERATED_ACCEPTED -> {
                val supportResult = supportQagRepository.insertSupportQag(supportQagInserting)
                if (supportResult == SupportQagResult.SUCCESS) {
                    updateCache(qagInfo, supportQagInserting.userId)
                } else {
                    println("⚠️ Add support error")
                }
                supportResult
            }
        }
    }

    private fun updateCache(qagInfo: QagInfo, userId: String) {
        qagPreviewCacheRepository.incrementQagPopularSupportCount(thematiqueId = null, qagId = qagInfo.id)
        qagPreviewCacheRepository.incrementQagPopularSupportCount(
            thematiqueId = qagInfo.thematiqueId,
            qagId = qagInfo.id,
        )
        qagPreviewCacheRepository.incrementQagLatestSupportCount(thematiqueId = null, qagId = qagInfo.id)
        qagPreviewCacheRepository.incrementQagLatestSupportCount(
            thematiqueId = qagInfo.thematiqueId,
            qagId = qagInfo.id,
        )
        qagPreviewCacheRepository.incrementSupportedSupportCount(
            thematiqueId = null,
            qagId = qagInfo.id,
            userId = userId,
        )
        qagPreviewCacheRepository.incrementSupportedSupportCount(
            thematiqueId = qagInfo.thematiqueId,
            qagId = qagInfo.id,
            userId = userId,
        )
        qagDetailsCacheRepository.incrementSupportCount(qagInfo.id)
        supportQagCacheRepository.addSupportedQagIds(userId = userId, qagId = qagInfo.id)
    }

}