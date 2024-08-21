package fr.gouv.agora.infrastructure.consultationResults

import fr.gouv.agora.usecase.consultationResponse.GetConsultationResultsWithDemographicRatiosUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
@Tag(name = "Admin")
class GetConsultationResponsesWithDemographicInfoController(
    private val consultationResultsUseCase: GetConsultationResultsWithDemographicRatiosUseCase,
    private val mapper: ConsultationResultWithDemographicInfoTsvMapper,
) {

    @Operation(
        summary = "Récupérer les réponses des consultations avec des infos utilisateurs (format fichier excel)",
        responses = [
            ApiResponse(responseCode = "200", description = "Success", content = [Content(mediaType = "text/plain")]),
            ApiResponse(
                responseCode = "400",
                description = "Not Found : L'id fourni ne correspond à aucune Consultation",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized : Votre compte ne possède pas les droits administrateur",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @GetMapping("/admin/consultations/{consultationId}/responses")
    fun getConsultationResults(
        @PathVariable @Parameter(
            description = "id de la consultation",
            example = "db0f2d59-7962-43ff-a69f-878204b7be95",
        ) consultationId: String
    ): ResponseEntity<*> {
        return consultationResultsUseCase.getConsultationResults(consultationId)?.let { consultationResult ->
            ResponseEntity.ok().header("Content-Type", "text/plain").body(mapper.buildTsvBody(consultationResult))
        } ?: ResponseEntity.badRequest().body(Unit)
    }

}
