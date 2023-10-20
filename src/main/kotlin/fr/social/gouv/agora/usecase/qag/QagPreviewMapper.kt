package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagPreview
import fr.social.gouv.agora.domain.SupportQag
import fr.social.gouv.agora.usecase.qagPreview.QagWithSupportCount
import org.springframework.stereotype.Component

@Component
class QagPreviewMapper {

    fun toPreview(qag: QagWithSupportCount, supportedQagIds: List<String>, userId: String): QagPreview {
        return QagPreview(
            id = qag.qagInfo.id,
            thematique = qag.thematique,
            title = qag.qagInfo.title,
            username = qag.qagInfo.username,
            date = qag.qagInfo.date,
            support = SupportQag(
                supportCount = qag.qagInfo.supportCount,
                isSupportedByUser = supportedQagIds.any { supportedQagId -> supportedQagId == qag.qagInfo.id }
            ),
            isAuthor = qag.qagInfo.userId == userId,
        )
    }

}