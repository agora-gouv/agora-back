package fr.social.gouv.agora.infrastructure.qag

import fr.social.gouv.agora.domain.QagModerating
import fr.social.gouv.agora.domain.SupportQag
import fr.social.gouv.agora.infrastructure.utils.UnicodeStringDecoder
import org.springframework.stereotype.Component

@Component
class QagModeratingJsonMapper {

    fun toJson(qagModeratingList: List<QagModerating>, totalNumberQagModerating: Int): QagModeratingHomeJson {
        return QagModeratingHomeJson(
            totalNumber = totalNumberQagModerating,
            qagsToModerate = qagModeratingList.map { qagModerating ->
                QagModeratingJson(
                    id = qagModerating.id,
                    thematique = ThematiqueJson(
                        label = qagModerating.thematique.label,
                        picto = UnicodeStringDecoder.decodeUnicode(qagModerating.thematique.picto),
                    ),
                    title = qagModerating.title,
                    description = qagModerating.description,
                    date = qagModerating.date.toString(),
                    username = qagModerating.username,
                    support = toJson(qagModerating.support),
                )
            })
    }

    fun toJson(supportQag: SupportQag): SupportQagJson {
        return SupportQagJson(
            supportCount = supportQag.supportCount,
            isSupportedByUser = supportQag.isSupportedByUser,
        )
    }
}
