package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.domain.ConsultationUpdateInfoV2
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.FeedbackQuestion
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.Footer
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.Goal
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.InfoHeader
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.ResponsesInfo
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.Section
import fr.gouv.agora.infrastructure.common.StrapiAttributes
import fr.gouv.agora.infrastructure.common.StrapiData
import fr.gouv.agora.infrastructure.common.StrapiDataList
import fr.gouv.agora.infrastructure.common.toHtml
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationStrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.StrapiConsultationContenuApresReponse
import fr.gouv.agora.infrastructure.consultation.dto.StrapiConsultationContenuAutre
import fr.gouv.agora.infrastructure.consultation.dto.StrapiConsultationContenuAvantReponse
import fr.gouv.agora.infrastructure.consultation.dto.StrapiConsultationSection
import fr.gouv.agora.infrastructure.consultation.dto.StrapiConsultationSectionChiffre
import fr.gouv.agora.infrastructure.consultation.dto.StrapiConsultationSectionCitation
import fr.gouv.agora.infrastructure.consultation.dto.StrapiConsultationSectionImage
import fr.gouv.agora.infrastructure.consultation.dto.StrapiConsultationSectionRichText
import fr.gouv.agora.infrastructure.consultation.dto.StrapiConsultationSectionTitre
import fr.gouv.agora.infrastructure.consultation.dto.StrapiConsultationSectionVideo
import fr.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateSectionDTO
import fr.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateV2DTO
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDate
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import org.springframework.stereotype.Component

@Component
class ConsultationUpdateInfoV2Mapper {

    companion object {
        private const val TRUE_INT_VALUE = 1

        private const val VISIBILITY_TYPE_PREVIEW = 1
        private const val VISIBILITY_TYPE_SHOW_ON_EXPAND = 0
        private const val VISIBILITY_TYPE_SHOW_AS_HEADER = 2

        private const val SECTION_TYPE_TITLE = "title"
        private const val SECTION_TYPE_RICH_TEXT = "richText"
        private const val SECTION_TYPE_IMAGE = "image"
        private const val SECTION_TYPE_VIDEO = "video"
        private const val SECTION_TYPE_FOCUS_NUMBER = "focusNumber"
        private const val SECTION_TYPE_QUOTE = "quote"
        private const val SECTION_TYPE_ACCORDION = "accordion"

        private const val GOALS_SEPARATOR = "|"
    }

    fun toDomain(
        dto: ConsultationUpdateV2DTO,
        sectionDTOs: List<ConsultationUpdateSectionDTO>,
    ): ConsultationUpdateInfoV2 {
        return ConsultationUpdateInfoV2(
            id = dto.id.toString(),
            updateDate = dto.updateDate.toLocalDateTime(),
            shareTextTemplate = dto.shareTextTemplate,
            hasQuestionsInfo = dto.hasQuestionsInfo == TRUE_INT_VALUE,
            hasParticipationInfo = dto.hasParticipationInfo == TRUE_INT_VALUE,
            responsesInfo = buildResponsesInfo(dto),
            infoHeader = buildInfoHeader(dto),
            sectionsHeader = buildSections(
                sectionDTOs = sectionDTOs,
                filter = { sectionDTO -> sectionDTO.visibilityType == VISIBILITY_TYPE_SHOW_AS_HEADER && sectionDTO.parentSectionId == null },
            ),
            body = buildSections(
                sectionDTOs = sectionDTOs,
                filter = { sectionDTO -> sectionDTO.visibilityType == VISIBILITY_TYPE_SHOW_ON_EXPAND && sectionDTO.parentSectionId == null },
            ),
            bodyPreview = buildSections(
                sectionDTOs = sectionDTOs,
                filter = { sectionDTO -> sectionDTO.visibilityType == VISIBILITY_TYPE_PREVIEW && sectionDTO.parentSectionId == null },
            ),
            downloadAnalysisUrl = dto.downloadAnalysisUrl,
            feedbackQuestion = buildFeedbackQuestion(dto),
            footer = buildFooter(dto),
            goals = buildGoals(dto),
        )
    }

    private fun buildResponsesInfo(dto: ConsultationUpdateV2DTO): ResponsesInfo? {
        if (dto.responsesInfoPicto == null || dto.responsesInfoDescription == null || dto.responsesInfoActionText == null) return null
        return ResponsesInfo(
            picto = dto.responsesInfoPicto,
            description = dto.responsesInfoDescription,
            actionText = dto.responsesInfoActionText,
        )
    }

    private fun buildInfoHeader(dto: ConsultationUpdateV2DTO): InfoHeader? {
        if (dto.infoHeaderPicto == null || dto.infoHeaderDescription == null) return null
        return InfoHeader(
            picto = dto.infoHeaderPicto,
            description = dto.infoHeaderDescription,
        )
    }

