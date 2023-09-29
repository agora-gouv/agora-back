package fr.social.gouv.agora.infrastructure.moderatus.repository

import fr.social.gouv.agora.infrastructure.moderatus.dto.ModeratusQagLockDTO
import org.springframework.stereotype.Component
import java.util.*

@Component
class ModeratusQagLockMapper {

    fun toQagId(dto: ModeratusQagLockDTO): String {
        return dto.qagId.toString()
    }

    fun toDto(qagId: String): ModeratusQagLockDTO? {
        return try {
            ModeratusQagLockDTO(
                id = UUID.randomUUID(),
                lockDate = Date(),
                qagId = UUID.fromString(qagId),
            )
        } catch (e: IllegalArgumentException) {
            null
        }
    }

}