package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagPreview
import fr.social.gouv.agora.usecase.login.repository.LoginRepository
import fr.social.gouv.agora.usecase.qag.repository.QagLatestListRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class GetQagLatestPreviewListUseCase(
    private val loginRepository: LoginRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val supportRepository: GetSupportQagRepository,
    private val qagLatestListRepository: QagLatestListRepository,
) {
    fun getQagLatestPreviewList(deviceId: String, thematiqueId: String?): List<QagPreview> {
        return qagLatestListRepository.getQagLatestList(thematiqueId = thematiqueId.takeUnless { it.isNullOrBlank() })
            .mapNotNull { qagInfo ->
                thematiqueRepository.getThematique(qagInfo.thematiqueId)?.let { thematique ->
                    loginRepository.getUser(deviceId)?.let { userInfo ->
                        supportRepository.getSupportQag(qagInfo.id, userInfo.userId)?.let { supportQag ->
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
                }
            }.sortedByDescending { it.date }
    }
}
