package fr.social.gouv.agora.usecase.supportQag

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.domain.SupportQagInserting
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagResult
import org.springframework.stereotype.Service

@Service
class InsertSupportQagUseCase(
    private val responseQagRepository: ResponseQagRepository,
    private val qagInfoRepository: QagInfoRepository,
    private val supportQagRepository: SupportQagRepository,
) {
    fun insertSupportQag(supportQagInserting: SupportQagInserting): SupportQagResult {
        if (responseQagRepository.getResponseQag(supportQagInserting.qagId) != null) return SupportQagResult.FAILURE

        return when (qagInfoRepository.getQagInfo(supportQagInserting.qagId)?.status) {
            null, QagStatus.ARCHIVED, QagStatus.MODERATED_REJECTED -> SupportQagResult.FAILURE
            QagStatus.OPEN, QagStatus.MODERATED_ACCEPTED -> supportQagRepository.insertSupportQag(
                SupportQagInserting(
                    qagId = supportQagInserting.qagId,
                    userId = supportQagInserting.userId,
                )
            )
        }
    }
}