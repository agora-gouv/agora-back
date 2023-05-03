package fr.social.gouv.agora.usecase.responseQag

import fr.social.gouv.agora.domain.ResponseQagPreview
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.responseQag.repository.ResponseQagListRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class GetResponseQagPreviewListUseCase(
    private val thematiqueRepository: ThematiqueRepository,
    private val qagRepository: QagInfoRepository,
    private val responseQagListRepository: ResponseQagListRepository,
) {
    fun getResponseQagPreviewList(): List<ResponseQagPreview>? {
        return responseQagListRepository.getResponseQagList()
            .mapNotNull { responseQag ->
                thematiqueRepository.getThematiqueList()
                    .find { thematique -> thematique.id == qagRepository.getQagInfo(responseQag.qagId)?.thematiqueId }
                    ?.let { thematique ->
                        qagRepository.getQagInfo(responseQag.qagId)?.title?.let { title ->
                            ResponseQagPreview(
                                qagId = responseQag.qagId,
                                thematique = thematique,
                                title = title,
                                author = responseQag.author,
                                authorPortraitUrl = responseQag.authorPortraitUrl,
                                responseDate = responseQag.responseDate,
                            )
                        }
                    }
            }
    }
}
