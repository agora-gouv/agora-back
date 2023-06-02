package fr.social.gouv.agora.infrastructure.qagPaginated

import fr.social.gouv.agora.domain.QagPreview
import fr.social.gouv.agora.infrastructure.qag.SupportQagJson
import fr.social.gouv.agora.infrastructure.qagHome.QagPreviewJson
import fr.social.gouv.agora.infrastructure.thematique.ThematiqueNoIdJson
import fr.social.gouv.agora.usecase.qagPaginated.QagsAndMaxPageCount
import org.springframework.stereotype.Component

@Component
class QagPaginatedJsonMapper {

    fun toJson(qagsAndMaxPageCount: QagsAndMaxPageCount): QagPaginatedJson {
        return QagPaginatedJson(
            maxPageNumber = qagsAndMaxPageCount.maxPageCount,
            qags = qagsAndMaxPageCount.qags.map { domain -> toJson(domain) },
        )
    }

    private fun toJson(domain: QagPreview): QagPreviewJson {
        return QagPreviewJson(
            qagId = domain.id,
            thematique = ThematiqueNoIdJson(
                label = domain.thematique.label,
                picto = domain.thematique.picto,
            ),
            title = domain.title,
            username = domain.username,
            date = domain.date.toString(),
            support = SupportQagJson(
                supportCount = domain.support.supportCount,
                isSupportedByUser = domain.support.isSupportedByUser,
            )
        )
    }

}