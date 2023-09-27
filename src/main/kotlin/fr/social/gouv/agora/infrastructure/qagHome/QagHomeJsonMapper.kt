package fr.social.gouv.agora.infrastructure.qagHome

import fr.social.gouv.agora.domain.QagPreview
import fr.social.gouv.agora.domain.QagSelectedForResponse
import fr.social.gouv.agora.domain.ResponseQagPreview
import fr.social.gouv.agora.infrastructure.qag.QagJsonMapper
import fr.social.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import org.springframework.stereotype.Component

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

@Component
class QagHomeJsonMapper(
    private val qagMapper: QagJsonMapper,
    private val thematiqueJsonMapper: ThematiqueJsonMapper,
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
        qagSelectedForResponseList: List<QagSelectedForResponse>,
        responseQagList: List<ResponseQagPreview>,
    ): QagResponsesJson {
        return QagResponsesJson(
            incomingResponses = qagSelectedForResponseList.map { qagSelectedForResponse ->
                IncomingResponseQagPreviewJson(
                    qagId = qagSelectedForResponse.id,
                    thematique = thematiqueJsonMapper.toNoIdJson(qagSelectedForResponse.thematique),
                    title = qagSelectedForResponse.title,
                    support = qagMapper.toJson(qagSelectedForResponse.support)
                )
            },
            responsesList = responseQagList.map { responseQag ->
                ResponseQagPreviewJson(
                    qagId = responseQag.qagId,
                    thematique = thematiqueJsonMapper.toNoIdJson(responseQag.thematique),
                    title = responseQag.title,
                    author = responseQag.author,
                    authorPortraitUrl = responseQag.authorPortraitUrl,
                    responseDate = responseQag.responseDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"),
                )
            },
        )
    }

    private fun qagToJson(qagPreview: QagPreview): QagPreviewJson {
        return QagPreviewJson(
            qagId = qagPreview.id,
            thematique = thematiqueJsonMapper.toNoIdJson(qagPreview.thematique),
            title = qagPreview.title,
            username = qagPreview.username,
            date = qagPreview.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"),
            support = qagMapper.toJson(qagPreview.support),
            isAuthor = qagPreview.isAuthor
        )
    }

}