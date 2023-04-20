package fr.social.gouv.agora.infrastructure.qag

import fr.social.gouv.agora.domain.Qag
import org.springframework.stereotype.Component

@Component
class QagJsonMapper {

    fun toJson(qag: Qag): QagJson {
        return QagJson(
            id = qag.id,
            thematiqueId = qag.thematiqueId,
            title = qag.title,
            description = qag.description,
            date = qag.date,
            username = qag.username,
            support = qag.support?.let { support ->
                SupportQagJson(
                    supportCount = support.supportCount,
                    isSupportedByUser = support.isSupportedByUser,
                )
            }
        )
    }

}
