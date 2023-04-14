package fr.social.gouv.agora.infrastructure.reponseConsultation

import fr.social.gouv.agora.usecase.reponseConsultation.InsertReponseConsultationUseCase
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")

class ReponseConsultationController(
    private val insertReponseConsultationUseCase: InsertReponseConsultationUseCase,
    private val jsonMapper: ReponseConsultationJsonMapper
) {
    @PostMapping("/consultations/{id}/responses")
    fun getQuestions(@RequestBody responsesConsultationJson: ReponsesConsultationJson): HttpEntity<*> {
        val reponseConsultationList = jsonMapper.toDomain(responsesConsultationJson)
        return ResponseEntity.ok()
            .body(reponseConsultationList.map {
                insertReponseConsultationUseCase.insertReponseConsultation(it)
            })
    }
}