package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.domain.ConsultationUpdateInfoV2
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.FeedbackQuestion
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.Goal
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.InfoHeader
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.ResponsesInfo
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.Section
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
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ConsultationUpdateInfoV2Mapper {
    fun toDomainUnanswered(consultationDTO: ConsultationStrapiDTO): ConsultationUpdateInfoV2 {
        val contentBeforeResponse = consultationDTO.contenuAvantReponse

        val sections = toSections(contentBeforeResponse.sections)

        val sectionPourquoi = Section.Title("Pourquoi cette consultation ?")

        return ConsultationUpdateInfoV2(
            id = contentBeforeResponse.documentId,
            slug = contentBeforeResponse.slug,
            updateDate = consultationDTO.dateDeDebut,
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
                    contentBeforeResponse.presentation.toHtml().split("</p>").firstOrNull()?.let { "$it</p>" }
                        ?: contentBeforeResponse.presentation.toHtml()
                )
            ),
            infoHeader = null,
            downloadAnalysisUrl = null,
            feedbackQuestion = null,
            footer = null,
            goals = listOf(
                Goal("🗣️", contentBeforeResponse.commanditaire.toHtml().removePrefix("<p>").removeSuffix("</p>")),
                Goal("🎯", contentBeforeResponse.objectif.toHtml().removePrefix("<p>").removeSuffix("</p>")),
                Goal("🚀", contentBeforeResponse.axeGouvernemental.toHtml().removePrefix("<p>").removeSuffix("</p>")),
            ),
        )
    }

    fun toDomainContenuAutre(
        consultationDTO: ConsultationStrapiDTO,
        contentDTO: StrapiConsultationContenuAutre
    ): ConsultationUpdateInfoV2 {
        val htmlSections = toSections(contentDTO.sections)
        val previewHtmlSections = toPreviewSections(contentDTO.sections)

        val infoHeader = if (contentDTO.recapEmoji != null && contentDTO.recapLabel != null)
            InfoHeader(contentDTO.recapEmoji, contentDTO.recapLabel)
        else null

        return ConsultationUpdateInfoV2(
            id = contentDTO.documentId,
            slug = contentDTO.slug,
            updateDate = contentDTO.datetimePublication,
            shareTextTemplate = contentDTO.templatePartage,
            hasQuestionsInfo = false,
            hasParticipationInfo = false,
            responsesInfo = null,
            sectionsHeader = emptyList(),
            body = htmlSections,
            bodyPreview = previewHtmlSections,
            infoHeader = infoHeader,
            downloadAnalysisUrl = null,
            feedbackQuestion = FeedbackQuestion(
                contentDTO.documentId,
                "Donnez votre avis",
                "💬",
                "<body>${contentDTO.feedbackMessage}</body>"
            ),
            footer = null,
            goals = null,
        )
    }

    fun toDomainAnsweredOrEnded(
        consultation: ConsultationStrapiDTO,
        now: LocalDateTime,
    ): ConsultationUpdateInfoV2? {
        val contenu = consultation.contenuApresReponseOuTerminee
        val htmlSections = toSections(contenu.sections)

        val consultationIsEnded = consultation.dateDeFin.isBefore(now)
        val responsesInfo = if (consultationIsEnded) {
            ResponsesInfo(
                "🏁",
                "<body><b>Cette consultation est maintenant terminée.</b> Les résultats sont en cours d'analyse. Vous serez prévenu(e) dès que la synthèse sera disponible.</body>",
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
            id = contenu.documentId,
            slug = contenu.slug,
            updateDate = consultation.dateDeDebut,
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
                contenu.documentId,
                "Donnez votre avis",
                "💬",
                "<body>${contenu.feedbackMessage}</body>"
            ),
            footer = null,
            goals = null,
        )
    }

    fun toDomainReponseDuCommanditaire(consultation: ConsultationStrapiDTO): ConsultationUpdateInfoV2? {
        val contenu = consultation.consultationContenuReponseDuCommanditaire ?: return null
        val htmlSections = toSections(contenu.sections)
        val previewHtmlSections = toPreviewSections(contenu.sections)
        val infoHeader = if (contenu.recapEmoji != null && contenu.recapLabel != null)
            InfoHeader(contenu.recapEmoji, contenu.recapLabel)
        else null

        return ConsultationUpdateInfoV2(
            id = contenu.documentId,
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
                contenu.documentId,
                "Donnez votre avis",
                "💬",
                "<body>${contenu.feedbackMessage}</body>"
            ),
            footer = null,
            goals = null,
        )
    }

    fun toDomainAnalyseDesReponses(consultation: ConsultationStrapiDTO): ConsultationUpdateInfoV2? {
        val contenu = consultation.consultationContenuAnalyseDesReponses ?: return null
        val htmlSections = toSections(contenu.sections)
        val previewHtmlSections = toPreviewSections(contenu.sections)
        val infoHeader = if (contenu.recapEmoji != null && contenu.recapLabel != null)
            InfoHeader(contenu.recapEmoji, contenu.recapLabel)
        else null

        return ConsultationUpdateInfoV2(
            id = contenu.documentId,
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
            downloadAnalysisUrl = contenu.getAnalysePdfUrl(),
            feedbackQuestion = FeedbackQuestion(
                contenu.documentId,
                "Donnez votre avis",
                "💬",
                "<body>${contenu.feedbackMessage}</body>"
            ),
            footer = null,
            goals = null,
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
                    Section.Image(it.getImageUrl(), it.descriptionImage)
                }

                is StrapiConsultationSectionVideo -> {
                    Section.Video(
                        it.getVideoUrl(),
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
