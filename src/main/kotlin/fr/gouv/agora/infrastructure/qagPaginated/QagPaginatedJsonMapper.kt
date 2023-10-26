package fr.gouv.agora.infrastructure.qagPaginated

import fr.gouv.agora.domain.QagPreview
import fr.gouv.agora.infrastructure.profile.repository.DateMapper
import fr.gouv.agora.infrastructure.qag.SupportQagJson
import fr.gouv.agora.infrastructure.qagHome.QagPreviewJson
import fr.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import fr.gouv.agora.usecase.qagPaginated.QagsAndMaxPageCount
import org.springframework.stereotype.Component

@Component
class QagPaginatedJsonMapper(
    private val thematiqueJsonMapper: ThematiqueJsonMapper,
    private val dateMapper: DateMapper,
) {

    fun toJson(qagsAndMaxPageCount: QagsAndMaxPageCount): QagPaginatedJson {
        return QagPaginatedJson(
            maxPageNumber = qagsAndMaxPageCount.maxPageCount,
            qags = qagsAndMaxPageCount.qags.map { domain -> toJson(domain) },
        )
    }

    private fun toJson(domain: QagPreview): QagPreviewJson {
        return QagPreviewJson(
            qagId = domain.id,
            thematique = thematiqueJsonMapper.toNoIdJson(domain.thematique),
            title = domain.title,
            username = domain.username,
            date = dateMapper.toFormattedDate(domain.date),
            support = SupportQagJson(
                supportCount = domain.supportCount,
                isSupportedByUser = domain.isSupportedByUser,
            ),
            isAuthor = domain.isAuthor,
        )
    }

}