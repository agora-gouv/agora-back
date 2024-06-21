package fr.gouv.agora.usecase.qag

import fr.gouv.agora.domain.QagDetails
import fr.gouv.agora.domain.QagStatus
import org.springframework.stereotype.Component

@Component
class GetPublicQagDetailsUseCase(private val qagDetailsAggregate: QagDetailsAggregate) {
    fun getQagDetails(qagId: String): QagDetails? {
        return qagDetailsAggregate.getQag(qagId = qagId)?.let { qag ->
            when (qag.status) {
                QagStatus.OPEN, QagStatus.ARCHIVED, QagStatus.MODERATED_REJECTED -> null
                QagStatus.MODERATED_ACCEPTED, QagStatus.SELECTED_FOR_RESPONSE -> qag
            }
        }
    }

}