    private fun buildFeedbackQuestion(dto: ConsultationUpdateV2DTO): FeedbackQuestion? {
        if (dto.feedbackQuestionTitle == null || dto.feedbackQuestionPicto == null || dto.feedbackQuestionDescription == null) return null
        return FeedbackQuestion(
            consultationUpdateId = dto.id.toString(),
            title = dto.feedbackQuestionTitle,
            picto = dto.feedbackQuestionPicto,
            description = dto.feedbackQuestionDescription,
        )
    }

    private fun buildFooter(dto: ConsultationUpdateV2DTO): Footer? {
        return dto.footerDescription?.let {
            Footer(
                title = dto.footerTitle,
                description = dto.footerDescription,
            )
        }
    }

    private fun buildGoals(dto: ConsultationUpdateV2DTO): List<Goal>? {
        return dto.goals?.let { goals ->
            goals.split(GOALS_SEPARATOR).map { goal ->
                val picto = String(Character.toChars(goal.codePointAt(0)))
                Goal(
                    picto = picto,
                    description = goal.replace(picto, ""),
                )
            }
        }?.takeIf { it.isNotEmpty() }
    }

    private fun buildSections(
        sectionDTOs: List<ConsultationUpdateSectionDTO>,
        filter: (ConsultationUpdateSectionDTO) -> Boolean = { _ -> true },
    ): List<Section> {
        return sectionDTOs.filter(filter).mapNotNull { sectionDTO ->
            when (sectionDTO.type) {
                SECTION_TYPE_TITLE -> buildTitleSection(sectionDTO)
                SECTION_TYPE_RICH_TEXT -> buildRichTextSection(sectionDTO)
                SECTION_TYPE_IMAGE -> buildImageSection(sectionDTO)
                SECTION_TYPE_VIDEO -> buildVideoSection(sectionDTO)
                SECTION_TYPE_FOCUS_NUMBER -> buildFocusNumberSection(sectionDTO)
                SECTION_TYPE_QUOTE -> buildQuoteSection(sectionDTO)
                SECTION_TYPE_ACCORDION -> buildAccordionSection(sectionDTO, sectionDTOs)
                else -> null
            }
        }
    }

    private fun buildTitleSection(sectionDTO: ConsultationUpdateSectionDTO): Section.Title? {
        return sectionDTO.title?.let(Section::Title)
    }

    private fun buildRichTextSection(sectionDTO: ConsultationUpdateSectionDTO): Section.RichText? {
        return sectionDTO.description?.let(Section::RichText)
    }

    private fun buildImageSection(sectionDTO: ConsultationUpdateSectionDTO): Section.Image? {
        if (sectionDTO.url == null) return null
        return Section.Image(
            url = sectionDTO.url,
            contentDescription = sectionDTO.title,
        )
    }

    private fun buildVideoSection(sectionDTO: ConsultationUpdateSectionDTO): Section.Video? {
        if (
            sectionDTO.url == null
            || sectionDTO.videoWidth == null
            || sectionDTO.videoHeight == null
            || sectionDTO.videoTranscription == null
        ) return null
        return Section.Video(
            url = sectionDTO.url,
            width = sectionDTO.videoWidth,
            height = sectionDTO.videoHeight,
            authorInfo = buildAuthorInfo(sectionDTO),
            transcription = sectionDTO.videoTranscription,
        )
    }

    private fun buildAuthorInfo(sectionDTO: ConsultationUpdateSectionDTO): Section.Video.AuthorInfo? {
        if (sectionDTO.authorInfoName == null || sectionDTO.authorInfoMessage == null || sectionDTO.videoDate == null) return null
        return Section.Video.AuthorInfo(
            name = sectionDTO.authorInfoName,
            message = sectionDTO.authorInfoMessage,
            date = sectionDTO.videoDate.toLocalDate(),
        )
    }

    private fun buildFocusNumberSection(sectionDTO: ConsultationUpdateSectionDTO): Section.FocusNumber? {
        if (sectionDTO.title == null || sectionDTO.description == null) return null
        return Section.FocusNumber(
            title = sectionDTO.title,
            description = sectionDTO.description,
        )
    }

    private fun buildQuoteSection(sectionDTO: ConsultationUpdateSectionDTO): Section.Quote? {
        return sectionDTO.description?.let(Section::Quote)
    }

    private fun buildAccordionSection(
        accordionDTO: ConsultationUpdateSectionDTO,
        sectionDTOs: List<ConsultationUpdateSectionDTO>,
    ): Section.Accordion? {
        return accordionDTO.title?.let { title ->
            Section.Accordion(
                title = title,
                sections = buildSections(
                    sectionDTOs = sectionDTOs,
                    filter = { sectionDTO -> sectionDTO.parentSectionId == accordionDTO.id && sectionDTO.type != SECTION_TYPE_ACCORDION },
                )
            )
        }
    }

