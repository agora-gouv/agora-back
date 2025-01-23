package fr.gouv.agora.infrastructure.responseQag.repository

import fr.gouv.agora.domain.ResponseQag
import fr.gouv.agora.domain.ResponseQagAdditionalInfo
import fr.gouv.agora.domain.ResponseQagText
import fr.gouv.agora.domain.ResponseQagVideo
import fr.gouv.agora.infrastructure.responseQag.dto.StrapiResponseQag
import fr.gouv.agora.infrastructure.responseQag.dto.StrapiResponseQagText
import fr.gouv.agora.infrastructure.responseQag.dto.StrapiResponseQagVideo
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.toHtml
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import org.springframework.stereotype.Component

@Component
class ResponseQagMapper {
    fun toDomain(responseBody: StrapiDTO<StrapiResponseQag>): List<ResponseQag> {
        return responseBody.data.map {
            val response = it.attributes

            when (val responseContent = response.reponseType.first()) {
                is StrapiResponseQagText -> {
                    ResponseQagText(
                        author = response.auteur,
                        authorPortraitUrl = response.auteurPortraitUrl,
                        responseDate = response.reponseDate.toDate(),
                        feedbackQuestion = response.feedbackQuestion,
                        qagId = response.questionId,
                        responseText = "<body>${responseContent.text.toHtml()}</body>",
                        responseLabel = responseContent.label,
                    )
                }

                is StrapiResponseQagVideo -> {
                    ResponseQagVideo(
                        author = response.auteur,
                        authorPortraitUrl = response.auteurPortraitUrl,
                        responseDate = response.reponseDate.toDate(),
                        feedbackQuestion = response.feedbackQuestion,
                        qagId = response.questionId,
                        authorDescription = responseContent.auteurDescription,
                        videoUrl = responseContent.urlVideo,
                        videoWidth = responseContent.videoWidth,
                        videoHeight = responseContent.videoHeight,
                        transcription = responseContent.transcription,
                        videoTitle = responseContent.pageTitle,
                        additionalInfo = if (responseContent.hasInformationAdditionnelle()) {
                            ResponseQagAdditionalInfo(
                                responseContent.informationAdditionnelleTitre!!,
                                "<body>${responseContent.informationAdditionnelleDescription!!.toHtml()}</body>"
                            )
                        } else null
                    )
                }
            }
        }
    }
}
