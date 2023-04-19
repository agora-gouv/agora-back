package fr.social.gouv.agora.infrastructure.qag

import fr.social.gouv.agora.domain.Qag
import fr.social.gouv.agora.domain.SupportQag
import org.springframework.stereotype.Component

@Component
class QagJsonMapper {

    fun toJson(qag: Qag, supportQag: SupportQag?): QagJson {
        return QagJson(
            id = qag.id,
            thematiqueId = qag.thematiqueId,
            title = qag.title,
            description = qag.description,
            date = qag.date,
            username = qag.username,
            support = supportQag?.let {
                SupportQagJson(
                    supportCount = supportQag.supportCount,
                    isSupportedByUser = supportQag.isSupportedByUser,
                )
            }
        )
    }

}
