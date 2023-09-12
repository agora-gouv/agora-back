package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.domain.QagInserting
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import fr.social.gouv.agora.infrastructure.utils.UuidUtils
import fr.social.gouv.agora.infrastructure.utils.UuidUtils.DELETED_UUID
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
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
            status = when (dto.status) {
                STATUS_OPEN -> QagStatus.OPEN
                STATUS_ARCHIVED -> QagStatus.ARCHIVED
                STATUS_MODERATED_ACCEPTED -> QagStatus.MODERATED_ACCEPTED
                STATUS_MODERATED_REJECTED -> QagStatus.MODERATED_REJECTED
                STATUS_SELECTED_FOR_RESPONSE -> QagStatus.SELECTED_FOR_RESPONSE
                else -> throw IllegalArgumentException("Invalid QaG status : ${dto.status}")
            },
            username = dto.username,
            userId = dto.userId.toString(),
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

    fun updateQagStatus(dto: QagDTO, qagStatus: QagStatus): QagDTO {
        return dto.copy(
            status = when (qagStatus) {
                QagStatus.OPEN -> STATUS_OPEN
                QagStatus.ARCHIVED -> STATUS_ARCHIVED
                QagStatus.MODERATED_ACCEPTED -> STATUS_MODERATED_ACCEPTED
                QagStatus.MODERATED_REJECTED -> STATUS_MODERATED_REJECTED
                QagStatus.SELECTED_FOR_RESPONSE -> STATUS_SELECTED_FOR_RESPONSE
            }
        )
    }

    fun archiveQag(dto: QagDTO): QagDTO{
        return dto.copy(
            status = STATUS_ARCHIVED,
            username = "",
            userId = DELETED_UUID,
        )
    }
}