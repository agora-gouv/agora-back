package fr.gouv.agora.infrastructure.qag.repository

import fr.gouv.agora.domain.QagInserting
import fr.gouv.agora.domain.QagStatus
import fr.gouv.agora.infrastructure.qag.dto.QagDTO
import fr.gouv.agora.infrastructure.qag.dto.QagWithSupportCountDTO
import fr.gouv.agora.infrastructure.utils.UuidUtils
import fr.gouv.agora.usecase.qag.repository.QagInfo
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import org.springframework.stereotype.Component
import java.util.*

@Component
class QagInfoMapper {

    companion object {
        private const val STATUS_OPEN = 0
        private const val STATUS_ARCHIVED = 2
        private const val STATUS_MODERATED_REJECTED = -1
        private const val STATUS_MODERATED_ACCEPTED = 1
        private const val STATUS_SELECTED_FOR_RESPONSE = 7
    }

    fun toDomain(dto: QagDTO): QagInfo {
        return QagInfo(
            id = dto.id.toString(),
            thematiqueId = dto.thematiqueId.toString(),
            title = dto.title,
            description = dto.description,
            date = dto.postDate,
            status = toQagStatus(dto.status),
            username = dto.username,
            userId = dto.userId.toString(),
        )
    }

    fun toDomain(dto: QagWithSupportCountDTO): QagInfoWithSupportCount {
        return QagInfoWithSupportCount(
            id = dto.id.toString(),
            thematiqueId = dto.thematiqueId.toString(),
            title = dto.title,
            description = dto.description,
            date = dto.postDate,
            status = toQagStatus(dto.status),
            username = dto.username,
            userId = dto.userId.toString(),
            supportCount = dto.supportCount,
        )
    }

    fun toDto(domain: QagInserting): QagDTO? {
        return try {
            QagDTO(
                id = UuidUtils.NOT_FOUND_UUID,
                title = domain.title,
                description = domain.description,
                postDate = domain.date,
                status = STATUS_OPEN,
                username = domain.username,
                thematiqueId = UUID.fromString(domain.thematiqueId),
                userId = UUID.fromString(domain.userId),
            )
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun toQagStatus(status: Int) = when (status) {
        STATUS_OPEN -> QagStatus.OPEN
        STATUS_ARCHIVED -> QagStatus.ARCHIVED
        STATUS_MODERATED_ACCEPTED -> QagStatus.MODERATED_ACCEPTED
        STATUS_MODERATED_REJECTED -> QagStatus.MODERATED_REJECTED
        STATUS_SELECTED_FOR_RESPONSE -> QagStatus.SELECTED_FOR_RESPONSE
        else -> throw IllegalArgumentException("Invalid QaG status : $status")
    }

    fun toIntStatus(qagStatus: QagStatus): Int {
        return when (qagStatus) {
            QagStatus.OPEN -> STATUS_OPEN
            QagStatus.ARCHIVED -> STATUS_ARCHIVED
            QagStatus.MODERATED_ACCEPTED -> STATUS_MODERATED_ACCEPTED
            QagStatus.MODERATED_REJECTED -> STATUS_MODERATED_REJECTED
            QagStatus.SELECTED_FOR_RESPONSE -> STATUS_SELECTED_FOR_RESPONSE
        }
    }

}