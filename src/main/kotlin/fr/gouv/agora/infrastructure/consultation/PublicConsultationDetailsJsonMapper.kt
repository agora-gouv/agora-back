package fr.gouv.agora.infrastructure.consultation

import fr.gouv.agora.domain.ConsultationDetailsV2WithParticipantCount
import fr.gouv.agora.domain.ConsultationUpdateHistoryStatus
import fr.gouv.agora.domain.ConsultationUpdateHistoryType
import fr.gouv.agora.domain.ConsultationUpdateInfoV2
import fr.gouv.agora.infrastructure.common.DateMapper
import fr.gouv.agora.infrastructure.consultation.PublicConsultationDetailsJson.Body
import fr.gouv.agora.infrastructure.consultation.PublicConsultationDetailsJson.ConsultationDates
import fr.gouv.agora.infrastructure.consultation.PublicConsultationDetailsJson.FeedbackQuestion
import fr.gouv.agora.infrastructure.consultation.PublicConsultationDetailsJson.Footer
import fr.gouv.agora.infrastructure.consultation.PublicConsultationDetailsJson.Goal
import fr.gouv.agora.infrastructure.consultation.PublicConsultationDetailsJson.History
import fr.gouv.agora.infrastructure.consultation.PublicConsultationDetailsJson.InfoHeader
import fr.gouv.agora.infrastructure.consultation.PublicConsultationDetailsJson.ParticipationInfo
import fr.gouv.agora.infrastructure.consultation.PublicConsultationDetailsJson.QuestionsInfo
import fr.gouv.agora.infrastructure.consultation.PublicConsultationDetailsJson.ResponsesInfo
import fr.gouv.agora.infrastructure.consultation.PublicConsultationDetailsJson.Section
import fr.gouv.agora.infrastructure.thematique.ThematiqueNoIdJson
import org.springframework.stereotype.Component

@Component
class PublicConsultationDetailsJsonMapper(private val dateMapper: DateMapper) {

    fun toJson(consultationDetails: ConsultationDetailsV2WithParticipantCount): PublicConsultationDetailsJson {
        return PublicConsultationDetailsJson(
            updateId = consultationDetails.update.id,
            title = consultationDetails.consultation.title,
            coverUrl = consultationDetails.consultation.detailsCoverUrl,
            thematique = ThematiqueNoIdJson(
                label = consultationDetails.thematique.label,
                picto = consultationDetails.thematique.picto,
            ),
            questionsInfo = buildQuestionsInfo(consultationDetails),
            consultationDates = null,
            responsesInfo = buildResponsesInfo(consultationDetails),
            infoHeader = buildInfoHeader(consultationDetails),
            body = buildBody(consultationDetails),
            participationInfo = buildParticipationInfo(consultationDetails),
            downloadAnalysisUrl = consultationDetails.update.downloadAnalysisUrl,
            feedbackQuestion = buildFeedbackQuestion(consultationDetails),
            footer = buildFooter(consultationDetails),
            goals = buildGoals(consultationDetails),
            history = buildHistory(consultationDetails),
        )
    }

    fun toUpdateJson(consultationDetails: ConsultationDetailsV2WithParticipantCount): PublicConsultationDetailsJson {
        return PublicConsultationDetailsJson(
            updateId = consultationDetails.update.id,
            title = consultationDetails.consultation.title,
            coverUrl = consultationDetails.consultation.detailsCoverUrl,
            thematique = ThematiqueNoIdJson(
                label = consultationDetails.thematique.label,
                picto = consultationDetails.thematique.picto,
            ),
            questionsInfo = null,
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
        )
    }

    private fun buildQuestionsInfo(consultationDetails: ConsultationDetailsV2WithParticipantCount): QuestionsInfo? {
        if (!consultationDetails.update.hasQuestionsInfo) return null
        return QuestionsInfo(
            endDate = dateMapper.toFormattedDate(consultationDetails.consultation.endDate),
            questionCount = consultationDetails.consultation.questionCount,
            estimatedTime = consultationDetails.consultation.estimatedTime,
            participantCount = consultationDetails.participantCount,
            participantCountGoal = consultationDetails.consultation.participantCountGoal,
        )
    }

    private fun buildConsultationDates(consultationDetails: ConsultationDetailsV2WithParticipantCount): ConsultationDates? {
        if (!consultationDetails.update.hasQuestionsInfo) return null
        return ConsultationDates(
            startDate = dateMapper.toFormattedDate(consultationDetails.consultation.startDate),
            endDate = dateMapper.toFormattedDate(consultationDetails.consultation.endDate),
        )
    }

    private fun buildResponsesInfo(consultationDetails: ConsultationDetailsV2WithParticipantCount): ResponsesInfo? {
        return consultationDetails.update.responsesInfo?.let { responsesInfo ->
            ResponsesInfo(
                picto = responsesInfo.picto,
                description = responsesInfo.description,
            )
        }
    }

    private fun buildInfoHeader(consultationDetails: ConsultationDetailsV2WithParticipantCount): InfoHeader? {
        return consultationDetails.update.infoHeader?.let { infoHeader ->
            InfoHeader(
                picto = infoHeader.picto,
                description = infoHeader.description,
            )
        }
    }

    private fun buildParticipationInfo(consultationDetails: ConsultationDetailsV2WithParticipantCount): ParticipationInfo? {
        if (!consultationDetails.update.hasParticipationInfo) return null
        return ParticipationInfo(
            participantCount = consultationDetails.participantCount,
            participantCountGoal = consultationDetails.consultation.participantCountGoal,
        )
    }

    private fun buildFeedbackQuestion(consultationDetails: ConsultationDetailsV2WithParticipantCount): FeedbackQuestion? {
        return consultationDetails.update.feedbackQuestion?.let { feedbackQuestion ->
            FeedbackQuestion(
                title = feedbackQuestion.title,
                picto = feedbackQuestion.picto,
                description = feedbackQuestion.description,
            )
        }
    }

    private fun buildFooter(consultationDetails: ConsultationDetailsV2WithParticipantCount): Footer? {
        return consultationDetails.update.footer?.let { footer ->
            Footer(
                title = footer.title,
                description = footer.description,
            )
        }
    }

    private fun buildGoals(consultationDetails: ConsultationDetailsV2WithParticipantCount): List<Goal>? {
        return consultationDetails.update.goals?.map { goal ->
            Goal(
                picto = goal.picto,
                description = goal.description,
            )
        }
    }

    private fun buildHistory(consultationDetails: ConsultationDetailsV2WithParticipantCount): List<History>? {
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
                    ConsultationUpdateHistoryStatus.INCOMING -> "incoming'"
                },
                title = historyItem.title,
                date = historyItem.updateDate?.let(dateMapper::toFormattedDate),
                actionText = historyItem.actionText,
            )
        }
    }

    private fun buildBody(consultationDetails: ConsultationDetailsV2WithParticipantCount): Body {
        val allSections = consultationDetails.update.sectionsHeader.map(::buildSection) +
                consultationDetails.update.body.map(::buildSection)
        return Body(sections = allSections)
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