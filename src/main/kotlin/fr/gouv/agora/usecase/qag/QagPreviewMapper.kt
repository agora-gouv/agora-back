package fr.gouv.agora.usecase.qag

import fr.gouv.agora.domain.QagPreview
import fr.gouv.agora.domain.QagWithSupportCount
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import org.springframework.stereotype.Component

@Component
class QagPreviewMapper {

    fun toPreview(qag: QagWithSupportCount, isSupportedByUser: Boolean, isAuthor: Boolean): QagPreview {
        return toPreview(
            qag = qag.qagInfo,
            thematique = qag.thematique,
            isSupportedByUser = isSupportedByUser,
            isAuthor = isAuthor,
        )
    }

    fun toPreview(
        qag: QagInfoWithSupportCount,
        thematique: Thematique,
        isSupportedByUser: Boolean,
        isAuthor: Boolean,
    ): QagPreview {
        return QagPreview(
            id = qag.id,
            thematique = thematique,
            title = qag.title,
            username = qag.username,
            date = qag.date,
            supportCount = qag.supportCount,
            isSupportedByUser = isSupportedByUser,
            isAuthor = isAuthor,
        )
    }

}