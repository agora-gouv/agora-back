package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.Qag
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import org.springframework.stereotype.Service

@Service
class GetQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val qagSupportQagRepository: GetSupportQagRepository,
) {
    fun getQag(qagId: String, deviceId: String): Qag? {
        // TODO transform deviceId to userId
        return qagInfoRepository.getQagInfo(qagId)?.let { qagInfo ->
            Qag(
                id = qagInfo.id,
                thematiqueId = qagInfo.thematiqueId,
                title = qagInfo.title,
                description = qagInfo.description,
                date = qagInfo.date,
                status = qagInfo.status,
                username = qagInfo.username,
                support = qagSupportQagRepository.getSupportQag(qagId = qagId, userId = deviceId),
                response = null, // TODO Feat-37
            )
        }
    }
}
