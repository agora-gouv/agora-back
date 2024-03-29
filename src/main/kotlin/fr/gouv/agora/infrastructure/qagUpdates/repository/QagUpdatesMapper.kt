package fr.gouv.agora.infrastructure.qagUpdates.repository

import fr.gouv.agora.domain.QagInsertingUpdates
import fr.gouv.agora.domain.QagStatus
import fr.gouv.agora.domain.QagUpdates
import fr.gouv.agora.infrastructure.qagUpdates.dto.QagUpdatesDTO
import org.springframework.stereotype.Component
import java.util.*

@Component
class QagUpdatesMapper {

    companion object {
        private const val STATUS_OPEN = 0
        private const val STATUS_ARCHIVED = 2
        private const val STATUS_MODERATED_REJECTED = -1
        private const val STATUS_MODERATED_ACCEPTED = 1
        private const val STATUS_SELECTED_FOR_RESPONSE = 7

        private const val SHOULD_DELETE_TRUE = 1
        private const val SHOULD_DELETE_FALSE = 0
    }

    fun toDto(domain: QagInsertingUpdates): QagUpdatesDTO? {
        return try {
            QagUpdatesDTO(
                id = UUID.randomUUID(),
                qagId = UUID.fromString(domain.qagId),
                userId = UUID.fromString(domain.userId),
                status = if (domain.newQagStatus == QagStatus.MODERATED_ACCEPTED) STATUS_MODERATED_ACCEPTED
                else STATUS_MODERATED_REJECTED,
                moderatedDate = Date(),
                reason = domain.reason,
                shouldDeleteFlag = if (domain.shouldDelete) SHOULD_DELETE_TRUE else SHOULD_DELETE_FALSE,
            )
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    fun toDomain(dto: QagUpdatesDTO): QagUpdates? {
        return try {
            QagUpdates(
                qagId = dto.qagId.toString(),
                qagStatus = when (dto.status) {
                    STATUS_OPEN -> QagStatus.OPEN
                    STATUS_ARCHIVED -> QagStatus.ARCHIVED
                    STATUS_MODERATED_ACCEPTED -> QagStatus.MODERATED_ACCEPTED
                    STATUS_MODERATED_REJECTED -> QagStatus.MODERATED_REJECTED
                    STATUS_SELECTED_FOR_RESPONSE -> QagStatus.SELECTED_FOR_RESPONSE
                    else -> throw IllegalArgumentException("Invalid QaG status : ${dto.status}")
                },
                userId = dto.userId.toString(),
                moderatedDate = dto.moderatedDate,
            )
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}