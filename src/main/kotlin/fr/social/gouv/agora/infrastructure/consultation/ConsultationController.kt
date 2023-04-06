package fr.social.gouv.agora.infrastructure.consultation

import fr.social.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import fr.social.gouv.agora.infrastructure.consultation.repository.ConsultationDatabaseRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@Suppress("unused")
class ConsultationController(private val consultationDatabaseRepository: ConsultationDatabaseRepository) {

    @GetMapping("/consultations/{id}")
    fun getConsultation(@PathVariable id: String): ResponseEntity<ConsultationDTO> {
        return ResponseEntity.ok()
            .body(consultationDatabaseRepository.getConsultation(UUID.fromString(id)))
    }

}