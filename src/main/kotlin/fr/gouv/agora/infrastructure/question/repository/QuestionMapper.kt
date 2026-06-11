package fr.gouv.agora.infrastructure.question.repository

import fr.gouv.agora.domain.ChoixPossibleConditional
import fr.gouv.agora.domain.ChoixPossibleDefault
import fr.gouv.agora.domain.QuestionChapter
import fr.gouv.agora.domain.QuestionConditional
import fr.gouv.agora.domain.QuestionMultipleChoices
import fr.gouv.agora.domain.QuestionOpen
import fr.gouv.agora.domain.QuestionUniqueChoice
import fr.gouv.agora.infrastructure.common.toHtml
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationQuestionChoixMultiples
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationQuestionChoixUnique
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationQuestionConditionnelle
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationQuestionDescription
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationQuestionOuverte
import org.springframework.stereotype.Component

@Component
class QuestionMapper {
    fun toQuestionChoixMultiple(
        questionChoixMultipleStrapi: StrapiConsultationQuestionChoixMultiples,
        consultationDTO: ConsultationStrapiDTO
    ): QuestionMultipleChoices {
        val choix = questionChoixMultipleStrapi.choix.mapIndexed { index, choice ->
            ChoixPossibleDefault(
                choice.id,
                choice.label,
                index,
                questionChoixMultipleStrapi.id,
                choice.ouvert
            )
        }

        return QuestionMultipleChoices(
            questionChoixMultipleStrapi.id,
            questionChoixMultipleStrapi.titre,
            questionChoixMultipleStrapi.popupExplication?.toHtml(),
            questionChoixMultipleStrapi.numero,
            consultationDTO.getNextQuestionId(questionChoixMultipleStrapi),
            consultationDTO.documentId,
            choix,
            questionChoixMultipleStrapi.nombreMaximumDeChoix
        )
    }

    fun toQuestionChoixUnique(
        questionChoixUniqueStrapi: StrapiConsultationQuestionChoixUnique,
        consultationDTO: ConsultationStrapiDTO
    ): QuestionUniqueChoice {
        val choix = questionChoixUniqueStrapi.choix.mapIndexed { index, choice ->
            ChoixPossibleDefault(
                choice.id,
                choice.label,
                index,
                questionChoixUniqueStrapi.id,
                choice.ouvert
            )
        }

        return QuestionUniqueChoice(
            questionChoixUniqueStrapi.id,
            questionChoixUniqueStrapi.titre,
            questionChoixUniqueStrapi.popupExplication?.toHtml(),
            questionChoixUniqueStrapi.numero,
            consultationDTO.getNextQuestionId(questionChoixUniqueStrapi),
            consultationDTO.documentId,
            choix,
        )
    }

    fun toQuestionOuverte(
        questionOuverteStrapi: StrapiConsultationQuestionOuverte,
        consultationDTO: ConsultationStrapiDTO
    ): QuestionOpen {
        return QuestionOpen(
            questionOuverteStrapi.id,
            questionOuverteStrapi.titre,
            questionOuverteStrapi.popupExplication?.toHtml(),
            questionOuverteStrapi.numero,
            consultationDTO.getNextQuestionId(questionOuverteStrapi),
            consultationDTO.documentId,
        )
    }

    fun toQuestionDescription(
        questionDescriptionStrapi: StrapiConsultationQuestionDescription,
        consultationDTO: ConsultationStrapiDTO
    ): QuestionChapter {
        return QuestionChapter(
            questionDescriptionStrapi.id,
            questionDescriptionStrapi.titre,
            null,
            questionDescriptionStrapi.numero,
            consultationDTO.getNextQuestionId(questionDescriptionStrapi),
            consultationDTO.documentId,
            questionDescriptionStrapi.getImageUrl(),
            questionDescriptionStrapi.description.toHtml(),
            questionDescriptionStrapi.transcriptionImage,
        )
    }

    fun toQuestionConditionnelle(
        questionConditionnelleStrapi: StrapiConsultationQuestionConditionnelle,
        consultationDTO: ConsultationStrapiDTO
    ): QuestionConditional {
        val choices = questionConditionnelleStrapi.choix.mapIndexed { index, choice ->
            ChoixPossibleConditional(
                choice.id,
                choice.label,
                index,
                questionConditionnelleStrapi.id,
                choice.ouvert,
                consultationDTO.questions.first { q -> q.numero == choice.numeroDeLaQuestionSuivante }.id
            )
        }

        return QuestionConditional(
            questionConditionnelleStrapi.id,
            questionConditionnelleStrapi.titre,
            questionConditionnelleStrapi.popupExplication?.toHtml(),
            questionConditionnelleStrapi.numero,
            null,
            consultationDTO.documentId,
            choices
        )
    }
}
