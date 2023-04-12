package fr.social.gouv.agora.infrastructure.question.repository

import fr.social.gouv.agora.domain.Question
import fr.social.gouv.agora.infrastructure.choixpossible.dto.ChoixPossibleDTO
import fr.social.gouv.agora.infrastructure.choixpossible.repository.ChoixPossibleMapper
import fr.social.gouv.agora.infrastructure.question.dto.QuestionDTO
import org.springframework.stereotype.Component

@Component
class QuestionMapper (private val choixPossibleMapper: ChoixPossibleMapper) {

    fun toDomain(dto: QuestionDTO, choixDTO: List<ChoixPossibleDTO> ) = Question(
        id = dto.id.toString(),
        label = dto.label,
        ordre = dto.ordre,
        type = dto.type,
        consultationId = dto.consultationId.toString(),
        choixPossibleList = choixDTO.map { choixPossibleDTO -> choixPossibleMapper.toDomain(choixPossibleDTO) },
    )
}
