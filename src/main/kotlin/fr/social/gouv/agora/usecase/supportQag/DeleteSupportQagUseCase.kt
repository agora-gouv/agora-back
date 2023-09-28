package fr.social.gouv.agora.usecase.supportQag

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.domain.SupportQagDeleting
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qagPreview.repository.QagPreviewPageRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagResult
import org.springframework.stereotype.Service

@Service
class DeleteSupportQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val getSupportQagRepository: GetSupportQagRepository,
    private val supportQagRepository: SupportQagRepository,
    private val previewPageRepository: QagPreviewPageRepository,
) {
    fun deleteSupportQag(supportQagDeleting: SupportQagDeleting): SupportQagResult {
        if (getSupportQagRepository.getSupportQag(
                qagId = supportQagDeleting.qagId,
                userId = supportQagDeleting.userId,
            )?.isSupportedByUser == false
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
        val popularQagsNoThematique = previewPageRepository.getQagPopularList(thematiqueId = null)
        if (popularQagsNoThematique?.any { it.qagInfo.id == qagInfo.id } == true) {
            previewPageRepository.evictQagPopularList(thematiqueId = null)
        }

        val popularQagsWithThematique = previewPageRepository.getQagPopularList(thematiqueId = qagInfo.thematiqueId)
        if (popularQagsWithThematique?.any { it.qagInfo.id == qagInfo.id } == true) {
            previewPageRepository.evictQagPopularList(thematiqueId = qagInfo.thematiqueId)
        }

        previewPageRepository.evictQagSupportedList(userId = userId, thematiqueId = null)
        previewPageRepository.evictQagSupportedList(userId = userId, thematiqueId = qagInfo.thematiqueId)
    }
}