package fr.social.gouv.agora.infrastructure.qagModerating

import fr.social.gouv.agora.domain.QagModerating
import fr.social.gouv.agora.domain.SupportQag
import fr.social.gouv.agora.infrastructure.qag.SupportQagJson
import fr.social.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import org.springframework.stereotype.Component

@Component
class QagModeratingJsonMapper(private val thematiqueJsonMapper: ThematiqueJsonMapper) {

    fun toJson(qagModeratingList: List<QagModerating>, totalNumberQagModerating: Int): QagModeratingHomeJson {
        return QagModeratingHomeJson(
            totalNumber = totalNumberQagModerating,
            qagsToModerate = qagModeratingList.map { qagModerating ->
                QagModeratingJson(
                    id = qagModerating.id,
                    thematique = thematiqueJsonMapper.toNoIdJson(qagModerating.thematique),
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
