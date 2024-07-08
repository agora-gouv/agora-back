package fr.gouv.agora.infrastructure.consultation

import fr.gouv.agora.domain.ConsultationDetailsV2WithInfo
import fr.gouv.agora.domain.ConsultationUpdateHistoryStatus
import fr.gouv.agora.domain.ConsultationUpdateHistoryType
import fr.gouv.agora.domain.ConsultationUpdateInfoV2
import fr.gouv.agora.infrastructure.common.DateMapper
import fr.gouv.agora.infrastructure.consultation.ConsultationDetailsV2Json.Body
import fr.gouv.agora.infrastructure.consultation.ConsultationDetailsV2Json.ConsultationDates
import fr.gouv.agora.infrastructure.consultation.ConsultationDetailsV2Json.FeedbackQuestion
import fr.gouv.agora.infrastructure.consultation.ConsultationDetailsV2Json.Footer
import fr.gouv.agora.infrastructure.consultation.ConsultationDetailsV2Json.Goal
import fr.gouv.agora.infrastructure.consultation.ConsultationDetailsV2Json.History
import fr.gouv.agora.infrastructure.consultation.ConsultationDetailsV2Json.InfoHeader
import fr.gouv.agora.infrastructure.consultation.ConsultationDetailsV2Json.ParticipationInfo
import fr.gouv.agora.infrastructure.consultation.ConsultationDetailsV2Json.QuestionsInfo
import fr.gouv.agora.infrastructure.consultation.ConsultationDetailsV2Json.ResponsesInfo
import fr.gouv.agora.infrastructure.consultation.ConsultationDetailsV2Json.Section
import fr.gouv.agora.infrastructure.thematique.ThematiqueNoIdJson
import org.springframework.stereotype.Component

@Component
class ConsultationDetailsV2JsonMapper(private val dateMapper: DateMapper) {

    companion object {
        private const val SHARE_TEXT_REPLACE_TITLE_PATTERN = "{title}"
        private const val SHARE_TEXT_REPLACE_URL_PATTERN = "{url}"
        private const val SHARE_TEXT_CONSULTATION_PATH = "/consultations/"
    }

    fun toJson(consultationDetails: ConsultationDetailsV2WithInfo): ConsultationDetailsV2Json {
        return ConsultationDetailsV2Json(
            title = consultationDetails.consultation.title,
            coverUrl = consultationDetails.consultation.detailsCoverUrl,
            thematique = ThematiqueNoIdJson(
                label = consultationDetails.consultation.thematique.label,
                picto = consultationDetails.consultation.thematique.picto,
            ),
            questionsInfo = buildQuestionsInfo(consultationDetails),
            consultationDates = buildConsultationDates(consultationDetails),
            responsesInfo = buildResponsesInfo(consultationDetails),
            infoHeader = buildInfoHeader(consultationDetails),
            body = buildBody(consultationDetails),
            participationInfo = buildParticipationInfo(consultationDetails),
            downloadAnalysisUrl = consultationDetails.update.downloadAnalysisUrl,
            feedbackQuestion = buildFeedbackQuestion(consultationDetails),
            footer = buildFooter(consultationDetails),
            goals = buildGoals(consultationDetails),
            history = buildHistory(consultationDetails),
            updateId = consultationDetails.update.id,
            shareText = buildShareText(consultationDetails),
        )
    }

    private fun buildShareText(consultationDetails: ConsultationDetailsV2WithInfo): String {
        return consultationDetails.update.shareTextTemplate
            .replace(
                SHARE_TEXT_REPLACE_URL_PATTERN,
                System.getenv("UNIVERSAL_LINK_URL") + SHARE_TEXT_CONSULTATION_PATH + consultationDetails.consultation.id,
            )
            .replace(SHARE_TEXT_REPLACE_TITLE_PATTERN, consultationDetails.consultation.title)
    }

    private fun buildQuestionsInfo(consultationDetails: ConsultationDetailsV2WithInfo): QuestionsInfo? {
        if (!consultationDetails.update.hasQuestionsInfo) return null
        return QuestionsInfo(
            endDate = dateMapper.toFormattedDate(consultationDetails.consultation.endDate.toLocalDate()),
            questionCount = consultationDetails.consultation.questionCount,
            estimatedTime = consultationDetails.consultation.estimatedTime,
            participantCount = consultationDetails.participantCount,
            participantCountGoal = consultationDetails.consultation.participantCountGoal,
        )
    }

    private fun buildConsultationDates(consultationDetails: ConsultationDetailsV2WithInfo): ConsultationDates? {
        if (!consultationDetails.update.hasQuestionsInfo) return null
        return ConsultationDates(
            startDate = dateMapper.toFormattedDate(consultationDetails.consultation.startDate.toLocalDate()),
            endDate = dateMapper.toFormattedDate(consultationDetails.consultation.endDate.toLocalDate()),
        )
    }

