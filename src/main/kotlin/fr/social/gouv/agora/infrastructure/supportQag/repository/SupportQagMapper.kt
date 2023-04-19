package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.domain.SupportQag
import fr.social.gouv.agora.infrastructure.supportQag.dto.SupportQagDTO
import org.springframework.stereotype.Component

@Component
class SupportQagMapper {

    fun toDomain(supportCount: Int, dto: SupportQagDTO?): SupportQag {
        return SupportQag(
            supportCount = supportCount,
            isSupportedByUser = dto != null,
        )
    }

}