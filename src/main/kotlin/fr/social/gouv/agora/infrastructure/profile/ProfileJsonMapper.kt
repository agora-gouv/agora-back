package fr.social.gouv.agora.infrastructure.profile

import fr.social.gouv.agora.domain.*
import org.springframework.stereotype.Component

@Component
class ProfileJsonMapper {
    fun toDomain(json: ProfileJson): Profile {
        return Profile(
            gender = when (json.gender) {
                "M" -> Gender.MASCULIN
                "F" -> Gender.FEMININ_F
                "A" -> Gender.AUTRE_A
                else -> throw IllegalArgumentException("Invalid profile gender: ${json.gender}")
            },
            yearOfBirth = json.yearOfBirth,
            department = findDepartmentByNumber(json.department)
                ?: throw IllegalArgumentException("Invalid department: ${json.department}"),
            cityType = when (json.cityType) {
                "R" -> CityType.RURAL_R
                "U" -> CityType.URBAIN_U
                "A" -> CityType.AUTRE_A
                else -> throw IllegalArgumentException("Invalid city type: ${json.cityType}")
            },
            jobCategory = when (json.jobCategory) {
                "AG" -> JobCategory.AGRICULTEUR_AG
                "AR" -> JobCategory.ARTISAN_AR
                "CA" -> JobCategory.CADRE_CA
                "PI" -> JobCategory.PROFESSION_INTERMEDIAIRE_PI
                "EM" -> JobCategory.EMPLOYE_EM
                "OU" -> JobCategory.OUVRIER_OU
                "ND" -> JobCategory.NON_DETERMINE_ND
                "UN" -> JobCategory.UNKNOWN_UN
                else -> throw IllegalArgumentException("Invalid job category: ${json.jobCategory}")
            },
            voteFrequency = when (json.voteFrequency) {
                "S" -> Frequency.SOUVENT_S
                "P" -> Frequency.PARFOIS_P
                "J" -> Frequency.JAMAIS_J
                else -> throw IllegalArgumentException("Invalid vote frequency: ${json.voteFrequency}")
            },
            publicMeetingFrequency = when (json.publicMeetingFrequency) {
                "S" -> Frequency.SOUVENT_S
                "P" -> Frequency.PARFOIS_P
                "J" -> Frequency.JAMAIS_J
                else -> throw IllegalArgumentException("Invalid public meeting frequency: ${json.publicMeetingFrequency}")
            },
            consultationFrequency = when (json.consultationFrequency) {
                "S" -> Frequency.SOUVENT_S
                "P" -> Frequency.PARFOIS_P
                "J" -> Frequency.JAMAIS_J
                else -> throw IllegalArgumentException("Invalid consultation frequency: ${json.consultationFrequency}")
            },
        )
    }

    private fun findDepartmentByNumber(number: String): Department? {
        return Department.values().find { it.name.endsWith("_$number") }
    }
}