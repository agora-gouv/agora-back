package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagModerating
import fr.social.gouv.agora.domain.SupportQag
import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import org.springframework.stereotype.Component

@Component
class QagModeratingMapper {

    fun toQagModerating(qagInfo: QagInfo, thematique: Thematique, supportQag: SupportQag): QagModerating {
        return QagModerating(
            id = qagInfo.id,
            thematique = thematique,
            title = qagInfo.title,
            description = qagInfo.description,
            username = qagInfo.username,
            date = qagInfo.date,
            support = supportQag,
        )
    }

}