package fr.gouv.agora.infrastructure.qagHome

import fr.gouv.agora.domain.QagPreview
import fr.gouv.agora.infrastructure.profile.repository.DateMapper
import fr.gouv.agora.infrastructure.qag.SupportQagJson
import fr.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import fr.gouv.agora.usecase.responseQag.ResponseQagPreviewList
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
        val qagHomePopupTitle = System.getenv("QAG_HOME_POPUP_TITLE")?.takeIf { it.isNotBlank() }
        val qagHomePopupDescription = System.getenv("QAG_HOME_POPUP_DESCRIPTION")?.takeIf { it.isNotBlank() }

        return QagPreviewsJson(
            qagList = QagListJson(
                popular = qagPopularList.map { qag -> qagToJson(qag) },
                latest = qagLatestList.map { qag -> qagToJson(qag) },
                supporting = qagSupportedList.map { qag -> qagToJson(qag) },
            ),
            askQagErrorText = qagErrorText,
            qagPopup = if (qagHomePopupTitle != null && qagHomePopupDescription != null) {
                QagPopupJson(
                    title = qagHomePopupTitle,
                    description = qagHomePopupDescription,
                )
            } else null,
        )
    }

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