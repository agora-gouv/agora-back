package fr.gouv.agora.infrastructure.consultationResults

import fr.gouv.agora.infrastructure.consultationResponse.repository.ConsultationResponseResultJsonCacheRepository
import fr.gouv.agora.usecase.consultationResults.ConsultationResultsWithUpdateUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class ConsultationResponsesWithUpdateController(
    private val getConsultationResultsUseCase: ConsultationResultsWithUpdateUseCase,
    private val cacheRepository: ConsultationResponseResultJsonCacheRepository,
    private val consultationResultJsonMapper: ConsultationResultJsonMapper,
) {

    @GetMapping("/consultations/{consultationId}/responses")
    fun getConsultationResultsWithUpdate(@PathVariable consultationId: String): ResponseEntity<*> {
        val resultJson = cacheRepository.getConsultationResults(consultationId)
            ?: getConsultationResultsFromUseCase(consultationId)

        return resultJson?.let { ResponseEntity.ok().body(resultJson) } ?: ResponseEntity.badRequest().body(Unit)
    }

    private fun getConsultationResultsFromUseCase(consultationId: String): ConsultationResultsWithUpdateJson? {
        return getConsultationResultsUseCase.getConsultationResults(consultationId)?.let { consultationResult ->
            consultationResultJsonMapper.toJson(consultationResult)
        }?.also { consultationResultJson ->
            cacheRepository.insertConsultationResults(consultationId, consultationResultJson)
        }
    }

}