package fr.social.gouv.agora.infrastructure.question.repository

import fr.social.gouv.agora.domain.Question
import fr.social.gouv.agora.infrastructure.question.dto.ChoixPossibleDTO
import fr.social.gouv.agora.infrastructure.question.dto.QuestionDTO
import org.springframework.stereotype.Component

@Component
class QuestionMapper(private val choixPossibleMapper: ChoixPossibleMapper) {

    fun toDomain(dto: QuestionDTO, choixPossibleDtoList: List<ChoixPossibleDTO>) = Question(
        id = dto.id.toString(),
        label = dto.label,
        ordre = dto.ordre,
        type = dto.type,
        maxChoices = dto.maxChoices,
        consultationId = dto.consultationId.toString(),
        choixPossibleList = choixPossibleDtoList.map { choixPossibleDTO -> choixPossibleMapper.toDomain(choixPossibleDTO) },
    )
}
