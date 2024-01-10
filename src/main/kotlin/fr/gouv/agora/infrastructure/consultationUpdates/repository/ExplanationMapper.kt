package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.domain.Explanation
import fr.gouv.agora.domain.ExplanationImage
import fr.gouv.agora.infrastructure.consultationUpdates.dto.ExplanationDTO
import org.springframework.stereotype.Component

@Component
class ExplanationMapper {

    fun toDomain(dto: ExplanationDTO): Explanation {
        return Explanation(
            isTogglable = dto.toggleable.toBoolean(),
            title = dto.title,
            intro = dto.intro,
            image = dto.imageUrl?.let { url ->
                ExplanationImage(
                    url = url,
                    description = dto.imageDescription,
                )
            },
            description = dto.description,
        )
    }
}