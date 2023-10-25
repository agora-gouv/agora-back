package fr.social.gouv.agora.infrastructure.qagPaginated

import fr.social.gouv.agora.domain.QagPreview
import fr.social.gouv.agora.infrastructure.profile.repository.DateMapper
import fr.social.gouv.agora.infrastructure.qag.SupportQagJson
import fr.social.gouv.agora.infrastructure.qagHome.QagPreviewJson
import fr.social.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import fr.social.gouv.agora.usecase.qagPaginated.QagsAndMaxPageCount
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