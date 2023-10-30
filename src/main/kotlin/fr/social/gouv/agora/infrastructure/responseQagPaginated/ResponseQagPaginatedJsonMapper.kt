package fr.social.gouv.agora.infrastructure.responseQagPaginated

import fr.social.gouv.agora.domain.ResponseQagPreview
import fr.social.gouv.agora.infrastructure.profile.repository.DateMapper
import fr.social.gouv.agora.infrastructure.qagHome.ResponseQagPreviewJson
import fr.social.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import fr.social.gouv.agora.usecase.responseQag.ResponseQagPaginatedList
import org.springframework.stereotype.Component

@Component
class ResponseQagPaginatedJsonMapper(
    private val thematiqueJsonMapper: ThematiqueJsonMapper,
    private val dateMapper: DateMapper,
) {

    fun toJson(responseQagPaginatedList: ResponseQagPaginatedList): ResponseQagPaginatedJson {
        return ResponseQagPaginatedJson(
            maxPageNumber = responseQagPaginatedList.maxPageNumber,
            responses = responseQagPaginatedList.listResponseQag.map { domain -> toJson(domain) },
        )
    }

    private fun toJson(domain: ResponseQagPreview): ResponseQagPreviewJson {
        return ResponseQagPreviewJson(
            qagId = domain.qagId,
            thematique = thematiqueJsonMapper.toNoIdJson(domain.thematique),
            title = domain.title,
            author = domain.author,
            authorPortraitUrl = domain.authorPortraitUrl,
            responseDate = dateMapper.toFormattedDate(domain.responseDate),
        )
    }
}