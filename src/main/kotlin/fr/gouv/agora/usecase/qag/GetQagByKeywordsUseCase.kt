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
        return qagInfoRepository.getQagByKeywordsList(keywords).mapNotNull { qagInfo ->
            thematiqueRepository.getThematiqueList().find { thematique -> thematique.id == qagInfo.thematiqueId }
                ?.let { thematique ->
                    val qagWithSupportCount = QagWithSupportCount(
                        qagInfo = qagInfo,
                        thematique = thematique,
                    )
                    val supportedQagIds = supportQagUseCase.getUserSupportedQagIds(userId)
                    qagPreviewMapper.toPreview(
                        qag = qagWithSupportCount,
                        isSupportedByUser = qagWithSupportCount.qagInfo.id in supportedQagIds,
                        isAuthor = userId == qagWithSupportCount.qagInfo.userId
                    )
                }
        }
    }
}