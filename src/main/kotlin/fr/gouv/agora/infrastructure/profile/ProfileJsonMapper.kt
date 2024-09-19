package fr.gouv.agora.infrastructure.profile

import fr.gouv.agora.domain.*
import org.springframework.stereotype.Component

@Component
class ProfileJsonMapper {
    fun toDomain(json: ProfileJson, userId: String): ProfileInserting? {
        return ProfileInserting(
            gender = toGender(json.gender),
            yearOfBirth = json.yearOfBirth?.toIntOrNull(),
            department = Department.findByCode(json.department),
            cityType = toCityType(json.cityType),
            jobCategory = toJobCategory(json.jobCategory),
            voteFrequency = toFrequency(json.voteFrequency),
            publicMeetingFrequency = toFrequency(json.publicMeetingFrequency),
            consultationFrequency = toFrequency(json.consultationFrequency),
            userId = userId,
        )
    }

    fun toJson(profile: Profile): ProfileJson {
        return ProfileJson(
            gender = fromGender(profile.gender),
            yearOfBirth = profile.yearOfBirth?.toString(),
            department = Department.getDepartmentCode(profile.department),
            cityType = fromCityType(profile.cityType),
            jobCategory = fromJobCategory(profile.jobCategory),
            voteFrequency = fromFrequency(profile.voteFrequency),
            publicMeetingFrequency = fromFrequency(profile.publicMeetingFrequency),
            consultationFrequency = fromFrequency(profile.consultationFrequency),
        )
    }

    private fun toGender(gender: String?) = when (gender) {
        "M" -> Gender.MASCULIN
        "F" -> Gender.FEMININ
        "A" -> Gender.AUTRE
        else -> null
    }

    private fun fromGender(gender: Gender?) = when (gender) {
        Gender.MASCULIN -> "M"
        Gender.FEMININ -> "F"
        Gender.AUTRE -> "A"
        null -> null
    }

    private fun toCityType(cityType: String?) = when (cityType) {
        "R" -> CityType.RURAL
        "U" -> CityType.URBAIN
        "A" -> CityType.AUTRE
        else -> null
    }

    private fun fromCityType(cityType: CityType?) = when (cityType) {
        CityType.RURAL -> "R"
        CityType.URBAIN -> "U"
        CityType.AUTRE -> "A"
        null -> null
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

    private fun fromJobCategory(jobCategory: JobCategory?) = when (jobCategory) {
        JobCategory.AGRICULTEUR -> "AG"
        JobCategory.ARTISAN -> "AR"
        JobCategory.CADRE -> "CA"
        JobCategory.PROFESSION_INTERMEDIAIRE -> "PI"
        JobCategory.EMPLOYE -> "EM"
        JobCategory.OUVRIER -> "OU"
        JobCategory.ETUDIANTS -> "ET"
        JobCategory.RETRAITES -> "RE"
        JobCategory.AUTRESOUSANSACTIVITEPRO -> "AU"
        JobCategory.UNKNOWN -> "UN"
        null -> null
    }

    private fun toFrequency(frequencyString: String?) = when (frequencyString) {
        "S" -> Frequency.SOUVENT
        "P" -> Frequency.PARFOIS
        "J" -> Frequency.JAMAIS
        else -> null
    }

    private fun fromFrequency(frequency: Frequency?) = when (frequency) {
        Frequency.SOUVENT -> "S"
        Frequency.PARFOIS -> "P"
        Frequency.JAMAIS -> "J"
        null -> null
    }
}
