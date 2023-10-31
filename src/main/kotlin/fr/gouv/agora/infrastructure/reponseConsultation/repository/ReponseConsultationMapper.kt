package fr.gouv.agora.infrastructure.reponseConsultation.repository

import fr.gouv.agora.domain.*
import fr.gouv.agora.infrastructure.reponseConsultation.dto.ReponseConsultationDTO
import fr.gouv.agora.infrastructure.reponseConsultation.dto.ResponseConsultationCountDTO
import fr.gouv.agora.infrastructure.reponseConsultation.dto.ResponseConsultationCountWithDemographicInfoDTO
import org.springframework.stereotype.Component
import java.util.*

@Component
class ReponseConsultationMapper {

    fun toDomain(dto: ReponseConsultationDTO): ReponseConsultation {
        return ReponseConsultation(
            id = dto.id.toString(),
            questionId = dto.questionId.toString(),
            choiceId = dto.choiceId.toString(),
            participationId = dto.participationId.toString(),
            userId = dto.userId.toString(),
            responseText = dto.responseText,
        )
    }

    fun toDomain(dto: ResponseConsultationCountDTO): ResponseConsultationCount {
        return ResponseConsultationCount(
            questionId = dto.questionId.toString(),
            choiceId = dto.choiceId.toString(),
            responseCount = dto.responseCount,
        )
    }

    fun toDomain(dto: ResponseConsultationCountWithDemographicInfoDTO): ResponseConsultationCountWithDemographicInfo {
        return ResponseConsultationCountWithDemographicInfo(
            questionId = dto.questionId.toString(),
            choiceId = dto.choiceId.toString(),
            responseCount = dto.responseCount,
            gender = toGender(dto.gender),
            yearOfBirth = dto.yearOfBirth,
            department = findDepartmentByNumber(dto.department),
            cityType = toCityType(dto.cityType),
            jobCategory = toJobCategory(dto.jobCategory),
            voteFrequency = toFrequency(dto.voteFrequency),
            publicMeetingFrequency = toFrequency(dto.publicMeetingFrequency),
            consultationFrequency = toFrequency(dto.consultationFrequency),
        )
    }

    fun toDto(
        consultationId: UUID,
        userId: UUID,
        participationId: UUID,
        domain: ReponseConsultationInserting,
    ): List<ReponseConsultationDTO> {
        return if (domain.choiceIds.isNullOrEmpty()) {
            listOfNotNull(
                toDto(
                    consultationId = consultationId,
                    userId = userId,
                    participationId = participationId,
                    domain = domain,
                    choiceId = null,
                )
            )
        } else {
            domain.choiceIds.mapNotNull { choiceId ->
                toDto(
                    consultationId = consultationId,
                    userId = userId,
                    participationId = participationId,
                    domain = domain,
                    choiceId = choiceId,
                )
            }
        }
    }

    private fun toDto(
        consultationId: UUID,
        userId: UUID,
        participationId: UUID,
        domain: ReponseConsultationInserting,
        choiceId: String?,
    ): ReponseConsultationDTO? {
        return try {
            ReponseConsultationDTO(
                id = UUID.randomUUID(),
                consultationId = consultationId,
                questionId = UUID.fromString(domain.questionId),
                choiceId = choiceId?.let { UUID.fromString(choiceId) },
                responseText = domain.responseText,
                participationId = participationId,
                participationDate = Date(),
                userId = userId,
            )
        } catch (e: IllegalStateException) {
            null
        }
    }

    private fun toGender(gender: String?) = when (gender) {
        "M" -> Gender.MASCULIN
        "F" -> Gender.FEMININ
        "A" -> Gender.AUTRE
        else -> null
    }

    private fun toCityType(cityType: String?) = when (cityType) {
        "R" -> CityType.RURAL
        "U" -> CityType.URBAIN
        "A" -> CityType.AUTRE
        else -> null
    }

    private fun toJobCategory(jobCategory: String?) = when (jobCategory) {
        "AG" -> JobCategory.AGRICULTEUR
        "AR" -> JobCategory.ARTISAN
        "CA" -> JobCategory.CADRE
        "PI" -> JobCategory.PROFESSION_INTERMEDIAIRE
        "EM" -> JobCategory.EMPLOYE
        "OU" -> JobCategory.OUVRIER
        "ET" -> JobCategory.ETUDIANTS
        "RE" -> JobCategory.RETRAITES
        "AU" -> JobCategory.AUTRESOUSANSACTIVITEPRO
        "UN" -> JobCategory.UNKNOWN
        else -> null
    }

    private fun findDepartmentByNumber(number: String?): Department? {
        return number?.let { Department.values().find { it.name.endsWith("_$number") } }
    }

    private fun toFrequency(frequencyString: String?) = when (frequencyString) {
        "S" -> Frequency.SOUVENT
        "P" -> Frequency.PARFOIS
        "J" -> Frequency.JAMAIS
        else -> null
    }

}