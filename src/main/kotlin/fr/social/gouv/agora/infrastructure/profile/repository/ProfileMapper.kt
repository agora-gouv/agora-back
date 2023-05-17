package fr.social.gouv.agora.infrastructure.profile.repository

import fr.social.gouv.agora.domain.Profile
import fr.social.gouv.agora.infrastructure.profile.dto.ProfileDTO
import org.springframework.stereotype.Component

@Component
class ProfileMapper {
    fun toDomain(dto: ProfileDTO): Profile {
        return Profile(
            id = dto.id.toString(),
            gender = dto.gender,
            yearOfBirth = dto.yearOfBirth,
            department = dto.department,
            cityType = dto.cityType,
            jobCategory = dto.jobCategory,
            voteFrequency = dto.voteFrequency,
            publicMeetingFrequency = dto.publicMeetingFrequency,
            consultationFrequency = dto.consultationFrequency,
        )
    }
}