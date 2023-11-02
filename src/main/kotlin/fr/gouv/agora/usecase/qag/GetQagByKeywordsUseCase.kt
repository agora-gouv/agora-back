package fr.gouv.agora.usecase.qag

import fr.gouv.agora.domain.QagPreview
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qagPreview.QagWithSupportCount
import fr.gouv.agora.usecase.supportQag.SupportQagUseCase
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class GetQagByKeywordsUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val supportQagUseCase: SupportQagUseCase,
    private val qagPreviewMapper: QagPreviewMapper,
) {
    fun getQagByKeywordsUseCase(userId: String, keywords: List<String>): List<QagPreview> {
        val thematiqueList = thematiqueRepository.getThematiqueList()
        val qagWithSupportCountList = qagInfoRepository.getQagByKeywordsList(keywords).mapNotNull { qagInfo ->
            thematiqueList.find { thematique -> thematique.id == qagInfo.thematiqueId }?.let { thematique ->
                QagWithSupportCount(
                    qagInfo = qagInfo,
                    thematique = thematique,
                )
            }
        }
        val supportedQagIds = supportQagUseCase.getUserSupportedQagIds(userId)
        return qagWithSupportCountList.map { qagWithSupportCount ->
            qagPreviewMapper.toPreview(
                qag = qagWithSupportCount,
                isSupportedByUser = supportedQagIds.any { qagId -> qagId == qagWithSupportCount.qagInfo.id },
                isAuthor = userId == qagWithSupportCount.qagInfo.userId
            )
        }
    }
}