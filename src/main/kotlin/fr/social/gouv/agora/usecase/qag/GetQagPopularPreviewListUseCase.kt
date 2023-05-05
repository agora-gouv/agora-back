package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagPreview
import fr.social.gouv.agora.usecase.qag.repository.QagPopularListRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class GetQagPopularPreviewListUseCase(
    private val thematiqueRepository: ThematiqueRepository,
    private val supportRepository: GetSupportQagRepository,
    private val qagPopularListRepository: QagPopularListRepository,
) {
    fun getQagPopularPreviewList(userId: String): List<QagPreview> {
        return qagPopularListRepository.getQagPopularList()
            .mapNotNull { qagInfo ->
                thematiqueRepository.getThematiqueList()
                    .find { thematique -> thematique.id == qagInfo.thematiqueId }?.let { thematique ->
                        supportRepository.getSupportQag(qagInfo.id, userId)?.let {
                            QagPreview(
                                id = qagInfo.id,
                                thematique = thematique,
                                title = qagInfo.title,
                                username = qagInfo.username,
                                date = qagInfo.date,
                                support = it,
                            )
                        }
                    }
            }.sortedByDescending {it.support.supportCount}
    }
}
