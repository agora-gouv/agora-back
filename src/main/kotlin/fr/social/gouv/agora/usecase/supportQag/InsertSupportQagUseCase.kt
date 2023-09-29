package fr.social.gouv.agora.usecase.supportQag

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.domain.SupportQagInserting
import fr.social.gouv.agora.usecase.qag.QagWithSupportCount
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qagPreview.repository.QagPreviewPageCacheRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagResult
import org.springframework.stereotype.Service

@Service
class InsertSupportQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val getSupportQagRepository: GetSupportQagRepository,
    private val supportQagRepository: SupportQagRepository,
    private val previewPageRepository: QagPreviewPageCacheRepository,
) {
    fun insertSupportQag(supportQagInserting: SupportQagInserting): SupportQagResult {
        if (getSupportQagRepository.getSupportQag(
                qagId = supportQagInserting.qagId,
                userId = supportQagInserting.userId,
            )?.isSupportedByUser == true
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
        previewPageRepository.getQagPopularList(thematiqueId = null)?.let { qagList ->
            buildIncrementSupportCountIfPresent(qagInfo, qagList)?.let { updatedQagList ->
                previewPageRepository.initQagPopularList(thematiqueId = null, qagList = updatedQagList)
            }
        }

        previewPageRepository.getQagPopularList(thematiqueId = qagInfo.thematiqueId)?.let { qagList ->
            buildIncrementSupportCountIfPresent(qagInfo, qagList)?.let { updatedQagList ->
                previewPageRepository.initQagPopularList(thematiqueId = qagInfo.thematiqueId, qagList = updatedQagList)
            }
        }

        previewPageRepository.getQagSupportedList(userId = userId, thematiqueId = null)?.let { qagList ->
            buildIncrementSupportCountIfPresent(qagInfo, qagList)?.let { updatedQagList ->
                previewPageRepository.initQagSupportedList(
                    userId = userId,
                    thematiqueId = null,
                    qagList = updatedQagList,
                )
            }
        }

        previewPageRepository.getQagSupportedList(userId = userId, thematiqueId = qagInfo.thematiqueId)
            ?.let { qagList ->
                buildIncrementSupportCountIfPresent(qagInfo, qagList)?.let { updatedQagList ->
                    previewPageRepository.initQagSupportedList(
                        userId = userId,
                        thematiqueId = qagInfo.thematiqueId,
                        qagList = updatedQagList,
                    )
                }
            }
    }

    private fun buildIncrementSupportCountIfPresent(
        qagInfo: QagInfo,
        qagList: List<QagWithSupportCount>,
    ): List<QagWithSupportCount>? {
        return if (qagList.any { it.qagInfo.id == qagInfo.id }) {
            qagList.map { qag ->
                if (qag.qagInfo.id == qagInfo.id) {
                    qag.copy(supportCount = qag.supportCount + 1)
                } else qag
            }
        } else null
    }
}