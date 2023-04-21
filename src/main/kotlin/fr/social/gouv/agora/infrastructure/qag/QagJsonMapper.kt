package fr.social.gouv.agora.infrastructure.qag

import fr.social.gouv.agora.domain.Qag
import org.springframework.stereotype.Component

@Component
class QagJsonMapper {

    fun toJson(qag: Qag): QagJson {
        val response = buildResponseQagJson(qag)
        val support = if (response == null) {
            buildSupportQagJson(qag)
        } else null

        return QagJson(
            id = qag.id,
            thematiqueId = qag.thematiqueId,
            title = qag.title,
            description = qag.description,
            date = qag.date,
            username = qag.username,
            support = support,
            response = response,
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
                transcription = response.transcription,
                feedbackStatus = null, // TODO Feat-40 or Feat-42
            )
        }
    }

}
