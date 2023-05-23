package fr.social.gouv.agora.infrastructure.profile

import fr.social.gouv.agora.domain.*
import org.springframework.stereotype.Component

@Component
class ProfileJsonMapper {
    fun toDomain(json: ProfileJson, userId: String): ProfileInserting? {
        return try {
            toFrequency(json.voteFrequency)?.let { voteFrequency ->
                toFrequency(json.publicMeetingFrequency)?.let { publicMeetingFrequency ->
                    toFrequency(json.consultationFrequency)?.let { consultationFrequency ->
                        ProfileInserting(
                            gender = when (json.gender) {
                                "M" -> Gender.MASCULIN
                                "F" -> Gender.FEMININ
                                "A" -> Gender.AUTRE
                                else -> throw IllegalArgumentException("Invalid profile gender: ${json.gender}")
                            },
                            yearOfBirth = json.yearOfBirth,
                            department = findDepartmentByNumber(json.department)
                                ?: throw IllegalArgumentException("Invalid department: ${json.department}"),
                            cityType = when (json.cityType) {
                                "R" -> CityType.RURAL
                                "U" -> CityType.URBAIN
                                "A" -> CityType.AUTRE
                                else -> throw IllegalArgumentException("Invalid city type: ${json.cityType}")
                            },
                            jobCategory = when (json.jobCategory) {
                                "AG" -> JobCategory.AGRICULTEUR
                                "AR" -> JobCategory.ARTISAN
                                "CA" -> JobCategory.CADRE
                                "PI" -> JobCategory.PROFESSION_INTERMEDIAIRE
                                "EM" -> JobCategory.EMPLOYE
                                "OU" -> JobCategory.OUVRIER
                                "ND" -> JobCategory.NON_DETERMINE
                                "UN" -> JobCategory.UNKNOWN
                                else -> throw IllegalArgumentException("Invalid job category: ${json.jobCategory}")
                            },
                            voteFrequency = voteFrequency,
                            publicMeetingFrequency = publicMeetingFrequency,
                            consultationFrequency = consultationFrequency,
                            userId = userId,
                        )
                    }
                }
            }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    fun toJson(profile: Profile): ProfileJson {
        return ProfileJson(
            gender = when (profile.gender) {
                Gender.MASCULIN -> "M"
                Gender.FEMININ -> "F"
                Gender.AUTRE -> "A"
            },
            yearOfBirth = profile.yearOfBirth,
            department = toDepartmentNumber(profile.department),
            cityType = when (profile.cityType) {
                CityType.RURAL -> "R"
                CityType.URBAIN -> "U"
                CityType.AUTRE -> "A"
            },
            jobCategory = when (profile.jobCategory) {
                JobCategory.AGRICULTEUR -> "AG"
                JobCategory.ARTISAN -> "AR"
                JobCategory.CADRE -> "CA"
                JobCategory.PROFESSION_INTERMEDIAIRE -> "PI"
                JobCategory.EMPLOYE -> "EM"
                JobCategory.OUVRIER -> "OU"
                JobCategory.NON_DETERMINE -> "ND"
                JobCategory.UNKNOWN -> "UN"
            },
            voteFrequency = fromFrequency(profile.voteFrequency),
            publicMeetingFrequency = fromFrequency(profile.publicMeetingFrequency),
            consultationFrequency = fromFrequency(profile.consultationFrequency),
        )
    }

    private fun findDepartmentByNumber(number: String): Department? {
        return Department.values().find { it.name.endsWith("_$number") }
    }

    private fun toFrequency(frequencyString: String): Frequency? {
        return try {
            when (frequencyString) {
                "S" -> Frequency.SOUVENT
                "P" -> Frequency.PARFOIS
                "J" -> Frequency.JAMAIS
                else -> throw IllegalArgumentException("Invalid frequency: $frequencyString")
            }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun fromFrequency(frequency: Frequency): String {
        return when (frequency) {
            Frequency.SOUVENT -> "S"
            Frequency.PARFOIS -> "P"
            Frequency.JAMAIS -> "J"
        }
    }

    private fun toDepartmentNumber(department: Department): String {
        return department.name.substringAfterLast("_")
    }
}