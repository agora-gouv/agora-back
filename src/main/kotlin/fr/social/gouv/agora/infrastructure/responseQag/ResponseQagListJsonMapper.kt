package fr.social.gouv.agora.infrastructure.responseQag

import fr.social.gouv.agora.domain.ResponseQagPreview
import fr.social.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import org.springframework.stereotype.Component

@Component
class ResponseQagListJsonMapper(
    private val mapperThematique: ThematiqueJsonMapper,
) {
    fun toJson(
        domainList: List<ResponseQagPreview>,
    ): ResponseQagListJson {
        return ResponseQagListJson(responsesList = domainList.map { domain ->
            ResponseQagPreviewJson(
                qagId = domain.qagId,
                thematique = mapperThematique.toJson(domain.thematique),
                title = domain.title,
                author = domain.author,
                authorPortraitUrl = domain.authorPortraitUrl,
                responseDate = domain.responseDate.toString(),
            )
        }, qagList= QagListJson(popular = emptyList(), latest = emptyList(), supporting = emptyList()))
    }
}