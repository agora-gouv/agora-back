package fr.social.gouv.agora.infrastructure.reponseConsultation

import fr.social.gouv.agora.infrastructure.reponseConsultation.repository.ConsultationResponseResultJsonCacheRepository
import fr.social.gouv.agora.usecase.reponseConsultation.GetConsultationResultsUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class GetConsultationResponsesController(
    private val getConsultationResultsUseCase: GetConsultationResultsUseCase,
    private val cacheRepository: ConsultationResponseResultJsonCacheRepository,
    private val consultationResultJsonMapper: ConsultationResultJsonMapper,
) {

    @GetMapping("/consultations/{consultationId}/responses")
    fun getConsultationResults(@PathVariable consultationId: String): ResponseEntity<*> {
        val resultJson = cacheRepository.getConsultationResults(consultationId)
            ?: getConsultationResultsFromUseCase(consultationId)

        return resultJson?.let { ResponseEntity.ok().body(resultJson) } ?: ResponseEntity.badRequest().body(Unit)
    }

    private fun getConsultationResultsFromUseCase(consultationId: String): ConsultationResultJson? {
        return getConsultationResultsUseCase.getConsultationResults(consultationId)?.let { consultationResult ->
            consultationResultJsonMapper.toJson(consultationResult)
        }?.also { consultationResultJson ->
            cacheRepository.insertConsultationResults(consultationId, consultationResultJson)
        }
    }

}