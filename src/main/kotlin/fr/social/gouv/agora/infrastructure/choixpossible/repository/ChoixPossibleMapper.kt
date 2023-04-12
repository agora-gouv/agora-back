package fr.social.gouv.agora.infrastructure.choixpossible.repository

import fr.social.gouv.agora.domain.ChoixPossible
import fr.social.gouv.agora.infrastructure.choixpossible.dto.ChoixPossibleDTO
import fr.social.gouv.agora.infrastructure.utils.Mapper
import org.springframework.stereotype.Component
import java.util.*

@Component
class ChoixPossibleMapper : Mapper<ChoixPossible, ChoixPossibleDTO> {

    override fun toDomain(dto: ChoixPossibleDTO) = ChoixPossible(
        id = dto.id.toString(),
        label = dto.label,
        ordre = dto.ordre,
        questionId = dto.questionId.toString(),
    )

    override fun toDto(domain: ChoixPossible) = ChoixPossibleDTO(
        id = UUID.fromString(domain.id),
        label = domain.label,
        ordre = domain.ordre,
        questionId = UUID.fromString(domain.questionId),
    )

}
