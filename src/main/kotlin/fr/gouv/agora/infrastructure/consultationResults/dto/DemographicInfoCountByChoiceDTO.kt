package fr.gouv.agora.infrastructure.consultationResults.dto

import org.springframework.data.rest.core.config.Projection
import java.util.*

@Projection(types = [DemographicInfoCountByChoiceDTO::class])
interface DemographicInfoCountByChoiceDTO {
    val choiceId: UUID
    val key: String?
    val count: Int
}