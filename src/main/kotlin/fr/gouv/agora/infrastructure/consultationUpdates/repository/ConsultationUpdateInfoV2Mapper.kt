package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.domain.ConsultationUpdateInfoV2
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.FeedbackQuestion
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
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ConsultationUpdateInfoV2Mapper {
    fun toDomainUnanswered(consultationDTO: StrapiAttributes<ConsultationStrapiDTO>): ConsultationUpdateInfoV2 {
        val consultation = consultationDTO.attributes
        val contentBeforeResponse = consultation.contenuAvantReponse.data.attributes

        val sections = toSections(contentBeforeResponse.sections)

        val sectionPourquoi = Section.Title("Pourquoi cette initiative ?")

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
                Goal("🗣️", contentBeforeResponse.commanditaire.toHtml()),
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
                "<body><b>Cette consultation est maintenant terminée.</b> Les résultats sont en cours d’analyse. Vous serez prévenu(e) dès que la synthèse sera disponible.</body>",
                "Voir tous les résultats"
            )
        } else {
            ResponsesInfo(
                "🙌",
                "<body><b>Merci pour votre participation</b> !</body>",
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
            downloadAnalysisUrl = contenu.getAnalysePdfUrl(),
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
