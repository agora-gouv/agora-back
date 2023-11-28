package fr.gouv.agora.infrastructure.header.repository

import fr.gouv.agora.domain.Header
import fr.gouv.agora.infrastructure.header.dto.HeaderDTO
import org.springframework.stereotype.Component

@Component
class HeaderMapper {
    fun toDomain(dto: HeaderDTO): Header {
        return Header(
            headerId = dto.headerId,
            title = dto.title,
            message = dto.message,
        )
    }
}