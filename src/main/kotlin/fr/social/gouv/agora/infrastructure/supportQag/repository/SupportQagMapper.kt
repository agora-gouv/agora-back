package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.domain.SupportQag
import fr.social.gouv.agora.domain.SupportQagInfo
import fr.social.gouv.agora.domain.SupportQagInserting
import fr.social.gouv.agora.infrastructure.supportQag.dto.SupportQagDTO
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

    fun toDomain(supportCount: Int, dto: SupportQagDTO?): SupportQag {
        return SupportQag(
            supportCount = supportCount,
            isSupportedByUser = dto != null,
        )
    }

    fun toSupportQagInfo(dto: SupportQagDTO): SupportQagInfo {
        return SupportQagInfo(
            qagId = dto.qagId.toString(),
            userId = dto.userId.toString(),
            supportDate = dto.supportDate,
        )
    }
}