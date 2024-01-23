package fr.gouv.agora.infrastructure.reponseConsultation.repository

import fr.gouv.agora.domain.*
import fr.gouv.agora.infrastructure.reponseConsultation.dto.DemographicInfoCountByChoiceDTO
import fr.gouv.agora.infrastructure.reponseConsultation.dto.DemographicInfoCountDTO
import fr.gouv.agora.infrastructure.reponseConsultation.dto.ReponseConsultationDTO
import fr.gouv.agora.infrastructure.reponseConsultation.dto.ResponseConsultationCountDTO
import org.springframework.stereotype.Component
import java.time.LocalDate
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

    fun toDomain(
        genderCount: List<DemographicInfoCountDTO>,
        ageRangeCount: List<DemographicInfoCountDTO>,
        departmentCount: List<DemographicInfoCountDTO>,
        cityTypeCount: List<DemographicInfoCountDTO>,
        jobCategoryCount: List<DemographicInfoCountDTO>,
        voteFrequencyCount: List<DemographicInfoCountDTO>,
        publicMeetingFrequencyCount: List<DemographicInfoCountDTO>,
        consultationFrequencyCount: List<DemographicInfoCountDTO>,
    ): DemographicInfoCount {
        val currentYear = LocalDate.now().year
        return DemographicInfoCount(
            genderCount = genderCount.associate { toGender(it.key) to it.count },
            ageRangeCount = ageRangeCount.fold(
                initial = mutableMapOf(),
                operation = { buildingAgeRangeCount, demographicInfo ->
                    val ageRange = toAgeRange(currentYear = currentYear, demographicInfo.key)
                    buildingAgeRangeCount[ageRange] = (buildingAgeRangeCount[ageRange] ?: 0) + demographicInfo.count
                    buildingAgeRangeCount
                },
            ),
            departmentCount = departmentCount.associate { findDepartmentByNumber(it.key) to it.count },
            cityTypeCount = cityTypeCount.associate { toCityType(it.key) to it.count },
            jobCategoryCount = jobCategoryCount.associate { toJobCategory(it.key) to it.count },
            voteFrequencyCount = voteFrequencyCount.associate { toFrequency(it.key) to it.count },
            publicMeetingFrequencyCount = publicMeetingFrequencyCount.associate { toFrequency(it.key) to it.count },
            consultationFrequencyCount = consultationFrequencyCount.associate { toFrequency(it.key) to it.count },
        )
    }

    fun toDomain(
        genderCount: List<DemographicInfoCountByChoiceDTO>,
        ageRangeCount: List<DemographicInfoCountByChoiceDTO>,
        departmentCount: List<DemographicInfoCountByChoiceDTO>,
        cityTypeCount: List<DemographicInfoCountByChoiceDTO>,
        jobCategoryCount: List<DemographicInfoCountByChoiceDTO>,
        voteFrequencyCount: List<DemographicInfoCountByChoiceDTO>,
        publicMeetingFrequencyCount: List<DemographicInfoCountByChoiceDTO>,
        consultationFrequencyCount: List<DemographicInfoCountByChoiceDTO>,
    ): DemographicInfoCountByChoices {
        val currentYear = LocalDate.now().year
        val allChoices =
            (genderCount + ageRangeCount + departmentCount + cityTypeCount + jobCategoryCount + voteFrequencyCount + publicMeetingFrequencyCount + consultationFrequencyCount)
                .map { it.choiceId.toString() }
                .toSet()

        return DemographicInfoCountByChoices(
            choiceDemographicInfoMap = allChoices.associateWith { choice ->
                DemographicInfoCount(
                    genderCount = genderCount.filter { it.choiceId.toString() == choice }
                        .associate { toGender(it.key) to it.count },
                    ageRangeCount = ageRangeCount.filter { it.choiceId.toString() == choice }
                        .fold(
                            initial = mutableMapOf(),
                            operation = { buildingAgeRangeCount, demographicInfo ->
                                val ageRange = toAgeRange(currentYear = currentYear, demographicInfo.key)
                                buildingAgeRangeCount[ageRange] =
                                    (buildingAgeRangeCount[ageRange] ?: 0) + demographicInfo.count
                                buildingAgeRangeCount
                            },
                        ),
                    departmentCount = departmentCount.filter { it.choiceId.toString() == choice }
                        .associate { findDepartmentByNumber(it.key) to it.count },
                    cityTypeCount = cityTypeCount.filter { it.choiceId.toString() == choice }
                        .associate { toCityType(it.key) to it.count },
                    jobCategoryCount = jobCategoryCount.filter { it.choiceId.toString() == choice }
                        .associate { toJobCategory(it.key) to it.count },
                    voteFrequencyCount = voteFrequencyCount.filter { it.choiceId.toString() == choice }
                        .associate { toFrequency(it.key) to it.count },
                    publicMeetingFrequencyCount = publicMeetingFrequencyCount.filter { it.choiceId.toString() == choice }
                        .associate { toFrequency(it.key) to it.count },
                    consultationFrequencyCount = consultationFrequencyCount.filter { it.choiceId.toString() == choice }
                        .associate { toFrequency(it.key) to it.count },
                )
            }
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

    @Suppress("KotlinConstantConditions")
    private fun toAgeRange(currentYear: Int, yearOfBirthString: String?): AgeRange? {
        return yearOfBirthString?.toIntOrNull()?.let { yearOfBirth ->
            val age = currentYear - yearOfBirth
            when {
                age < 0 -> null
                age in 0..18 -> AgeRange.LESS_THAN_18
                age in 18..25 -> AgeRange.BETWEEN_18_AND_25
                age in 26..35 -> AgeRange.BETWEEN_26_AND_35
                age in 36..45 -> AgeRange.BETWEEN_36_AND_45
                age in 46..55 -> AgeRange.BETWEEN_46_AND_55
                age in 56..65 -> AgeRange.BETWEEN_56_AND_65
                age > 65 -> AgeRange.MORE_THAN_65
                else -> null
            }
        }
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