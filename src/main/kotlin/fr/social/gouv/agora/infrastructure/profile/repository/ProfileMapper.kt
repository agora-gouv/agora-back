package fr.social.gouv.agora.infrastructure.profile.repository

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.infrastructure.profile.dto.ProfileDTO
import org.springframework.stereotype.Component
import java.util.*

@Component
class ProfileMapper {
    fun toDomain(dto: ProfileDTO): Profile? {
        return try {
            toFrequency(dto.consultationFrequency)?.let {
                toFrequency(dto.publicMeetingFrequency)?.let { it1 ->
                    toFrequency(dto.voteFrequency)?.let { it2 ->
                        Profile(
                            gender = when (dto.gender) {
                                "M" -> Gender.MASCULIN
                                "F" -> Gender.FEMININ
                                "A" -> Gender.AUTRE
                                else -> throw IllegalArgumentException("Invalid profile gender: ${dto.gender}")
                            },
                            yearOfBirth = dto.yearOfBirth,
                            department = findDepartmentByNumber(dto.department)
                                ?: throw IllegalArgumentException("Invalid department: ${dto.department}"),
                            cityType = when (dto.cityType) {
                                "R" -> CityType.RURAL
                                "U" -> CityType.URBAIN
                                "A" -> CityType.AUTRE
                                else -> throw IllegalArgumentException("Invalid city type: ${dto.cityType}")
                            },
                            jobCategory = when (dto.jobCategory) {
                                "AG" -> JobCategory.AGRICULTEUR
                                "AR" -> JobCategory.ARTISAN
                                "CA" -> JobCategory.CADRE
                                "PI" -> JobCategory.PROFESSION_INTERMEDIAIRE
                                "EM" -> JobCategory.EMPLOYE
                                "OU" -> JobCategory.OUVRIER
                                "ND" -> JobCategory.NON_DETERMINE
                                "UN" -> JobCategory.UNKNOWN
                                else -> throw IllegalArgumentException("Invalid job category: ${dto.jobCategory}")
                            },
                            voteFrequency = it2,
                            publicMeetingFrequency = it1,
                            consultationFrequency = it,
                        )
                    }
                }
            }
        } catch (e: IllegalArgumentException) {
            println(e.message)
            null
        }
    }

    fun toDto(domain: ProfileInserting): ProfileDTO? {
        return try {
            ProfileDTO(
                id = UUID.randomUUID(),
                gender = when (domain.gender) {
                    Gender.MASCULIN -> "M"
                    Gender.FEMININ -> "F"
                    Gender.AUTRE -> "A"
                },
                yearOfBirth = domain.yearOfBirth,
                department = toDepartmentNumber(domain.department),
                cityType = when (domain.cityType) {
                    CityType.RURAL -> "R"
                    CityType.URBAIN -> "U"
                    CityType.AUTRE -> "A"
                },
                jobCategory = when (domain.jobCategory) {
                    JobCategory.AGRICULTEUR -> "AG"
                    JobCategory.ARTISAN -> "AR"
                    JobCategory.CADRE -> "CA"
                    JobCategory.PROFESSION_INTERMEDIAIRE -> "PI"
                    JobCategory.EMPLOYE -> "EM"
                    JobCategory.OUVRIER -> "OU"
                    JobCategory.NON_DETERMINE -> "ND"
                    JobCategory.UNKNOWN -> "UN"
                },
                voteFrequency = fromFrequency(domain.voteFrequency),
                publicMeetingFrequency = fromFrequency(domain.publicMeetingFrequency),
                consultationFrequency = fromFrequency(domain.consultationFrequency),
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

    private fun toFrequency(frequencyString: String): Frequency? {
        return try {
            when (frequencyString) {
                "S" -> Frequency.SOUVENT
                "P" -> Frequency.PARFOIS
                "J" -> Frequency.JAMAIS
                else -> throw IllegalArgumentException("Invalid frequency: $frequencyString")
            }
        } catch (e: IllegalArgumentException) {
            println(e.message)
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
}