    private fun buildResponsesInfo(consultationDetails: ConsultationDetailsV2WithInfo): ResponsesInfo? {
        return consultationDetails.update.responsesInfo?.let { responsesInfo ->
            ResponsesInfo(
                picto = responsesInfo.picto,
                description = responsesInfo.description,
                actionText = responsesInfo.actionText,
            )
        }
    }

    private fun buildInfoHeader(consultationDetails: ConsultationDetailsV2WithInfo): InfoHeader? {
        return consultationDetails.update.infoHeader?.let { infoHeader ->
            InfoHeader(
                picto = infoHeader.picto,
                description = infoHeader.description,
            )
        }
    }

    private fun buildParticipationInfo(consultationDetails: ConsultationDetailsV2WithInfo): ParticipationInfo? {
        if (!consultationDetails.update.hasParticipationInfo) return null
        return ParticipationInfo(
            participantCount = consultationDetails.participantCount,
            participantCountGoal = consultationDetails.consultation.participantCountGoal,
        )
    }

    private fun buildFeedbackQuestion(consultationDetails: ConsultationDetailsV2WithInfo): FeedbackQuestion? {
        return consultationDetails.update.feedbackQuestion?.let { feedbackQuestion ->
            FeedbackQuestion(
                consultationUpdateId = feedbackQuestion.consultationUpdateId,
                title = feedbackQuestion.title,
                picto = feedbackQuestion.picto,
                description = feedbackQuestion.description,
                results = consultationDetails.isUserFeedbackPositive?.let { isUserFeedbackPositive ->
                    FeedbackQuestion.Results(
                        isUserFeedbackPositive = isUserFeedbackPositive,
                        stats = consultationDetails.feedbackStats?.let { stats ->
                            FeedbackQuestion.Stats(
                                positiveRatio = stats.positiveRatio,
                                negativeRatio = stats.negativeRatio,
                                responseCount = stats.responseCount,
                            )
                        }
                    )
                }
            )
        }
    }

    private fun buildFooter(consultationDetails: ConsultationDetailsV2WithInfo): Footer? {
        return consultationDetails.update.footer?.let { footer ->
            Footer(
                title = footer.title,
                description = footer.description,
            )
        }
    }

    private fun buildGoals(consultationDetails: ConsultationDetailsV2WithInfo): List<Goal>? {
        return consultationDetails.update.goals?.map { goal ->
            Goal(
                picto = goal.picto,
                description = goal.description,
            )
        }
    }

    private fun buildHistory(consultationDetails: ConsultationDetailsV2WithInfo): List<History>? {
        return consultationDetails.history?.map { historyItem ->
            History(
                updateId = historyItem.consultationUpdateId,
                type = when (historyItem.type) {
                    ConsultationUpdateHistoryType.UPDATE -> "update"
                    ConsultationUpdateHistoryType.RESULTS -> "results"
                },
                status = when (historyItem.status) {
                    ConsultationUpdateHistoryStatus.DONE -> "done"
                    ConsultationUpdateHistoryStatus.CURRENT -> "current"
                    ConsultationUpdateHistoryStatus.INCOMING -> "incoming"
                },
                title = historyItem.title,
                date = historyItem.updateDate?.let(dateMapper::toFormattedDate),
                actionText = historyItem.actionText,
            )
        }
    }

    private fun buildBody(consultationDetails: ConsultationDetailsV2WithInfo): Body {
        return Body(
            headerSections = consultationDetails.update.sectionsHeader.map(::buildSection),
            sectionsPreview = consultationDetails.update.bodyPreview.map(::buildSection),
            sections = consultationDetails.update.body.map(::buildSection),
        )
    }

    private fun buildSection(section: ConsultationUpdateInfoV2.Section): Section {
        return when (section) {
            is ConsultationUpdateInfoV2.Section.Title -> Section.Title(title = section.title)
            is ConsultationUpdateInfoV2.Section.RichText -> Section.RichText(description = section.description)
            is ConsultationUpdateInfoV2.Section.Image -> Section.Image(
                url = section.url,
                contentDescription = section.contentDescription,
            )

            is ConsultationUpdateInfoV2.Section.Video -> Section.Video(
                url = section.url,
                width = section.width,
                height = section.height,
                authorInfo = section.authorInfo?.let { authorInfo ->
                    Section.Video.AuthorInfo(
                        name = authorInfo.name,
                        message = authorInfo.message,
                        date = dateMapper.toFormattedDate(authorInfo.date),
                    )
                },
                transcription = section.transcription,
            )

            is ConsultationUpdateInfoV2.Section.FocusNumber -> Section.FocusNumber(
                title = section.title,
                description = section.description,
            )

            is ConsultationUpdateInfoV2.Section.Quote -> Section.Quote(description = section.description)

            is ConsultationUpdateInfoV2.Section.Accordion -> Section.Accordion(
                title = section.title,
                sections = section.sections.map(::buildSection),
            )
        }
    }

}
