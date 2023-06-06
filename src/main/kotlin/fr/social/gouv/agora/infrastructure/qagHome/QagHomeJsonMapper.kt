package fr.social.gouv.agora.infrastructure.qagHome

import fr.social.gouv.agora.domain.QagPreview
import fr.social.gouv.agora.domain.ResponseQagPreview
import fr.social.gouv.agora.infrastructure.qag.QagJsonMapper
import fr.social.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import org.springframework.stereotype.Component

@Component
class QagHomeJsonMapper(
    private val qagMapper: QagJsonMapper,
    private val thematiqueJsonMapper: ThematiqueJsonMapper,
) {
    fun toJson(
        responseQagList: List<ResponseQagPreview>,
        qagPopularList: List<QagPreview>,
        qagLatestList: List<QagPreview>,
        qagSupportedList: List<QagPreview>,
        qagErrorText: String?,
    ): QagHomeJson {
        return QagHomeJson(
            responsesList = responseQagList.map { domain ->
                ResponseQagPreviewJson(
                    qagId = domain.qagId,
                    thematique = thematiqueJsonMapper.toNoIdJson(domain.thematique),
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
            ),
            askQagErrorText = qagErrorText,
        )
    }

    private fun qagToJson(qagPreview: QagPreview): QagPreviewJson {
        return QagPreviewJson(
            qagId = qagPreview.id,
            thematique = thematiqueJsonMapper.toNoIdJson(qagPreview.thematique),
            title = qagPreview.title,
            username = qagPreview.username,
            date = qagPreview.date.toString(),
            support = qagMapper.toJson(qagPreview.support)
        )
    }
}