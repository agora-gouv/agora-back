package fr.social.gouv.agora.infrastructure.profile.repository

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.infrastructure.profile.dto.ProfileDTO
import org.springframework.stereotype.Component
import java.util.*

@Component
class ProfileMapper {
    fun toDomain(dto: ProfileDTO): Profile {
        return Profile(
            gender = when (dto.gender) {
                "M" -> Gender.MASCULIN
                "F" -> Gender.FEMININ_F
                "A" -> Gender.AUTRE_A
                else -> throw IllegalArgumentException("Invalid profile gender: ${dto.gender}")
            },
            yearOfBirth = dto.yearOfBirth,
            department = findDepartmentByNumber(dto.department)
                ?: throw IllegalArgumentException("Invalid department: ${dto.department}"),
            cityType = when (dto.cityType) {
                "R" -> CityType.RURAL_R
                "U" -> CityType.URBAIN_U
                "A" -> CityType.AUTRE_A
                else -> throw IllegalArgumentException("Invalid city type: ${dto.cityType}")
            },
            jobCategory = when (dto.jobCategory) {
                "AG" -> JobCategory.AGRICULTEUR_AG
                "AR" -> JobCategory.ARTISAN_AR
                "CA" -> JobCategory.CADRE_CA
                "PI" -> JobCategory.PROFESSION_INTERMEDIAIRE_PI
                "EM" -> JobCategory.EMPLOYE_EM
                "OU" -> JobCategory.OUVRIER_OU
                "ND" -> JobCategory.NON_DETERMINE_ND
                "UN" -> JobCategory.UNKNOWN_UN
                else -> throw IllegalArgumentException("Invalid job category: ${dto.jobCategory}")
            },
            voteFrequency = when (dto.voteFrequency) {
                "S" -> Frequency.SOUVENT_S
                "P" -> Frequency.PARFOIS_P
                "J" -> Frequency.JAMAIS_J
                else -> throw IllegalArgumentException("Invalid vote frequency: ${dto.voteFrequency}")
            },
            publicMeetingFrequency = when (dto.publicMeetingFrequency) {
                "S" -> Frequency.SOUVENT_S
                "P" -> Frequency.PARFOIS_P
                "J" -> Frequency.JAMAIS_J
                else -> throw IllegalArgumentException("Invalid public meeting frequency: ${dto.publicMeetingFrequency}")
            },
            consultationFrequency = when (dto.consultationFrequency) {
                "S" -> Frequency.SOUVENT_S
                "P" -> Frequency.PARFOIS_P
                "J" -> Frequency.JAMAIS_J
                else -> throw IllegalArgumentException("Invalid consultation frequency: ${dto.consultationFrequency}")
            },
        )
    }

    fun toDto(domain: ProfileInserting): ProfileDTO? {
        return try {
            ProfileDTO(
                id = UUID.randomUUID(),
                gender = when (domain.gender) {
                    Gender.MASCULIN -> "M"
                    Gender.FEMININ_F -> "F"
                    Gender.AUTRE_A -> "A"
                },
                yearOfBirth = domain.yearOfBirth,
                department = toDepartmentNumber(domain.department),
                cityType = when (domain.cityType) {
                    CityType.RURAL_R -> "R"
                    CityType.URBAIN_U -> "U"
                    CityType.AUTRE_A -> "A"
                },
                jobCategory = when (domain.jobCategory) {
                    JobCategory.AGRICULTEUR_AG -> "AG"
                    JobCategory.ARTISAN_AR -> "AR"
                    JobCategory.CADRE_CA -> "CA"
                    JobCategory.PROFESSION_INTERMEDIAIRE_PI -> "PI"
                    JobCategory.EMPLOYE_EM -> "EM"
                    JobCategory.OUVRIER_OU -> "OU"
                    JobCategory.NON_DETERMINE_ND -> "ND"
                    JobCategory.UNKNOWN_UN -> "UN"
                },
                voteFrequency = when (domain.voteFrequency) {
                    Frequency.SOUVENT_S -> "S"
                    Frequency.PARFOIS_P -> "P"
                    Frequency.JAMAIS_J -> "J"
                },
                publicMeetingFrequency = when (domain.publicMeetingFrequency) {
                    Frequency.SOUVENT_S -> "S"
                    Frequency.PARFOIS_P -> "P"
                    Frequency.JAMAIS_J -> "J"
                },
                consultationFrequency = when (domain.consultationFrequency) {
                    Frequency.SOUVENT_S -> "S"
                    Frequency.PARFOIS_P -> "P"
                    Frequency.JAMAIS_J -> "J"
                },
                userId = UUID.fromString(domain.userId),
            )
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun findDepartmentByNumber(number: String): Department? {
        return Department.values().find { it.name.endsWith("_$number") }
    }

    private fun toDepartmentNumber(department: Department): String {
        return department.name.substringAfterLast("_")
    }
}