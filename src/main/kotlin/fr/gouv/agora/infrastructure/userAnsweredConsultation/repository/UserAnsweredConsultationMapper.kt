package fr.gouv.agora.infrastructure.userAnsweredConsultation.repository

import fr.gouv.agora.domain.UserAnsweredConsultation
import fr.gouv.agora.infrastructure.userAnsweredConsultation.dto.UserAnsweredConsultationDTO
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserAnsweredConsultationMapper {

    fun toDto(domain: UserAnsweredConsultation): UserAnsweredConsultationDTO? {
        return try {
            UserAnsweredConsultationDTO(
                id = UUID.randomUUID(),
                participationDate = Date(),
                userId = UUID.fromString(domain.userId),
                consultationId = domain.consultationId
            )
        } catch (e: IllegalArgumentException) {
            null
        }
    }

}
