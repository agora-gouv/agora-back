package fr.social.gouv.agora.infrastructure.profile.repository

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.infrastructure.profile.dto.ProfileDTO
import fr.social.gouv.agora.usecase.profile.repository.ProfileInsertionResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class ProfileRepositoryImplInsertProfileTest {

    @Autowired
    private lateinit var repository: ProfileRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: ProfileDatabaseRepository

    @MockBean
    private lateinit var cacheRepository: ProfileCacheRepository

    @MockBean
    private lateinit var mapper: ProfileMapper

    private val profileDTO = ProfileDTO(
        id = UUID.randomUUID(),
        gender = "F",
        yearOfBirth = 1900,
        department = "75",
        cityType = "R",
        jobCategory = "AG",
        voteFrequency = "S",
        publicMeetingFrequency = "S",
        consultationFrequency = "S",
        userId = UUID.randomUUID(),
    )

    private val profile = ProfileInserting(
        gender = Gender.FEMININ,
        yearOfBirth = 1990,
        department = Department.ALLIER_3,
        cityType = CityType.URBAIN,
        jobCategory = JobCategory.OUVRIER,
        voteFrequency = Frequency.JAMAIS,
        publicMeetingFrequency = Frequency.PARFOIS,
        consultationFrequency = Frequency.SOUVENT,
        userId = "bc9e81be-eb4d-11ed-a05b-0242ac120010"
    )

    @Test
    fun `insertProfile - when mapper returns null - should return FAILURE`() {
        given(mapper.toDto(profile)).willReturn(null)

        // When
        val result = repository.insertProfile(profile)

        // Then
        assertThat(result).isEqualTo(ProfileInsertionResult.FAILURE)
        then(databaseRepository).shouldHaveNoInteractions()
        then(cacheRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `insertProfile - when mapper returns DTO and ID does not exist - should return SUCCESS`() {
        // Given
        given(mapper.toDto(profile)).willReturn(profileDTO)
        given(databaseRepository.existsById(profileDTO.id)).willReturn(false)

        // When
        val result = repository.insertProfile(profile)

        // Then
        assertThat(result).isEqualTo(ProfileInsertionResult.SUCCESS)
        then(databaseRepository).should(times(1)).save(profileDTO)
        then(cacheRepository).should(only())
            .insertProfile(userUUID = profileDTO.userId, profileDTO = profileDTO)
    }

    @Test
    fun `insertProfile - when mapper returns DTO and ID exist - should return SUCCESS and save with another DTO`() {
        // Given
        val profileDTO1 = profileDTO.copy(id = UUID.randomUUID())
        val profileDTO2 = profileDTO.copy(id = UUID.randomUUID())
        given(mapper.toDto(profile)).willReturn(profileDTO1, profileDTO2)
        given(databaseRepository.existsById(profileDTO1.id)).willReturn(true)
        given(databaseRepository.existsById(profileDTO2.id)).willReturn(false)

        // When
        val result = repository.insertProfile(profile)

        // Then
        assertThat(result).isEqualTo(ProfileInsertionResult.SUCCESS)
        then(mapper).should(times(2)).toDto(profile)
        inOrder(databaseRepository).also {
            then(databaseRepository).should(it, times(1)).existsById(profileDTO1.id)
            then(databaseRepository).should(it, times(1)).existsById(profileDTO2.id)
            then(databaseRepository).should(it).save(profileDTO2)
            it.verifyNoMoreInteractions()
        }
        then(cacheRepository).should(only())
            .insertProfile(profileDTO2.userId, profileDTO2)
    }

    @Test
    fun `insertProfile - when mapper returns DTO and ID exist 3 times - should return FAILURE`() {
        // Given
        given(mapper.toDto(profile)).willReturn(profileDTO)
        given(databaseRepository.existsById(profileDTO.id)).willReturn(true, true, true)

        // When
        val result = repository.insertProfile(profile)

        // Then
        assertThat(result).isEqualTo(ProfileInsertionResult.FAILURE)
        then(mapper).should(times(3)).toDto(profile)
        inOrder(databaseRepository).also {
            then(databaseRepository).should(it, times(3)).existsById(profileDTO.id)
            it.verifyNoMoreInteractions()
        }
        then(cacheRepository).shouldHaveNoInteractions()
    }
}