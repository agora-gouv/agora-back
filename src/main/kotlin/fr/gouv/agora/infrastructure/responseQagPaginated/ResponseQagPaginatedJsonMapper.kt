package fr.gouv.agora.infrastructure.responseQagPaginated

import fr.gouv.agora.domain.ResponseQagPreviewWithoutOrder
import fr.gouv.agora.infrastructure.profile.repository.DateMapper
import fr.gouv.agora.infrastructure.qagHome.ResponseQagPreviewWithoutOrderJson
import fr.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import fr.gouv.agora.usecase.responseQag.ResponseQagPaginatedList
import org.springframework.stereotype.Component

@Component
class ResponseQagPaginatedJsonMapper(
    private val thematiqueJsonMapper: ThematiqueJsonMapper,
    private val dateMapper: DateMapper,
) {

    fun toJson(responseQagPaginatedList: ResponseQagPaginatedList): ResponseQagPaginatedJson {
        return ResponseQagPaginatedJson(
            maxPageNumber = responseQagPaginatedList.maxPageNumber,
            responses = responseQagPaginatedList.responsesQag.map { domain -> toJson(domain) },
        )
    }

    private fun toJson(domain: ResponseQagPreviewWithoutOrder): ResponseQagPreviewWithoutOrderJson {
        return ResponseQagPreviewWithoutOrderJson(
            qagId = domain.qagId,
            thematique = thematiqueJsonMapper.toNoIdJson(domain.thematique),
            title = domain.title,
            author = domain.author,
            authorPortraitUrl = domain.authorPortraitUrl,
            responseDate = dateMapper.toFormattedDate(domain.responseDate),
        )
    }
}