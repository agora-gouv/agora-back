package fr.gouv.agora.infrastructure.qagHome

import fr.gouv.agora.domain.QagPreview
import fr.gouv.agora.infrastructure.common.DateMapper
import fr.gouv.agora.infrastructure.qag.SupportQagJson
import fr.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import fr.gouv.agora.usecase.responseQag.ResponseQagPreviewList
import org.springframework.stereotype.Component

@Component
class QagHomeJsonMapper(
    private val thematiqueJsonMapper: ThematiqueJsonMapper,
    private val dateMapper: DateMapper,
) {

    fun toResponsesJson(
        previewList: ResponseQagPreviewList,
    ): QagResponsesJson {
        return QagResponsesJson(
            incomingResponses = previewList.incomingResponses.map { qag ->
                IncomingResponseQagPreviewJson(
                    qagId = qag.id,
                    thematique = thematiqueJsonMapper.toNoIdJson(qag.thematique),
                    title = qag.title,
                    support = SupportQagJson(
                        supportCount = qag.supportCount,
                        isSupportedByUser = true,
                    ),
                    order = qag.order,
                    previousMondayDate = dateMapper.toFormattedDate(qag.dateLundiPrecedent),
                    nextMondayDate = dateMapper.toFormattedDate(qag.dateLundiSuivant),
                )
            },
            responsesList = previewList.responses.map { responseQag ->
                ResponseQagPreviewJson(
                    qagId = responseQag.qagId,
                    thematique = thematiqueJsonMapper.toNoIdJson(responseQag.thematique),
                    title = responseQag.title,
                    author = responseQag.author,
                    authorPortraitUrl = responseQag.authorPortraitUrl,
                    responseDate = dateMapper.toFormattedDate(responseQag.responseDate),
                    order = responseQag.order,
                )
            },
        )
    }

    fun toJson(qagPreviewList: List<QagPreview>): QagPreviewListJson {
        return QagPreviewListJson(
            results = qagPreviewList.map { qagPreview -> qagToJson(qagPreview) }
        )
    }

    fun toQagAskStatusJson(askQagErrorText: String?): QagAskStatusJson {
        return QagAskStatusJson(askQagErrorText = askQagErrorText)
    }

    private fun qagToJson(qagPreview: QagPreview): QagPreviewJson {
        return QagPreviewJson(
            qagId = qagPreview.id,
            thematique = thematiqueJsonMapper.toNoIdJson(qagPreview.thematique),
            title = qagPreview.title,
            description = qagPreview.description,
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
