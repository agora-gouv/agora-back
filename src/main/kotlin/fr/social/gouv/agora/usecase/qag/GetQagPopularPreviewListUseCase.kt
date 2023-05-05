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
    fun getQagPopularPreviewList(userId: String, thematiqueId: String?): List<QagPreview> {
        return qagPopularListRepository.getQagPopularList(thematiqueId = thematiqueId)
            .mapNotNull { qagInfo ->
                thematiqueRepository.getThematique(qagInfo.thematiqueId)?.let { thematique ->
                    supportRepository.getSupportQag(qagInfo.id, userId)?.let { supportQag ->
                        QagPreview(
                            id = qagInfo.id,
                            thematique = thematique,
                            title = qagInfo.title,
                            username = qagInfo.username,
                            date = qagInfo.date,
                            support = supportQag,
                        )
                    }
                }
            }.sortedByDescending { it.support.supportCount }
    }
}
