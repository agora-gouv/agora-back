package fr.gouv.agora.infrastructure.headerQag.repository

import fr.gouv.agora.domain.HeaderQag
import fr.gouv.agora.infrastructure.headerQag.dto.HeaderQagDTO
import org.springframework.stereotype.Component

@Component
class HeaderQagMapper {
    fun toDomain(dto: HeaderQagDTO): HeaderQag {
        return HeaderQag(
            headerId = dto.headerId,
            title = dto.title,
            message = dto.message,
        )
    }
}