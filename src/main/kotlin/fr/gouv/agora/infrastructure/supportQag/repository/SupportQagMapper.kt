package fr.gouv.agora.infrastructure.supportQag.repository

import fr.gouv.agora.domain.SupportQagInserting
import fr.gouv.agora.infrastructure.supportQag.dto.SupportQagDTO
import org.springframework.stereotype.Component
import java.util.*

@Component
class SupportQagMapper {

    fun toDto(domain: SupportQagInserting): SupportQagDTO? {
        return try {
            SupportQagDTO(
                id = UUID.randomUUID(),
                supportDate = Date(),
                userId = UUID.fromString(domain.userId),
                qagId = UUID.fromString(domain.qagId)
            )
        } catch (e: IllegalArgumentException) {
            null
        }
    }

}
