package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.domain.QagInserting
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import fr.social.gouv.agora.infrastructure.utils.UuidUtils
import org.springframework.stereotype.Component
import java.util.*

@Component
class QagInfoMapper {

    companion object {
        private const val STATUS_OPEN = 1
        private const val STATUS_ARCHIVED = 2
        private const val STATUS_MODERATED = -1
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
                STATUS_MODERATED -> QagStatus.MODERATED
                else -> throw IllegalArgumentException("Invalid QaG status : ${dto.status}")
            },
            username = dto.username,
        )
    }

    fun toDto(domain: QagInserting): QagDTO? {
        return try {
            QagDTO(
                id = UuidUtils.NOT_FOUND_UUID,
                title = domain.title,
                description = domain.description,
                postDate = domain.date,
                status = when (domain.status) {
                    QagStatus.OPEN -> STATUS_OPEN
                    QagStatus.ARCHIVED -> STATUS_ARCHIVED
                    QagStatus.MODERATED -> STATUS_MODERATED
                },
                username = domain.username,
                thematiqueId = UUID.fromString(domain.thematiqueId),
                userId = UUID.fromString(domain.userId),
            )
        } catch (e: IllegalArgumentException) {
            null
        }
    }

}