package fr.social.gouv.agora.infrastructure.qagHome

import fr.social.gouv.agora.domain.QagPreview
import fr.social.gouv.agora.domain.ResponseQagPreview
import fr.social.gouv.agora.infrastructure.qag.QagJsonMapper
import fr.social.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import org.springframework.stereotype.Component

@Component
class QagHomeJsonMapper(
    private val mapperThematique: ThematiqueJsonMapper,
    private val qagMapper: QagJsonMapper,
) {
    fun toJson(
        responseQagList: List<ResponseQagPreview>,
        qagPopularList: List<QagPreview>,
        qagLatestList: List<QagPreview>,
        qagSupportedList: List<QagPreview>,
    ): QagHomeJson {
        return QagHomeJson(
            responsesList = responseQagList.map { domain ->
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
                popular = qagPopularList.map { domainQag -> qagToJson(domainQag) },
                latest = qagLatestList.map { domainQag -> qagToJson(domainQag) },
                supporting = qagSupportedList.map { domainQag -> qagToJson(domainQag) },
            )
        )
    }

    private fun qagToJson(domainQag: QagPreview): QagPreviewJson {
        return QagPreviewJson(
            qagId = domainQag.id,
            thematique = mapperThematique.toJson(domainQag.thematique),
            title = domainQag.title,
            username = domainQag.username,
            date = domainQag.date.toString(),
            support = qagMapper.toJson(domainQag.support)
        )
    }
}