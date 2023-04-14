package fr.social.gouv.agora.infrastructure.reponseConsultation

import fr.social.gouv.agora.usecase.reponseConsultation.InsertReponseConsultationUseCase
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@Suppress("unused")

class ReponseConsultationController(
    private val insertReponseConsultationUseCase: InsertReponseConsultationUseCase,
    private val jsonMapper: ReponseConsultationJsonMapper
) {
    @PostMapping("/consultations/{id}/responses")
    fun postResponseConsultation(@RequestBody responsesConsultationJson: ReponsesConsultationJson): HttpEntity<*> {
        val participationId = UUID.randomUUID()
        val reponseConsultationList = jsonMapper.toDomain(responsesConsultationJson, participationId)
        return ResponseEntity.ok()
            .body(reponseConsultationList.map {
                insertReponseConsultationUseCase.insertReponseConsultation(it)
            })
    }
}