package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.domain.ConsultationUpdateInfoV2
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.FeedbackQuestion
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.Footer
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.Goal
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.InfoHeader
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.ResponsesInfo
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.Section
import fr.gouv.agora.infrastructure.common.StrapiAttributes
import fr.gouv.agora.infrastructure.common.toHtml
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationContenuAutre
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationSection
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationSectionAccordeon
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationSectionChiffre
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationSectionCitation
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationSectionImage
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationSectionRichText
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationSectionTitre
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationSectionVideo
import fr.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateSectionDTO
import fr.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateV2DTO
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDate
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import org.springframework.stereotype.Component
import java.time.LocalDateTime

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
            slug = dto.slug,
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
            isPublished = true,
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
            dto.id.toString(),
            "Donnez votre avis",
            "💬",
            dto.feedbackQuestionDescription,
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

        val sections = toSections(contentBeforeResponse.sections)

        val sectionPourquoi = Section.Title("Pourquoi cette consultation ?")

        return ConsultationUpdateInfoV2(
            id = consultation.contenuAvantReponse.data.id,
            slug = contentBeforeResponse.slug,
            updateDate = consultation.dateDeDebut,
            shareTextTemplate = contentBeforeResponse.templatePartage,
            hasQuestionsInfo = true,
            hasParticipationInfo = false,
            responsesInfo = null,
            sectionsHeader = emptyList(),
            body = listOf(
                sectionPourquoi,
                Section.RichText(contentBeforeResponse.presentation.toHtml()),
                *sections.toTypedArray()
            ),
            bodyPreview = listOf(
                sectionPourquoi,
                Section.RichText(
                    contentBeforeResponse.presentation.toHtml().split("<br/>").take(2).joinToString("<br/>")
                )
            ),
            infoHeader = null,
            downloadAnalysisUrl = null,
            feedbackQuestion = null,
            footer = null,
            goals = listOf(
                Goal("🗣️", "Consultation proposée par " + contentBeforeResponse.commanditaire.toHtml()),
                Goal("🎯", "<b>Objectif : </b>" + contentBeforeResponse.objectif.toHtml()),
                Goal("🚀", "<b>Axe gouvernemental : </b>" + contentBeforeResponse.axeGouvernemental.toHtml()),
            ),
            isPublished = consultation.isPublished(),
        )
    }

    fun toDomainContenuAutre(
        consultationDTO: StrapiAttributes<ConsultationStrapiDTO>,
        contentDTO: StrapiAttributes<StrapiConsultationContenuAutre>
    ): ConsultationUpdateInfoV2 {
        val contenu = contentDTO.attributes

        val htmlSections = toSections(contenu.sections)
        val previewHtmlSections = toPreviewSections(contenu.sections)

        val infoHeader = if (contenu.recapEmoji != null && contenu.recapLabel != null)
            InfoHeader(contenu.recapEmoji, contenu.recapLabel)
        else null

        return ConsultationUpdateInfoV2(
            id = contentDTO.id,
            slug = contenu.slug,
            updateDate = contenu.datetimePublication,
            shareTextTemplate = contenu.templatePartage,
            hasQuestionsInfo = false,
            hasParticipationInfo = false,
            responsesInfo = null,
            sectionsHeader = emptyList(),
            body = htmlSections,
            bodyPreview = previewHtmlSections,
            infoHeader = infoHeader,
            downloadAnalysisUrl = null,
            feedbackQuestion = FeedbackQuestion(
                contentDTO.id,
                "Donnez votre avis",
                "💬",
                "<body>${contenu.feedbackMessage}</body>"
            ),
            footer = null,
            goals = null,
            isPublished = consultationDTO.attributes.isPublished(),
        )
    }

    fun toDomainAnsweredOrEnded(
        consultation: StrapiAttributes<ConsultationStrapiDTO>,
        now: LocalDateTime,
    ): ConsultationUpdateInfoV2? {
        val contenu = consultation.attributes.contenuApresReponseOuTerminee.data.attributes
        val contenuId = consultation.attributes.contenuApresReponseOuTerminee.data.id
        val htmlSections = toSections(contenu.sections)

        val consultationIsEnded = consultation.attributes.dateDeFin.isBefore(now)
        val responsesInfo = if (consultationIsEnded) {
            ResponsesInfo(
                "🏁",
                "<body><b>Cette consultation est maintenant terminée.</b> Les résultats sont en cours d’analyse. Vous serez notifié(e) dès que la synthèse sera disponible.</body>",
                "Voir tous les résultats"
            )
        } else {
            ResponsesInfo(
                "🙌",
                "<body><b>Merci pour votre participation</b> à cette consultation !</body>",
                "Voir les premiers résultats"
            )
        }

        val previewHtmlSections = toPreviewSections(contenu.sections)

        return ConsultationUpdateInfoV2(
            id = contenuId,
            slug = contenu.slug,
            updateDate = consultation.attributes.dateDeDebut,
            shareTextTemplate = contenu.templatePartage,
            hasQuestionsInfo = false,
            hasParticipationInfo = false,
            responsesInfo = responsesInfo,
            sectionsHeader = emptyList(),
            body = htmlSections,
            bodyPreview = previewHtmlSections,
            infoHeader = null,
            downloadAnalysisUrl = null,
            feedbackQuestion = FeedbackQuestion(
                contenuId,
                "Donnez votre avis",
                "💬",
                "<body>${contenu.feedbackMessage}</body>"
            ),
            footer = null,
            goals = null,
            isPublished = consultation.attributes.isPublished(),
        )
    }

    fun toDomainReponseDuCommanditaire(consultation: StrapiAttributes<ConsultationStrapiDTO>): ConsultationUpdateInfoV2? {
        val contenu = consultation.attributes.consultationContenuReponseDuCommanditaire.data?.attributes
            ?: return null
        val contenuId = consultation.attributes.consultationContenuReponseDuCommanditaire.data.id
        val htmlSections = toSections(contenu.sections)
        val previewHtmlSections = toPreviewSections(contenu.sections)
        val infoHeader = if (contenu.recapEmoji != null && contenu.recapLabel != null)
            InfoHeader(contenu.recapEmoji, contenu.recapLabel)
        else null

        return ConsultationUpdateInfoV2(
            id = contenuId,
            slug = contenu.slug,
            updateDate = contenu.datetimePublication,
            shareTextTemplate = contenu.templatePartage,
            hasQuestionsInfo = false,
            hasParticipationInfo = false,
            responsesInfo = null,
            sectionsHeader = emptyList(),
            body = htmlSections,
            bodyPreview = previewHtmlSections,
            infoHeader = infoHeader,
            downloadAnalysisUrl = null,
            feedbackQuestion = FeedbackQuestion(
                contenuId,
                "Donnez votre avis",
                "💬",
                "<body>${contenu.feedbackMessage}</body>"
            ),
            footer = null,
            goals = null,
            isPublished = consultation.attributes.isPublished(),
        )
    }

    fun toDomainAnalyseDesReponses(consultation: StrapiAttributes<ConsultationStrapiDTO>): ConsultationUpdateInfoV2? {
        val contenu = consultation.attributes.consultationContenuAnalyseDesReponses.data?.attributes
            ?: return null
        val contenuId = consultation.attributes.consultationContenuAnalyseDesReponses.data.id
        val htmlSections = toSections(contenu.sections)
        val previewHtmlSections = toPreviewSections(contenu.sections)
        val infoHeader = if (contenu.recapEmoji != null && contenu.recapLabel != null)
            InfoHeader(contenu.recapEmoji, contenu.recapLabel)
        else null

        return ConsultationUpdateInfoV2(
            id = contenuId,
            slug = contenu.slug,
            updateDate = contenu.datetimePublication,
            shareTextTemplate = contenu.templatePartage,
            hasQuestionsInfo = false,
            hasParticipationInfo = false,
            responsesInfo = null,
            sectionsHeader = emptyList(),
            body = htmlSections,
            bodyPreview = previewHtmlSections,
            infoHeader = infoHeader,
            downloadAnalysisUrl = contenu.lienTelechargementAnalyse,
            feedbackQuestion = FeedbackQuestion(
                contenuId,
                "Donnez votre avis",
                "💬",
                "<body>${contenu.feedbackMessage}</body>"
            ),
            footer = null,
            goals = null,
            isPublished = consultation.attributes.isPublished(),
        )
    }

    private fun toSections(sections: List<StrapiConsultationSection>): List<Section> {
        val sectionHeader = sections.map {
            when (it) {
                is StrapiConsultationSectionTitre -> Section.Title(it.titre)

                is StrapiConsultationSectionRichText -> {
                    Section.RichText(it.description.toHtml())
                }

                is StrapiConsultationSectionCitation -> {
                    Section.Quote(it.description.toHtml())
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
                    Section.FocusNumber(it.titre, it.description.toHtml())
                }

                is StrapiConsultationSectionAccordeon -> {
                    Section.Accordion(it.titre, listOf(Section.RichText(it.description.toHtml())))
                }
            }
        }
        return sectionHeader
    }

    private fun toPreviewSections(sections: List<StrapiConsultationSection>): List<Section> {
        val sectionsHtml = toSections(sections)

        if (sectionsHtml.size < 8) return emptyList()

        return sectionsHtml.take(5)
    }
}
