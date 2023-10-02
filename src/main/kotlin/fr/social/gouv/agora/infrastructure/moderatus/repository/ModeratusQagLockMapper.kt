package fr.social.gouv.agora.infrastructure.moderatus.repository

import fr.social.gouv.agora.infrastructure.moderatus.dto.ModeratusQagLockDTO
import fr.social.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import org.springframework.stereotype.Component
import java.util.*

@Component
class ModeratusQagLockMapper {

    fun toQagId(dto: ModeratusQagLockDTO): String {
        return dto.qagId.toString()
    }

    fun toDto(qagId: String): ModeratusQagLockDTO? {
        return qagId.toUuidOrNull()?.let { qagUUID ->
            ModeratusQagLockDTO(
                id = UUID.randomUUID(),
                lockDate = Date(),
                qagId = qagUUID,
            )
        }
    }

}