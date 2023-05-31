package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagPreview
import fr.social.gouv.agora.domain.SupportQag
import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import org.springframework.stereotype.Component

@Component
class QagPreviewMapper {

    fun toPreview(qagInfo: QagInfo, thematique: Thematique, supportQag: SupportQag): QagPreview {
        return QagPreview(
            id = qagInfo.id,
            thematique = thematique,
            title = qagInfo.title,
            username = qagInfo.username,
            date = qagInfo.date,
            support = supportQag,
        )
    }

}