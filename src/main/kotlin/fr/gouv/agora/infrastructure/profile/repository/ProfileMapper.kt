package fr.gouv.agora.infrastructure.profile.repository

import fr.gouv.agora.domain.*
import fr.gouv.agora.infrastructure.profile.dto.ProfileDTO
import org.springframework.stereotype.Component
import java.util.*

@Component
class ProfileMapper {
    fun toDomain(dto: ProfileDTO): Profile? {
        return Profile(
            gender = toGender(dto.gender),
            yearOfBirth = dto.yearOfBirth,
            department = Department.findByCode(dto.department),
            cityType = toCityType(dto.cityType),
            jobCategory = toJobCategory(dto.jobCategory),
            voteFrequency = toFrequency(dto.voteFrequency),
            publicMeetingFrequency = toFrequency(dto.publicMeetingFrequency),
            consultationFrequency = toFrequency(dto.consultationFrequency),
            primaryDepartment = dto.primaryDepartment?.let { Territoire.Departement.from(it) },
            secondaryDepartment = dto.secondaryDepartment?.let { Territoire.Departement.from(it) },
        )
    }

    fun toDto(domain: ProfileInserting): ProfileDTO? {
        return try {
            ProfileDTO(
                id = UUID.randomUUID(),
                gender = fromGender(domain.gender),
                yearOfBirth = domain.yearOfBirth,
                department = Department.getDepartmentCode(domain.department),
                cityType = fromCityType(domain.cityType),
                jobCategory = fromJobCategory(domain.jobCategory),
                voteFrequency = fromFrequency(domain.voteFrequency),
                publicMeetingFrequency = fromFrequency(domain.publicMeetingFrequency),
                consultationFrequency = fromFrequency(domain.consultationFrequency),
                userId = UUID.fromString(domain.userId),
                primaryDepartment = null,
                secondaryDepartment = null,
            )
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    fun updateProfile(oldProfileDTO: ProfileDTO, newProfileDTO: ProfileDTO): ProfileDTO {
        return ProfileDTO(
            id = oldProfileDTO.id,
            gender = newProfileDTO.gender,
            yearOfBirth = newProfileDTO.yearOfBirth,
            department = newProfileDTO.department,
            cityType = newProfileDTO.cityType,
            jobCategory = newProfileDTO.jobCategory,
            voteFrequency = newProfileDTO.voteFrequency,
            publicMeetingFrequency = newProfileDTO.publicMeetingFrequency,
            consultationFrequency = newProfileDTO.consultationFrequency,
            userId = newProfileDTO.userId,
            primaryDepartment = oldProfileDTO.primaryDepartment,
            secondaryDepartment = oldProfileDTO.secondaryDepartment,
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
