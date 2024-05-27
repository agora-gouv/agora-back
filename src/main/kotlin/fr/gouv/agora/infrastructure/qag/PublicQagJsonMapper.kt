package fr.gouv.agora.infrastructure.qag

import fr.gouv.agora.domain.QagDetails
import fr.gouv.agora.domain.ResponseQag
import fr.gouv.agora.domain.ResponseQagText
import fr.gouv.agora.domain.ResponseQagVideo
import fr.gouv.agora.infrastructure.common.DateMapper
import fr.gouv.agora.infrastructure.thematique.ThematiqueNoIdJson
import org.springframework.stereotype.Component

@Component
class PublicQagJsonMapper(private val dateMapper: DateMapper) {

    fun toJson(qagDetails: QagDetails): PublicQagJson {
        return PublicQagJson(
            id = qagDetails.id,
            thematique = ThematiqueNoIdJson(
                label = qagDetails.thematique.label,
                picto = qagDetails.thematique.picto,
            ),
            title = qagDetails.title,
            description = qagDetails.description,
            date = dateMapper.toFormattedDate(qagDetails.date),
            username = qagDetails.username,
            supportCount = qagDetails.supportCount,
            videoResponse = toResponseVideoJson(qagDetails.response),
            textResponse = toResponseTextJson(qagDetails.response),
        )
    }

    private fun toResponseVideoJson(responseQag: ResponseQag?): PublicQagResponseVideoJson? {
        if (responseQag == null || responseQag !is ResponseQagVideo) return null
        return PublicQagResponseVideoJson(
            author = responseQag.author,
            authorDescription = responseQag.authorDescription,
            authorPortraitUrl = responseQag.authorPortraitUrl,
            responseDate = dateMapper.toFormattedDate(responseQag.responseDate),
            videoUrl = responseQag.videoUrl,
            videoWidth = responseQag.videoWidth,
            videoHeight = responseQag.videoHeight,
            transcription = responseQag.transcription,
            additionalInfo = responseQag.additionalInfo?.let {
                PublicQagResponseVideoAdditionalInfoJson(
                    title = it.additionalInfoTitle,
                    description = it.additionalInfoDescription,
                )
            },
            feedbackQuestion = responseQag.feedbackQuestion,
        )
    }

    private fun toResponseTextJson(responseQag: ResponseQag?): PublicQagResponseTextJson? {
        if (responseQag == null || responseQag !is ResponseQagText) return null
        return PublicQagResponseTextJson(
            responseLabel = responseQag.responseLabel,
            responseText = responseQag.responseText,
            feedbackQuestion = responseQag.feedbackQuestion,
        )
    }

}