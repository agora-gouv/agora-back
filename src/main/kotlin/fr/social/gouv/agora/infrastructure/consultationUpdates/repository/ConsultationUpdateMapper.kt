package fr.social.gouv.agora.infrastructure.consultationUpdates.repository

import fr.social.gouv.agora.domain.ConsultationStatus
import fr.social.gouv.agora.domain.ConsultationUpdate
import fr.social.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateDTO
import org.springframework.stereotype.Component

@Component
class ConsultationUpdateMapper {

    fun toDomain(dto: ConsultationUpdateDTO): ConsultationUpdate {
        return ConsultationUpdate(
            status = when (dto.step) {
                1 -> ConsultationStatus.COLLECTING_DATA
                2 -> ConsultationStatus.POLITICAL_COMMITMENT
                3 -> ConsultationStatus.EXECUTION
                else -> throw IllegalArgumentException("Invalid consultation update status: ${dto.step}")
            },
            description = dto.description,
        )
    }

}