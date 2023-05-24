package fr.social.gouv.agora.infrastructure.profile.repository

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.infrastructure.profile.dto.ProfileDTO
import org.springframework.stereotype.Component
import java.util.*

@Component
class ProfileMapper {
    fun toDomain(dto: ProfileDTO): Profile? {
        return Profile(
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

    fun toDto(domain: ProfileInserting): ProfileDTO? {
        return try {
            ProfileDTO(
                id = UUID.randomUUID(),
                gender = fromGender(domain.gender),
                yearOfBirth = domain.yearOfBirth,
                department = toDepartmentNumber(domain.department),
                cityType = fromCityType(domain.cityType),
                jobCategory = fromJobCategory(domain.jobCategory),
                voteFrequency = fromFrequency(domain.voteFrequency),
                publicMeetingFrequency = fromFrequency(domain.publicMeetingFrequency),
                consultationFrequency = fromFrequency(domain.consultationFrequency),
                userId = UUID.fromString(domain.userId),
            )
        } catch (e: IllegalArgumentException) {
            null
        }
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
        "ND" -> JobCategory.NON_DETERMINE
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
        JobCategory.NON_DETERMINE -> "ND"
        JobCategory.UNKNOWN -> "UN"
        null -> null
    }

    private fun findDepartmentByNumber(number: String?): Department? {
        return number?.let { Department.values().find { it.name.endsWith("_$number") } }
    }

    private fun toDepartmentNumber(department: Department?): String? {
        return department?.name?.substringAfterLast("_")
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