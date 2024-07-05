package fr.gouv.agora.infrastructure.consultationResults.dto

interface DemographicInfoCountByChoiceDTO {
    val choiceId: String
    val key: String?
    val count: Int
}
