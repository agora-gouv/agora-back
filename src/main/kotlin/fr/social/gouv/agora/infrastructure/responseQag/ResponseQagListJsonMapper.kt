package fr.social.gouv.agora.infrastructure.responseQag

import fr.social.gouv.agora.domain.QagPreview
import fr.social.gouv.agora.domain.ResponseQagPreview
import fr.social.gouv.agora.infrastructure.qag.QagJsonMapper
import fr.social.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import org.springframework.stereotype.Component

@Component
class ResponseQagListJsonMapper(
    private val mapperThematique: ThematiqueJsonMapper,
    private val qagMapper: QagJsonMapper,
) {
    fun toJson(
        responseQagList: List<ResponseQagPreview>,
        qagPopularList: List<QagPreview>,
    ): ResponseQagListJson {
        return ResponseQagListJson(responsesList = responseQagList.map { domain ->
            ResponseQagPreviewJson(
                qagId = domain.qagId,
                thematique = mapperThematique.toJson(domain.thematique),
                title = domain.title,
                author = domain.author,
                authorPortraitUrl = domain.authorPortraitUrl,
                responseDate = domain.responseDate.toString(),
            )
        },
            qagList = QagListJson(
                popular = qagPopularList.map { domainQag -> QagPopularJson(
                    qagId = domainQag.id,
                    thematique = mapperThematique.toJson(domainQag.thematique),
                    title = domainQag.title,
                    username = domainQag.username,
                    date = domainQag.date.toString(),
                    support = qagMapper.toJson(domainQag.support)
                ) },
                latest = emptyList(),
                supporting = emptyList()
            )
        )
    }
}