package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import org.springframework.stereotype.Component

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

}