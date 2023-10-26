package fr.gouv.agora.infrastructure.qag

import fr.gouv.agora.domain.QagInserting
import fr.gouv.agora.domain.QagStatus
import fr.gouv.agora.domain.QagWithUserData
import fr.gouv.agora.infrastructure.profile.repository.DateMapper
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
            response = buildResponseQagJson(qagWithUserData),
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

    private fun buildResponseQagJson(qagWithUserData: QagWithUserData): ResponseQagJson? {
        return qagWithUserData.qagDetails.response?.let { response ->
            ResponseQagJson(
                author = response.author,
                authorDescription = response.authorDescription,
                responseDate = dateMapper.toFormattedDate(response.responseDate),
                videoUrl = response.videoUrl,
                videoWidth = response.videoWidth,
                videoHeight = response.videoHeight,
                transcription = StringUtils.unescapeLineBreaks(response.transcription),
                feedbackStatus = qagWithUserData.hasGivenFeedback,
                feedbackResults = qagWithUserData.qagDetails.feedbackResults?.let { feedbackResults ->
                    FeedbackResultsJson(
                        positiveRatio = feedbackResults.positiveRatio,
                        negativeRatio = feedbackResults.negativeRatio,
                        count = feedbackResults.count,
                    )
                }
            )
        }
    }
}
