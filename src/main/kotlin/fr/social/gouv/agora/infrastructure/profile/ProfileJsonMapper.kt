package fr.social.gouv.agora.infrastructure.profile

import fr.social.gouv.agora.domain.*
import org.springframework.stereotype.Component

@Component
class ProfileJsonMapper {
    fun toDomain(json: ProfileJson, userId: String): ProfileInserting? {
        return try {
            toFrequency(json.voteFrequency)?.let {
                toFrequency(json.publicMeetingFrequency)?.let { it1 ->
                    toFrequency(json.consultationFrequency)?.let { it2 ->
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
                            voteFrequency = it,
                            publicMeetingFrequency = it1,
                            consultationFrequency = it2,
                            userId = userId,
                        )
                    }
                }
            }
        } catch (e: IllegalArgumentException) {
            println(e.message)
            null
        }
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
                else -> throw IllegalArgumentException("Invalid public meeting frequency: $frequencyString")
            }
        } catch (e: IllegalArgumentException) {
            println(e.message)
            null
        }
    }
}