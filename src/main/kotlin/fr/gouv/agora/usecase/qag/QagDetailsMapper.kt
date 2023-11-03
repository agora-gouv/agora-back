package fr.gouv.agora.usecase.qag

import fr.gouv.agora.domain.QagDetails
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import fr.gouv.agora.usecase.qagPreview.QagWithSupportCount
import org.springframework.stereotype.Component

@Component
class QagDetailsMapper {

    fun toQagWithoutFeedbackResults(qag: QagDetails): QagDetails {
        return qag.copy(feedbackResults = null)
    }

    fun toQagWithSupportCount(
        qagInfoWithSupportCount: QagInfoWithSupportCount,
        thematique: Thematique,
    ): QagWithSupportCount {
        return QagWithSupportCount(
            qagInfo = qagInfoWithSupportCount,
            thematique = thematique,
        )
    }
}
