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

        return QuestionMultipleChoices(
            questionChoixMultipleStrapi.id,
            questionChoixMultipleStrapi.titre,
            questionChoixMultipleStrapi.popupExplication?.toHtml(),
            questionChoixMultipleStrapi.numero,
            consultationDTO.attributes.getNextQuestionId(questionChoixMultipleStrapi),
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

        return QuestionUniqueChoice(
            questionChoixUniqueStrapi.id,
            questionChoixUniqueStrapi.titre,
            questionChoixUniqueStrapi.popupExplication?.toHtml(),
            questionChoixUniqueStrapi.numero,
            consultationDTO.attributes.getNextQuestionId(questionChoixUniqueStrapi),
            consultationDTO.id,
            choix,
        )
    }

    fun toQuestionOuverte(
        questionOuverteStrapi: StrapiConsultationQuestionOuverte,
        consultationDTO: StrapiAttributes<ConsultationStrapiDTO>
    ): QuestionOpen {
        return QuestionOpen(
            questionOuverteStrapi.id,
            questionOuverteStrapi.titre,
            questionOuverteStrapi.popupExplication?.toHtml(),
            questionOuverteStrapi.numero,
            consultationDTO.attributes.getNextQuestionId(questionOuverteStrapi),
            consultationDTO.id,
        )
    }

    fun toQuestionDescription(
        questionDescriptionStrapi: StrapiConsultationQuestionDescription,
        consultationDTO: StrapiAttributes<ConsultationStrapiDTO>
    ): QuestionChapter {
        return QuestionChapter(
            questionDescriptionStrapi.id,
            questionDescriptionStrapi.titre,
            null,
            questionDescriptionStrapi.numero,
            consultationDTO.attributes.getNextQuestionId(questionDescriptionStrapi),
            consultationDTO.id,
            questionDescriptionStrapi.getImageUrl(),
            questionDescriptionStrapi.description.toHtml(),
            questionDescriptionStrapi.transcriptionImage,
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
