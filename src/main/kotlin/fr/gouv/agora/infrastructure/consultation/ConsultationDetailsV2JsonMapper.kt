package fr.gouv.agora.infrastructure.consultation

import fr.gouv.agora.domain.ConsultationDetailsV2WithInfo
import fr.gouv.agora.domain.ConsultationUpdateInfoV2
import fr.gouv.agora.infrastructure.consultation.ConsultationDetailsV2Json.*
import fr.gouv.agora.infrastructure.profile.repository.DateMapper
import fr.gouv.agora.infrastructure.thematique.ThematiqueNoIdJson
import org.springframework.stereotype.Component

@Component
class ConsultationDetailsV2JsonMapper(
    private val dateMapper: DateMapper,
) {

    companion object {
        private const val SHARE_TEXT_REPLACE_TITLE_PATTERN = "{title}"
        private const val SHARE_TEXT_REPLACE_ID_PATTERN = "{id}"
    }

    fun toJson(consultationDetails: ConsultationDetailsV2WithInfo): ConsultationDetailsV2Json {
        return ConsultationDetailsV2Json(
            title = consultationDetails.consultation.title,
            coverUrl = consultationDetails.consultation.coverUrl,
            shareText = buildShareText(consultationDetails),
            thematique = ThematiqueNoIdJson(
                label = consultationDetails.thematique.label,
                picto = consultationDetails.thematique.picto,
            ),
            questionsInfo = buildQuestionsInfo(consultationDetails),
            consultationDates = null, // TODO
            responsesInfo = buildResponsesInfo(consultationDetails),
            infoHeader = buildInfoHeader(consultationDetails),
            body = buildBody(consultationDetails),
            participationInfo = buildParticipationInfo(consultationDetails),
            downloadAnalysisUrl = consultationDetails.update.downloadAnalysisUrl,
            footer = buildFooter(consultationDetails),
        )
    }

    private fun buildShareText(consultationDetails: ConsultationDetailsV2WithInfo): String {
        return consultationDetails.update.shareTextTemplate
            .replace(SHARE_TEXT_REPLACE_ID_PATTERN, consultationDetails.consultation.id)
            .replace(SHARE_TEXT_REPLACE_TITLE_PATTERN, consultationDetails.consultation.title)
    }

    private fun buildQuestionsInfo(consultationDetails: ConsultationDetailsV2WithInfo): QuestionsInfo? {
        if (!consultationDetails.update.hasQuestionsInfo) return null
        return QuestionsInfo(
            endDate = dateMapper.toFormattedDate(consultationDetails.consultation.endDate),
            questionCount = consultationDetails.consultation.questionCount,
            estimatedTime = consultationDetails.consultation.estimatedTime,
            participantCount = consultationDetails.participantCount,
            participantCountGoal = consultationDetails.consultation.participantCountGoal,
        )
    }

    private fun buildResponsesInfo(consultationDetails: ConsultationDetailsV2WithInfo): ResponsesInfo? {
        return consultationDetails.update.responsesInfo?.let { responsesInfo ->
            ResponsesInfo(
                picto = responsesInfo.picto,
                description = responsesInfo.description,
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

    private fun buildFooter(consultationDetails: ConsultationDetailsV2WithInfo): Footer? {
        return consultationDetails.update.footer?.let { footer ->
            Footer(
                title = footer.title,
                description = footer.description,
            )
        }
    }

    private fun buildBody(consultationDetails: ConsultationDetailsV2WithInfo): Body {
        return Body(
            sectionsPreview = consultationDetails.update.bodyPreview.map { section -> buildSection(section) },
            sections = consultationDetails.update.body.map { section -> buildSection(section) },
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
                    )
                },
                transcription = section.transcription,
            )

            is ConsultationUpdateInfoV2.Section.FocusNumber -> Section.FocusNumber(
                title = section.title,
                description = section.description,
            )

            is ConsultationUpdateInfoV2.Section.Quote -> Section.Quote(description = section.description)
        }
    }

}