package fr.gouv.agora.infrastructure.question.repository

import fr.gouv.agora.domain.ChoixPossibleConditional
import fr.gouv.agora.domain.ChoixPossibleDefault
import fr.gouv.agora.domain.QuestionChapter
import fr.gouv.agora.domain.QuestionConditional
import fr.gouv.agora.domain.QuestionMultipleChoices
import fr.gouv.agora.domain.QuestionOpen
import fr.gouv.agora.domain.QuestionUniqueChoice
import fr.gouv.agora.infrastructure.common.StrapiAttributes
import fr.gouv.agora.infrastructure.common.toHtml
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationStrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.StrapiConsultationQuestionChoixMultiples
import fr.gouv.agora.infrastructure.consultation.dto.StrapiConsultationQuestionChoixUnique
import fr.gouv.agora.infrastructure.consultation.dto.StrapiConsultationQuestionConditionnelle
import fr.gouv.agora.infrastructure.consultation.dto.StrapiConsultationQuestionDescription
import fr.gouv.agora.infrastructure.consultation.dto.StrapiConsultationQuestionOuverte
import org.springframework.stereotype.Component

@Component
class QuestionMapper {
    fun toQuestionChoixMultiple(
        questionChoixMultipleStrapi: StrapiConsultationQuestionChoixMultiples,
        consultationDTO: StrapiAttributes<ConsultationStrapiDTO>
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

        val questionSuivanteId = if (questionChoixMultipleStrapi.numeroQuestionSuivante != null) {
            consultationDTO.attributes.getQuestionId(questionChoixMultipleStrapi.numeroQuestionSuivante)
        } else {
            consultationDTO.attributes.getQuestionId(questionChoixMultipleStrapi.numero + 1)
        }

        return QuestionMultipleChoices(
            questionChoixMultipleStrapi.id,
            questionChoixMultipleStrapi.titre,
            questionChoixMultipleStrapi.popupExplication?.toHtml(),
            questionChoixMultipleStrapi.numero,
            questionSuivanteId,
            consultationDTO.id,
            choix,
            questionChoixMultipleStrapi.nombreMaximumDeChoix
        )
    }

    fun toQuestionChoixUnique(
        questionChoixUniqueStrapi: StrapiConsultationQuestionChoixUnique,
        consultationDTO: StrapiAttributes<ConsultationStrapiDTO>
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

        val questionSuivanteId = if (questionChoixUniqueStrapi.numeroQuestionSuivante != null) {
            consultationDTO.attributes.getQuestionId(questionChoixUniqueStrapi.numeroQuestionSuivante)
        } else {
            consultationDTO.attributes.getQuestionId(questionChoixUniqueStrapi.numero + 1)
        }

        return QuestionUniqueChoice(
            questionChoixUniqueStrapi.id,
            questionChoixUniqueStrapi.titre,
            questionChoixUniqueStrapi.popupExplication?.toHtml(),
            questionChoixUniqueStrapi.numero,
            questionSuivanteId,
            consultationDTO.id,
            choix,
        )
    }

    fun toQuestionOuverte(
        questionOuverteStrapi: StrapiConsultationQuestionOuverte,
        consultationDTO: StrapiAttributes<ConsultationStrapiDTO>
    ): QuestionOpen {
        val questionSuivanteId = if (questionOuverteStrapi.numeroQuestionSuivante != null) {
            consultationDTO.attributes.getQuestionId(questionOuverteStrapi.numeroQuestionSuivante)
        } else {
            consultationDTO.attributes.getQuestionId(questionOuverteStrapi.numero + 1)
        }
        return QuestionOpen(
            questionOuverteStrapi.id,
            questionOuverteStrapi.titre,
            questionOuverteStrapi.popupExplication?.toHtml(),
            questionOuverteStrapi.numero,
            questionSuivanteId,
            consultationDTO.id,
        )
    }

    fun toQuestionDescription(
        questionDescriptionStrapi: StrapiConsultationQuestionDescription,
        consultationDTO: StrapiAttributes<ConsultationStrapiDTO>
    ): QuestionChapter {
        val questionSuivanteId = if (questionDescriptionStrapi.numeroQuestionSuivante != null) {
            consultationDTO.attributes.getQuestionId(questionDescriptionStrapi.numeroQuestionSuivante)
        } else {
            consultationDTO.attributes.getQuestionId(questionDescriptionStrapi.numero + 1)
        } // TODO refacto questionSuivanteId ?
        return QuestionChapter(
            questionDescriptionStrapi.id,
            questionDescriptionStrapi.titre,
            null,
            questionDescriptionStrapi.numero,
            questionSuivanteId,
            consultationDTO.id,
            questionDescriptionStrapi.description.toHtml()
        )
    }

    fun toQuestionConditionnelle(
        questionConditionnelleStrapi: StrapiConsultationQuestionConditionnelle,
        consultationDTO: StrapiAttributes<ConsultationStrapiDTO>
    ): QuestionConditional {
        val choices = questionConditionnelleStrapi.choix.mapIndexed { index, choice ->
            ChoixPossibleConditional(
                choice.id,
                choice.label,
                index,
                questionConditionnelleStrapi.id,
                choice.ouvert,
                consultationDTO.attributes.questions.first { it.numero == choice.numeroDeLaQuestionSuivante }.id
            )
        }

        return QuestionConditional(
            questionConditionnelleStrapi.id,
            questionConditionnelleStrapi.titre,
            questionConditionnelleStrapi.popupExplication?.toHtml(),
            questionConditionnelleStrapi.numero,
            null,
            consultationDTO.id,
            choices
        )
    }
}
