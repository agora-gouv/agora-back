package fr.gouv.agora.infrastructure.qagPaginated

import fr.gouv.agora.domain.HeaderQag
import fr.gouv.agora.domain.QagPreview
import fr.gouv.agora.infrastructure.common.DateMapper
import fr.gouv.agora.infrastructure.qag.SupportQagJson
import fr.gouv.agora.infrastructure.qagHome.HeaderQagJson
import fr.gouv.agora.infrastructure.qagHome.QagPreviewJson
import fr.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import fr.gouv.agora.usecase.qagPaginated.QagsAndMaxPageCountV2
import org.springframework.stereotype.Component

@Component
class QagPaginatedJsonMapper(
    private val thematiqueJsonMapper: ThematiqueJsonMapper,
    private val dateMapper: DateMapper,
) {

    fun toJson(qagsAndMaxPageCount: QagsAndMaxPageCountV2): QagPaginatedJsonV2 {
        return QagPaginatedJsonV2(
            maxPageNumber = qagsAndMaxPageCount.maxPageCount,
            header = qagsAndMaxPageCount.headerQag?.let(::toJson),
            qags = qagsAndMaxPageCount.qags.map(::toJson),
        )
    }

    private fun toJson(domain: QagPreview): QagPreviewJson {
        return QagPreviewJson(
            qagId = domain.id,
            thematique = thematiqueJsonMapper.toNoIdJson(domain.thematique),
            title = domain.title,
            description = domain.description,
            username = domain.username,
            date = dateMapper.toFormattedDate(domain.date),
            support = SupportQagJson(
                supportCount = domain.supportCount,
                isSupportedByUser = domain.isSupportedByUser,
            ),
            isAuthor = domain.isAuthor,
        )
    }

    private fun toJson(headerQag: HeaderQag): HeaderQagJson {
        return HeaderQagJson(
            headerId = headerQag.headerId,
            title = headerQag.title,
            message = headerQag.message
        )
    }

}
