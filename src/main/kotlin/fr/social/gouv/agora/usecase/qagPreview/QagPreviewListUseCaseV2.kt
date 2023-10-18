package fr.social.gouv.agora.usecase.qagPreview

import fr.social.gouv.agora.domain.QagPreview
import fr.social.gouv.agora.usecase.qag.QagPreviewMapper
import fr.social.gouv.agora.usecase.qag.QagWithSupportCountAggregate
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import org.springframework.stereotype.Service

@Service
class QagPreviewListUseCaseV2(
    private val qagWithSupportCountAggregate: QagWithSupportCountAggregate,
    private val supportQagRepository: GetSupportQagRepository,
    private val mapper: QagPreviewMapper,
) {

    fun getQagPreviewList(userId: String, thematiqueId: String?): QagPreviewList {
        val supportedQagIds = supportQagRepository.getUserSupportedQags(userId)
        return QagPreviewList(
            popularPreviewList = qagWithSupportCountAggregate.getPopularQags(thematiqueId = thematiqueId)
                .map { qag -> mapper.toPreview(qag, supportedQagIds, userId) },
            latestPreviewList = qagWithSupportCountAggregate.getLatestQags(thematiqueId = thematiqueId)
                .map { qag -> mapper.toPreview(qag, supportedQagIds, userId) },
            supportedPreviewList = qagWithSupportCountAggregate.getSupportedQags(
                userId = userId,
                thematiqueId = thematiqueId,
            ).map { qag -> mapper.toPreview(qag, supportedQagIds, userId) },
        )
    }

}

data class QagPreviewList(
    val popularPreviewList: List<QagPreview>,
    val latestPreviewList: List<QagPreview>,
    val supportedPreviewList: List<QagPreview>,
)