package fr.social.gouv.agora.infrastructure.question.repository

import fr.social.gouv.agora.domain.ChoixPossibleConditional
import fr.social.gouv.agora.domain.ChoixPossibleDefault
import fr.social.gouv.agora.infrastructure.question.dto.ChoixPossibleDTO
import org.springframework.stereotype.Component

@Component
class ChoixPossibleMapper {
    companion object {
        private const val HAS_OPEN_TEXT_FIELD_TRUE_VALUE = 1
    }
    fun toDefault(dto: ChoixPossibleDTO) = ChoixPossibleDefault(
        id = dto.id.toString(),
        label = dto.label,
        ordre = dto.ordre,
        questionId = dto.questionId.toString(),
        hasOpenTextField = dto.hasOpenTextField == HAS_OPEN_TEXT_FIELD_TRUE_VALUE
    )

    fun toConditional(dto: ChoixPossibleDTO) = dto.nextQuestionId?.let { nextQuestionId ->
        ChoixPossibleConditional(
            id = dto.id.toString(),
            label = dto.label,
            ordre = dto.ordre,
            questionId = dto.questionId.toString(),
            nextQuestionId = nextQuestionId.toString(),
            hasOpenTextField = dto.hasOpenTextField == HAS_OPEN_TEXT_FIELD_TRUE_VALUE
        )
    }
}
