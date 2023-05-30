package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import fr.social.gouv.agora.usecase.qag.repository.QagModeratingInfo
import org.springframework.stereotype.Component

@Component
class QagModeratingInfoMapper {

    fun toDomain(dto: QagDTO): QagModeratingInfo {
        return QagModeratingInfo(
            id = dto.id.toString(),
            thematiqueId = dto.thematiqueId.toString(),
            title = dto.title,
            description = dto.description,
            username = dto.username,
            date = dto.postDate,
        )
    }
}