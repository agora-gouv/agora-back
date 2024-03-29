package fr.gouv.agora.infrastructure.consultationResults.dto

import org.springframework.data.rest.core.config.Projection

@Projection(types = [DemographicInfoCountDTO::class])
interface DemographicInfoCountDTO {
    val key: String?
    val count: Int
}