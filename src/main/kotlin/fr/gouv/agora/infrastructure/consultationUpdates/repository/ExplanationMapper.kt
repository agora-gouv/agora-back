package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.domain.Explanation
import fr.gouv.agora.infrastructure.consultationUpdates.dto.ExplanationDTO
import org.springframework.stereotype.Component

@Component
class ExplanationMapper {

    fun toDomain(dto: ExplanationDTO): Explanation {
        return Explanation(
            isTogglable = dto.toggleable.toBoolean(),
            title = dto.title,
            intro = dto.intro,
            imageUrl = dto.imageUrl,
            description = dto.description,
        )
    }
}