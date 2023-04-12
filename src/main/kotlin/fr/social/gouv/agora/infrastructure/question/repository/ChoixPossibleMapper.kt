package fr.social.gouv.agora.infrastructure.question.repository

import fr.social.gouv.agora.domain.ChoixPossible
import fr.social.gouv.agora.infrastructure.question.dto.ChoixPossibleDTO
import org.springframework.stereotype.Component

@Component
class ChoixPossibleMapper {

    fun toDomain(dto: ChoixPossibleDTO) = ChoixPossible(
        id = dto.id.toString(),
        label = dto.label,
        ordre = dto.ordre,
        questionId = dto.questionId.toString(),
    )

}
