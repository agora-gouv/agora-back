package fr.social.gouv.agora.infrastructure.question.repository

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.infrastructure.question.dto.ChoixPossibleDTO
import fr.social.gouv.agora.infrastructure.question.dto.QuestionDTO
import org.springframework.stereotype.Component
import java.util.*

@Component
class QuestionMapper(private val choixPossibleMapper: ChoixPossibleMapper) {

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

    private fun buildQuestionUniqueChoice(
        dto: QuestionDTO,
        nextQuestionId: UUID?,
        choixPossibleDtoList: List<ChoixPossibleDTO>,
    ) = QuestionUniqueChoice(
        id = dto.id.toString(),
        title = dto.title,
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
        order = dto.ordre,
        consultationId = dto.consultationId.toString(),
        nextQuestionId = nextQuestionId?.toString(),
        choixPossibleList = choixPossibleDtoList.map(choixPossibleMapper::toDefault),
        maxChoices = dto.maxChoices ?: throw IllegalStateException("maxChoices must be non-null for multiple type")
    )

    private fun buildQuestionOpen(dto: QuestionDTO, nextQuestionId: UUID?) = QuestionOpen(
        id = dto.id.toString(),
        title = dto.title,
        order = dto.ordre,
        consultationId = dto.consultationId.toString(),
        nextQuestionId = nextQuestionId?.toString(),
    )

    private fun buildChapter(dto: QuestionDTO, nextQuestionId: UUID?) = QuestionChapter(
        id = dto.id.toString(),
        title = dto.title,
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
        order = dto.ordre,
        consultationId = dto.consultationId.toString(),
        choixPossibleList = choixPossibleDtoList.mapNotNull(choixPossibleMapper::toConditional),
        nextQuestionId = nextQuestionId?.toString(),
    )

    private fun findNextQuestionId(dto: QuestionDTO, questionDTOList: List<QuestionDTO>): UUID? {
        return dto.nextQuestionId ?: questionDTOList.find { questionDTO -> questionDTO.ordre == dto.ordre + 1 }?.id
    }
}
