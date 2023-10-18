package fr.social.gouv.agora.usecase.supportQag

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.domain.SupportQagInserting
import fr.social.gouv.agora.usecase.qag.QagWithSupportCount
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagWithSupportCountCacheRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagResult
import org.springframework.stereotype.Service

@Service
class InsertSupportQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val getSupportQagRepository: GetSupportQagRepository,
    private val supportQagRepository: SupportQagRepository,
    private val qagWithSupportCountCacheRepository: QagWithSupportCountCacheRepository,
) {
    fun insertSupportQag(supportQagInserting: SupportQagInserting): SupportQagResult {
        // TODO
//        if (getSupportQagRepository.getSupportQag(
//                qagId = supportQagInserting.qagId,
//                userId = supportQagInserting.userId,
//            )?.isSupportedByUser == true
//        ) {
//            println("⚠️ Add support error: already supported")
//            return SupportQagResult.FAILURE
//        }

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
        qagWithSupportCountCacheRepository.incrementQagPopularSupportCount(thematiqueId = null, qagId = qagInfo.id)
        qagWithSupportCountCacheRepository.incrementQagPopularSupportCount(
            thematiqueId = qagInfo.thematiqueId,
            qagId = qagInfo.id,
        )
        qagWithSupportCountCacheRepository.incrementQagLatestSupportCount(thematiqueId = null, qagId = qagInfo.id)
        qagWithSupportCountCacheRepository.incrementQagLatestSupportCount(
            thematiqueId = qagInfo.thematiqueId,
            qagId = qagInfo.id,
        )
        qagWithSupportCountCacheRepository.incrementSupportedSupportCount(
            thematiqueId = null,
            qagId = qagInfo.id,
            userId = userId,
        )
        qagWithSupportCountCacheRepository.incrementSupportedSupportCount(
            thematiqueId = qagInfo.thematiqueId,
            qagId = qagInfo.id,
            userId = userId,
        )
    }

}