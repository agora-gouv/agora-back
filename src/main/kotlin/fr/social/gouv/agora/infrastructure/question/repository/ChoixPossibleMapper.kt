package fr.social.gouv.agora.infrastructure.question.repository

import fr.social.gouv.agora.domain.ChoixPossibleConditional
import fr.social.gouv.agora.domain.ChoixPossibleDefault
import fr.social.gouv.agora.infrastructure.question.dto.ChoixPossibleDTO
import org.springframework.stereotype.Component

@Component
class ChoixPossibleMapper {

    fun toDefault(dto: ChoixPossibleDTO) = ChoixPossibleDefault(
        id = dto.id.toString(),
        label = dto.label,
        ordre = dto.ordre,
        questionId = dto.questionId.toString(),
    )

    fun toConditional(dto: ChoixPossibleDTO) = dto.nextQuestionId?.let { nextQuestionId ->
        ChoixPossibleConditional(
            id = dto.id.toString(),
            label = dto.label,
            ordre = dto.ordre,
            questionId = dto.questionId.toString(),
            nextQuestionId = nextQuestionId.toString(),
        )
    }

}
