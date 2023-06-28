package fr.social.gouv.agora.infrastructure.consultationUpdates.repository

import fr.social.gouv.agora.domain.Explanation
import fr.social.gouv.agora.infrastructure.consultationUpdates.dto.ExplanationDTO
import org.springframework.stereotype.Component

@Component
class ExplanationMapper {

    companion object {
        private const val IS_TOGGLABLE_TRUE_VALUE = 1
    }

    fun toDomain(dto: ExplanationDTO): Explanation {
        return Explanation(
            isTogglable = dto.isTogglable == IS_TOGGLABLE_TRUE_VALUE,
            title = dto.title,
            intro = dto.intro,
            imageUrl = dto.imageUrl,
            description = dto.description,
        )
    }
}