    fun toDomainUnanswered(consultationDTO: StrapiAttributes<ConsultationStrapiDTO>): ConsultationUpdateInfoV2 {
        val consultation = consultationDTO.attributes
        val contentBeforeResponse = consultation.contenuAvantReponse.data.attributes
        val contentAfterResponse = consultation.contenuApresReponseOuTerminee.data.attributes

        val sections = toSections(contentBeforeResponse.sections)

        return ConsultationUpdateInfoV2(
            id = "avant~${consultation.contenuAvantReponse.data.id}",
            updateDate = contentBeforeResponse.datetimePublication,
            shareTextTemplate = contentBeforeResponse.templatePartage,
            hasQuestionsInfo = true,
            hasParticipationInfo = false,
            responsesInfo = ResponsesInfo(
                picto = contentAfterResponse.encartVisualisationResultatAvantFinConsultationPictogramme,
                description = "<body>${contentAfterResponse.encartVisualisationResultatAvantFinConsultationDescription.toHtml()}</body>",
                actionText = contentAfterResponse.encartVisualisationResultatAvantFinConsultationCallToAction
            ),
            sectionsHeader = emptyList(),
            body = sections,
            bodyPreview = emptyList(),
            infoHeader = null,
            downloadAnalysisUrl = null,
            feedbackQuestion = null,
            footer = null,
            goals = null,
        )
    }

    fun toDomainContenuAutre(consultationDTO: StrapiAttributes<ConsultationStrapiDTO>, contentDTO: StrapiAttributes<StrapiConsultationContenuAutre>): ConsultationUpdateInfoV2 {
        val contenu = contentDTO.attributes

        val htmlSections = toSections(contenu.sections)

        return ConsultationUpdateInfoV2(
            id = "autre~${contentDTO.id}",
            updateDate = contenu.datetimePublication,
            shareTextTemplate = contenu.templatePartage,
            hasQuestionsInfo = false,
            hasParticipationInfo = false,
            responsesInfo = null,
            sectionsHeader = emptyList(),
            body = htmlSections,
            bodyPreview = emptyList(),
            infoHeader = null,
            downloadAnalysisUrl = contenu.lienTelechargementAnalyse,
            feedbackQuestion = FeedbackQuestion(
                contentDTO.id,
                contenu.feedbackTitre,
                contenu.feedbackPictogramme,
                "<body>${contenu.feedbackDescription.toHtml()}</body>"
            ),
            footer = Footer(
                contenu.footerTitre,
                "<body>${contenu.footerDescription.toHtml()}</body>"
            ),
            goals = null,
        )
    }

    fun toDomainAnswered(
        consultation: StrapiAttributes<ConsultationStrapiDTO>,
    ): ConsultationUpdateInfoV2? {
        val contenu = consultation.attributes.contenuApresReponseOuTerminee.data.attributes
        val contenuId = consultation.attributes.contenuApresReponseOuTerminee.data.id

        val htmlSections = toSections(contenu.sections)

        return ConsultationUpdateInfoV2(
            id = "apres~$contenuId",
            updateDate = contenu.datetimePublication,
            shareTextTemplate = contenu.templatePartageApresFinConsultation,
            hasQuestionsInfo = false,
            hasParticipationInfo = false,
            responsesInfo = null,
            sectionsHeader = emptyList(),
            body = htmlSections,
            bodyPreview = emptyList(),
            infoHeader = null,
            downloadAnalysisUrl = null,
            feedbackQuestion = FeedbackQuestion(
                "apres~$contenuId",
                contenu.feedbackTitre,
                contenu.feedbackPictogramme,
                "<body>${contenu.feedbackDescription.toHtml()}</body>"
            ),
            footer = Footer(
                contenu.footerTitre,
                "<body>${contenu.footerDescription.toHtml()}</body>"
            ),
            goals = null,
        )
    }

    private fun toSections(sections: List<StrapiConsultationSection>): List<Section> {
        val sectionHeader = sections.map {
            when (it) {
                is StrapiConsultationSectionTitre -> Section.Title(it.titre)

                is StrapiConsultationSectionRichText -> {
                    Section.RichText("<body>" + it.description.toHtml() + "</body>")
                }

                is StrapiConsultationSectionCitation -> {
                    Section.Quote("<body>" + it.description.toHtml() + "</body>")
                }

                is StrapiConsultationSectionImage -> {
                    Section.Image(it.url, it.descriptionImage)
                }

                is StrapiConsultationSectionVideo -> {
                    Section.Video(
                        it.url,
                        it.largeur,
                        it.hauteur,
                        Section.Video.AuthorInfo(it.nomAuteur, it.posteAuteur, it.dateTournage),
                        it.transcription
                    )
                }

                is StrapiConsultationSectionChiffre -> {
                    Section.FocusNumber(it.titre, "<body>" + it.description.toHtml() + "</body>")
                }
            }
        }
        return sectionHeader
    }
}
