package fr.gouv.agora.infrastructure.profile.repository

import fr.gouv.agora.infrastructure.profile.dto.DemographicInfoAskDateDTO
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDate
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

@Component
class DemographicInfoAskDateMapper {

    fun toDate(dto: DemographicInfoAskDateDTO): LocalDate {
        return dto.askDate.toLocalDate()
    }

    fun toDto(userId: UUID): DemographicInfoAskDateDTO {
        return DemographicInfoAskDateDTO(
            id = UUID.randomUUID(),
            askDate = Date(),
            userId = userId,
        )
    }

}