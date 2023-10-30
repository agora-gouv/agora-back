package fr.gouv.agora.infrastructure.reponseConsultation.dto

import org.springframework.data.rest.core.config.Projection
import java.util.*

@Projection(types = [ResponseConsultationCountDTO::class])
interface ResponseConsultationCountDTO {
    val questionId: UUID
    val choiceId: UUID
    val responseCount: Int
}