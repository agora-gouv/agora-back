package fr.gouv.agora.infrastructure.question.repository

import fr.gouv.agora.domain.Question
import fr.gouv.agora.infrastructure.common.StrapiAttributes
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationQuestionChoixMultiples
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationQuestionChoixUnique
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationQuestionConditionnelle
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationQuestionDescription
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationQuestionOuverte
import org.springframework.stereotype.Component

@Component
class QuestionsMapper(
    private val questionMapper: QuestionMapper
) {
    fun toDomain(consultationDTO: StrapiAttributes<ConsultationStrapiDTO>): List<Question> {
        return consultationDTO.attributes.questions.map { questionStrapi ->
            when (questionStrapi) {
                is StrapiConsultationQuestionChoixMultiples -> questionMapper.toQuestionChoixMultiple(
                    questionStrapi,
                    consultationDTO
                )

                is StrapiConsultationQuestionChoixUnique -> questionMapper.toQuestionChoixUnique(
                    questionStrapi,
                    consultationDTO
                )

                is StrapiConsultationQuestionOuverte -> questionMapper.toQuestionOuverte(
                    questionStrapi,
                    consultationDTO
                )

                is StrapiConsultationQuestionDescription -> questionMapper.toQuestionDescription(
                    questionStrapi,
                    consultationDTO
                )

                is StrapiConsultationQuestionConditionnelle -> questionMapper.toQuestionConditionnelle(
                    questionStrapi,
                    consultationDTO
                )
            }
        }
    }
}
