package fr.gouv.agora.infrastructure.reponseConsultation.dto

import org.springframework.data.rest.core.config.Projection
import java.util.*

@Projection(types = [ResponseConsultationCountWithDemographicInfoDTO::class])
interface ResponseConsultationCountWithDemographicInfoDTO {
    val questionId: UUID
    val choiceId: UUID
    val responseCount: Int
    val gender: String?
    val yearOfBirth: Int?
    val department: String?
    val cityType: String?
    val jobCategory: String?
    val voteFrequency: String?
    val publicMeetingFrequency: String?
    val consultationFrequency: String?
}