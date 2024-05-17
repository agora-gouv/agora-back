package fr.gouv.agora.infrastructure.qag

import fr.gouv.agora.domain.*
import fr.gouv.agora.infrastructure.common.DateMapper
import fr.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import fr.gouv.agora.infrastructure.utils.StringUtils
import fr.gouv.agora.usecase.qag.repository.QagInsertionResult
import org.springframework.stereotype.Component
import java.util.*

@Component
class QagJsonMapper(
    private val thematiqueJsonMapper: ThematiqueJsonMapper,
    private val dateMapper: DateMapper,
) {

    fun toJson(qagWithUserData: QagWithUserData): QagJson {
        return QagJson(
            id = qagWithUserData.qagDetails.id,
            thematique = thematiqueJsonMapper.toNoIdJson(qagWithUserData.qagDetails.thematique),
            title = qagWithUserData.qagDetails.title,
            description = qagWithUserData.qagDetails.description,
            date = dateMapper.toFormattedDate(qagWithUserData.qagDetails.date),
            username = qagWithUserData.qagDetails.username,
            canShare = qagWithUserData.canShare,
            canSupport = qagWithUserData.canSupport,
            canDelete = qagWithUserData.canDelete,
            support = SupportQagJson(
                supportCount = qagWithUserData.qagDetails.supportCount,
                isSupportedByUser = qagWithUserData.isSupportedByUser,
            ),
            isAuthor = qagWithUserData.isAuthor,
            response = buildResponseQagVideoJson(qagWithUserData),
            textResponse = buildResponseQagTextJson(qagWithUserData),
        )
    }

    fun toJson(qagInsertionResult: QagInsertionResult.Success): QagInsertionResultJson {
        return QagInsertionResultJson(qagId = qagInsertionResult.qagInfo.id)
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

    private fun buildResponseQagVideoJson(qagWithUserData: QagWithUserData): ResponseQagVideoJson? {
        return qagWithUserData.qagDetails.response?.let { response ->
            if (response is ResponseQagVideo)
                ResponseQagVideoJson(
                    author = response.author,
                    authorDescription = response.authorDescription,
                    responseDate = dateMapper.toFormattedDate(response.responseDate),
                    videoUrl = response.videoUrl,
                    videoWidth = response.videoWidth,
                    videoHeight = response.videoHeight,
                    transcription = StringUtils.unescapeLineBreaks(response.transcription),
                    feedbackQuestion = response.feedbackQuestion,
                    feedbackStatus = qagWithUserData.hasGivenFeedback,
                    feedbackUserResponse = qagWithUserData.isHelpful,
                    feedbackResults = qagWithUserData.qagDetails.feedbackResults?.let { feedbackResults ->
                        FeedbackResultsJson(
                            positiveRatio = feedbackResults.positiveRatio,
                            negativeRatio = feedbackResults.negativeRatio,
                            count = feedbackResults.count,
                        )
                    },
                    additionalInfo = response.additionalInfo?.let {
                        AdditionalInfoJson(
                            title = it.additionalInfoTitle,
                            description = response.additionalInfo.additionalInfoDescription
                        )
                    }
                )
            else null
        }
    }

    private fun buildResponseQagTextJson(qagWithUserData: QagWithUserData): ResponseQagTextJson? {
        return qagWithUserData.qagDetails.response?.let { response ->
            if (response is ResponseQagText)
                ResponseQagTextJson(
                    responseLabel = response.responseLabel,
                    responseText = response.responseText,
                    feedbackQuestion = response.feedbackQuestion,
                    feedbackStatus = qagWithUserData.hasGivenFeedback,
                    feedbackUserResponse = qagWithUserData.isHelpful,
                    feedbackResults = qagWithUserData.qagDetails.feedbackResults?.let
                    { feedbackResults ->
                        FeedbackResultsJson(
                            positiveRatio = feedbackResults.positiveRatio,
                            negativeRatio = feedbackResults.negativeRatio,
                            count = feedbackResults.count,
                        )
                    }
                ) else null
        }
    }

}
