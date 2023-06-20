package fr.social.gouv.agora.infrastructure.qagUpdates.repository

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.domain.QagUpdates
import fr.social.gouv.agora.infrastructure.qagUpdates.dto.QagUpdatesDTO
import fr.social.gouv.agora.infrastructure.utils.DateUtils.toDate
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

@Component
class QagUpdatesMapper {

    companion object {
        private const val STATUS_MODERATED_REJECTED = -1
        private const val STATUS_MODERATED_ACCEPTED = 1
    }

    fun toDto(domain: QagUpdates): QagUpdatesDTO? {
        return try {
            QagUpdatesDTO(
                id = UUID.randomUUID(),
                qagId = UUID.fromString(domain.qagId),
                userId = UUID.fromString(domain.userId),
                status = if (domain.newQagStatus == QagStatus.MODERATED_ACCEPTED) STATUS_MODERATED_ACCEPTED
                else STATUS_MODERATED_REJECTED,
                moderatedDate = Date(),
            )
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}