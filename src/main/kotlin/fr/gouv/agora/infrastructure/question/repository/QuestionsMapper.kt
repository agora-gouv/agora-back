package fr.gouv.agora.infrastructure.question.repository

import fr.gouv.agora.domain.Question
import fr.gouv.agora.domain.QuestionChapter
import fr.gouv.agora.domain.QuestionConditional
import fr.gouv.agora.domain.QuestionMultipleChoices
import fr.gouv.agora.domain.QuestionOpen
import fr.gouv.agora.domain.QuestionUniqueChoice
import fr.gouv.agora.infrastructure.common.StrapiAttributes
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationQuestionChoixMultiples
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationQuestionChoixUnique
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationQuestionConditionnelle
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationQuestionDescription
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationQuestionOuverte
import fr.gouv.agora.infrastructure.question.dto.ChoixPossibleDTO
import fr.gouv.agora.infrastructure.question.dto.QuestionDTO
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class QuestionsMapper(
    private val choixPossibleMapper: ChoixPossibleMapper,
    private val questionMapper: QuestionMapper
) {
    companion object {
        private const val QUESTION_TYPE_UNIQUE_CHOICE = "unique"
        private const val QUESTION_TYPE_MULTIPLE_CHOICE = "multiple"
        private const val QUESTION_TYPE_OPEN = "open"
        private const val QUESTION_TYPE_CHAPTER = "chapter"
        private const val QUESTION_TYPE_CONDITIONAL = "conditional"
    }

    fun toDomain(
        dto: QuestionDTO,
        questionDTOList: List<QuestionDTO>,
        choixPossibleDTOList: List<ChoixPossibleDTO>,
    ): Question {
        return when (dto.type) {
            QUESTION_TYPE_UNIQUE_CHOICE -> buildQuestionUniqueChoice(
                dto = dto,
                nextQuestionId = findNextQuestionId(dto, questionDTOList),
                choixPossibleDtoList = choixPossibleDTOList,
            )

            QUESTION_TYPE_MULTIPLE_CHOICE -> buildQuestionMultipleChoice(
                dto = dto,
                nextQuestionId = findNextQuestionId(dto, questionDTOList),
                choixPossibleDtoList = choixPossibleDTOList,
            )

            QUESTION_TYPE_OPEN -> buildQuestionOpen(
                dto = dto,
                nextQuestionId = findNextQuestionId(dto, questionDTOList)
            )

            QUESTION_TYPE_CHAPTER -> buildChapter(dto = dto, nextQuestionId = findNextQuestionId(dto, questionDTOList))
            QUESTION_TYPE_CONDITIONAL -> buildQuestionConditional(
                dto = dto,
                nextQuestionId = findNextQuestionId(dto, questionDTOList),
                choixPossibleDTOList,
            )

            else -> throw IllegalArgumentException("Invalid question type ${dto.type}")
        }
    }

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

    private fun buildQuestionUniqueChoice(
        dto: QuestionDTO,
        nextQuestionId: UUID?,
        choixPossibleDtoList: List<ChoixPossibleDTO>,
    ) = QuestionUniqueChoice(
        id = dto.id.toString(),
        title = dto.title,
        popupDescription = dto.popupDescription,
        order = dto.ordre,
        consultationId = dto.consultationId.toString(),
        choixPossibleList = choixPossibleDtoList.map(choixPossibleMapper::toDefault),
        nextQuestionId = nextQuestionId?.toString(),
    )

    private fun buildQuestionMultipleChoice(
        dto: QuestionDTO,
        nextQuestionId: UUID?,
        choixPossibleDtoList: List<ChoixPossibleDTO>,
    ) = QuestionMultipleChoices(
        id = dto.id.toString(),
        title = dto.title,
        popupDescription = dto.popupDescription,
        order = dto.ordre,
        consultationId = dto.consultationId.toString(),
        nextQuestionId = nextQuestionId?.toString(),
        choixPossibleList = choixPossibleDtoList.map(choixPossibleMapper::toDefault),
        maxChoices = dto.maxChoices ?: throw IllegalStateException("maxChoices must be non-null for multiple type")
    )

    private fun buildQuestionOpen(dto: QuestionDTO, nextQuestionId: UUID?) = QuestionOpen(
        id = dto.id.toString(),
        title = dto.title,
        popupDescription = dto.popupDescription,
        order = dto.ordre,
        consultationId = dto.consultationId.toString(),
        nextQuestionId = nextQuestionId?.toString(),
    )

    private fun buildChapter(dto: QuestionDTO, nextQuestionId: UUID?) = QuestionChapter(
        id = dto.id.toString(),
        title = dto.title,
        popupDescription = dto.popupDescription,
        order = dto.ordre,
        consultationId = dto.consultationId.toString(),
        nextQuestionId = nextQuestionId?.toString(),
        description = dto.description ?: "",
    )

    private fun buildQuestionConditional(
        dto: QuestionDTO,
        nextQuestionId: UUID?,
        choixPossibleDtoList: List<ChoixPossibleDTO>,
    ) = QuestionConditional(
        id = dto.id.toString(),
        title = dto.title,
        popupDescription = dto.popupDescription,
        order = dto.ordre,
        consultationId = dto.consultationId.toString(),
        choixPossibleList = choixPossibleDtoList.mapNotNull(choixPossibleMapper::toConditional),
        nextQuestionId = nextQuestionId?.toString(),
    )

    private fun findNextQuestionId(dto: QuestionDTO, questionDTOList: List<QuestionDTO>): UUID? {
        return dto.nextQuestionId ?: questionDTOList.find { questionDTO -> questionDTO.ordre == dto.ordre + 1 }?.id
    }
}
