package fr.social.gouv.agora.usecase.qagSimilar

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.qag.QagFilters
import org.springframework.stereotype.Component

@Component
class FindQagSimilarFilterGenerator {

    fun getFindQagSimilarFilters(): QagFilters {
        return QagFilters(
            filterQagInfo = { qagInfo -> qagInfo.status == QagStatus.MODERATED_ACCEPTED },
            filterSupportQagInfo = { true },
            filterQagWithSupportList = { true },
        )
    }
}