package fr.gouv.agora.infrastructure.qagHome

import fr.gouv.agora.domain.IncomingResponsePreview
import fr.gouv.agora.domain.QagPreview
import fr.gouv.agora.domain.ResponseQagPreview
import fr.gouv.agora.infrastructure.profile.repository.DateMapper
import fr.gouv.agora.infrastructure.qag.SupportQagJson
import fr.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import org.springframework.stereotype.Component

@Component
class QagHomeJsonMapper(
    private val thematiqueJsonMapper: ThematiqueJsonMapper,
    private val dateMapper: DateMapper,
) {
    fun toJson(
        qagPopularList: List<QagPreview>,
        qagLatestList: List<QagPreview>,
        qagSupportedList: List<QagPreview>,
        qagErrorText: String?,
    ): QagPreviewsJson {
        return QagPreviewsJson(
            qagList = QagListJson(
                popular = qagPopularList.map { qag -> qagToJson(qag) },
                latest = qagLatestList.map { qag -> qagToJson(qag) },
                supporting = qagSupportedList.map { qag -> qagToJson(qag) },
            ),
            askQagErrorText = qagErrorText,
        )
    }

    fun toResponsesJson(
        incomingResponses: List<IncomingResponsePreview>,
        responses: List<ResponseQagPreview>,
    ): QagResponsesJson {
        return QagResponsesJson(
            incomingResponses = incomingResponses.map { qag ->
                IncomingResponseQagPreviewJson(
                    qagId = qag.id,
                    thematique = thematiqueJsonMapper.toNoIdJson(qag.thematique),
                    title = qag.title,
                    support = SupportQagJson(
                        supportCount = qag.supportCount,
                        isSupportedByUser = true,
                    )
                )
            },
            responsesList = responses.map { responseQag ->
                ResponseQagPreviewJson(
                    qagId = responseQag.qagId,
                    thematique = thematiqueJsonMapper.toNoIdJson(responseQag.thematique),
                    title = responseQag.title,
                    author = responseQag.author,
                    authorPortraitUrl = responseQag.authorPortraitUrl,
                    responseDate = dateMapper.toFormattedDate(responseQag.responseDate),
                )
            },
        )
    }

    fun qagToJson(qagPreview: QagPreview): QagPreviewJson {
        return QagPreviewJson(
            qagId = qagPreview.id,
            thematique = thematiqueJsonMapper.toNoIdJson(qagPreview.thematique),
            title = qagPreview.title,
            username = qagPreview.username,
            date = dateMapper.toFormattedDate(qagPreview.date),
            support = SupportQagJson(
                supportCount = qagPreview.supportCount,
                isSupportedByUser = qagPreview.isSupportedByUser,
            ),
            isAuthor = qagPreview.isAuthor
        )
    }

}