package fr.social.gouv.agora.usecase.qagPreview

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.qag.QagFilters
import org.springframework.stereotype.Component

@Component
class QagPreviewFilterGenerator {

    fun getPreviewQagFilters(userId: String, thematiqueId: String?): QagFilters {
        return QagFilters(
            filterQagInfo = { qagInfo ->
                (thematiqueId == null || qagInfo.thematiqueId == thematiqueId)
                        && (qagInfo.status == QagStatus.OPEN && qagInfo.status == QagStatus.MODERATED_ACCEPTED)
            },
            filterSupportQagInfo = { true },
            filterQagWithSupportList = { qagInfoWithSupport ->
                qagInfoWithSupport.qagInfo.status == QagStatus.OPEN && qagInfoWithSupport.supportQagList.any { it.userId == userId }
            },
        )
    }
}