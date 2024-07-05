package fr.gouv.agora.infrastructure.consultationResults.dto

import java.util.*

interface DemographicInfoCountByChoiceDTO {
    val choiceId: String
    val key: String?
    val count: Int
}
