package fr.social.gouv.agora.usecase.qagSelection

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.qag.QagFilters
import org.springframework.stereotype.Component

@Component
class MostPopularQagFilterGenerator {

    fun generateFilter(): QagFilters {
        return QagFilters(
            filterQagInfo = { qagInfo -> qagInfo.status == QagStatus.MODERATED_ACCEPTED },
            filterSupportQagInfo = { true },
            filterQagWithSupportList = { true },
        )
    }

}