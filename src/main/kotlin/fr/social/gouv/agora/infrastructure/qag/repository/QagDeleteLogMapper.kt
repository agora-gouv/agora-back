package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.domain.QagDeleteLog
import fr.social.gouv.agora.infrastructure.qag.dto.QagDeleteLogDTO
import org.springframework.stereotype.Component
import java.util.*

@Component
class QagDeleteLogMapper {

    fun toDto(domain: QagDeleteLog): QagDeleteLogDTO? {
        return try {
            QagDeleteLogDTO(
                id = UUID.randomUUID(),
                qagId = UUID.fromString(domain.qagId),
                userId = UUID.fromString(domain.userId),
                deleteDate = Date(),
            )
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}