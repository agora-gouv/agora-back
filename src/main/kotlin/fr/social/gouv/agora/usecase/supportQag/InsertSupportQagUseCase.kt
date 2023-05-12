package fr.social.gouv.agora.usecase.supportQag

import fr.social.gouv.agora.domain.SupportQagInserting
import fr.social.gouv.agora.usecase.login.repository.LoginRepository
import fr.social.gouv.agora.usecase.qag.repository.QagSupportedListRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagResult
import org.springframework.stereotype.Service

@Service
class InsertSupportQagUseCase(
    private val repository: SupportQagRepository,
    private val qagSupportedListRepository: QagSupportedListRepository,
    private val loginRepository: LoginRepository,
) {
    fun insertSupportQag(supportQagInserting: SupportQagInserting): SupportQagResult {
        qagSupportedListRepository.deleteQagSupportedList(supportQagInserting.userId)
        val validUserId = loginRepository.getUser(supportQagInserting.userId)?.userId.toString()
        return repository.insertSupportQag(SupportQagInserting(qagId = supportQagInserting.qagId, userId = validUserId))
    }
}