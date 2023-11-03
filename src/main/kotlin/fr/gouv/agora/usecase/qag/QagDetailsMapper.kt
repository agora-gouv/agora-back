package fr.gouv.agora.usecase.qag

import fr.gouv.agora.domain.QagDetails
import org.springframework.stereotype.Component

@Component
class QagDetailsMapper {

    fun toQagWithoutFeedbackResults(qag: QagDetails): QagDetails {
        return qag.copy(feedbackResults = null)
    }
}
