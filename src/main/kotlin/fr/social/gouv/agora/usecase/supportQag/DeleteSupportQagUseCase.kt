package fr.social.gouv.agora.usecase.supportQag

import fr.social.gouv.agora.domain.SupportQagDeleting
import fr.social.gouv.agora.usecase.login.repository.LoginRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagSupportedListRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagResult
import org.springframework.stereotype.Service

@Service
class DeleteSupportQagUseCase(
    private val repository: SupportQagRepository,
    private val qagSupportedListRepository: QagSupportedListRepository,
    private val loginRepository: LoginRepository,
    private val qagInfoRepository: QagInfoRepository,
) {
    fun deleteSupportQag(supportQagDeleting: SupportQagDeleting): SupportQagResult {
        qagInfoRepository.getQagInfo(supportQagDeleting.qagId)?.let {
            qagSupportedListRepository.deleteQagSupportedList(
                thematiqueId = it.thematiqueId, userId = supportQagDeleting.userId
            )
        }
        val validUserId = loginRepository.getUser(supportQagDeleting.userId)?.userId.toString()
        return repository.deleteSupportQag(SupportQagDeleting(qagId = supportQagDeleting.qagId, userId = validUserId))
    }
}