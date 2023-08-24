package fr.social.gouv.agora.usecase.qagModerating

import fr.social.gouv.agora.domain.QagModerating
import fr.social.gouv.agora.domain.SupportQag
import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.usecase.qag.QagInfoWithSupportAndThematique
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

    fun toQagModerating(qag: QagInfoWithSupportAndThematique, userId: String): QagModerating {
        return toQagModerating(
            qagInfo = qag.qagInfo,
            thematique = qag.thematique,
            supportQag = SupportQag(
                supportCount = qag.supportQagInfoList.size,
                isSupportedByUser = qag.supportQagInfoList.any { supportQagInfo -> supportQagInfo.userId == userId }
            )
        )
    }

}