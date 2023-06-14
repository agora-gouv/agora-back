package fr.social.gouv.agora.infrastructure.qag

import fr.social.gouv.agora.domain.Qag
import fr.social.gouv.agora.domain.QagInserting
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.domain.SupportQag
import fr.social.gouv.agora.infrastructure.utils.StringUtils
import fr.social.gouv.agora.infrastructure.utils.UnicodeStringDecoder
import fr.social.gouv.agora.usecase.qag.repository.QagInsertionResult
import org.springframework.stereotype.Component
import java.util.*

@Component
class QagJsonMapper {

    fun toJson(qag: Qag): QagJson {
        val response = buildResponseQagJson(qag)
        val support = if (response == null) {
            buildSupportQagJson(qag)
        } else null

        return QagJson(
            id = qag.id,
            thematique = ThematiqueJson(
                label = qag.thematique.label,
                picto = UnicodeStringDecoder.decodeUnicode(qag.thematique.picto),
            ),
            title = qag.title,
            description = qag.description,
            date = qag.date,
            username = qag.username,
            canShareQag = qag.status == QagStatus.MODERATED_ACCEPTED,
            support = support,
            response = response,
        )
    }

    fun toJson(supportQag: SupportQag): SupportQagJson {
        return SupportQagJson(
            supportCount = supportQag.supportCount,
            isSupportedByUser = supportQag.isSupportedByUser,
        )
    }

    fun toJson(qagInsertionResult: QagInsertionResult.Success): QagInsertionResultJson {
        return QagInsertionResultJson(qagId = qagInsertionResult.qagId.toString())
    }

    fun toDomain(json: QagInsertingJson, userId: String): QagInserting {
        return QagInserting(
            thematiqueId = json.thematiqueId,
            title = json.title,
            description = json.description,
            date = Calendar.getInstance().time,
            status = QagStatus.OPEN,
            username = json.author,
            userId = userId,
        )
    }

    private fun buildSupportQagJson(qag: Qag): SupportQagJson? {
        return qag.support?.let { support ->
            SupportQagJson(
                supportCount = support.supportCount,
                isSupportedByUser = support.isSupportedByUser,
            )
        }
    }

    private fun buildResponseQagJson(qag: Qag): ResponseQagJson? {
        return qag.response?.let { response ->
            ResponseQagJson(
                author = response.author,
                authorDescription = response.authorDescription,
                responseDate = response.responseDate,
                videoUrl = response.videoUrl,
                transcription = StringUtils.unescapeLineBreaks(response.transcription),
                feedbackStatus = qag.feedback?.isExist ?: false,
            )
        }
    }
}
