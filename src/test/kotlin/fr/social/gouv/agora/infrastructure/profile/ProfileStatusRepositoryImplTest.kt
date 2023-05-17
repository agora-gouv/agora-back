package fr.social.gouv.agora.infrastructure.profile

import fr.social.gouv.agora.infrastructure.profile.dto.ProfileDTO
import fr.social.gouv.agora.infrastructure.profile.repository.CachedProfileDTO
import fr.social.gouv.agora.infrastructure.profile.repository.ProfileDatabaseRepository
import fr.social.gouv.agora.infrastructure.profile.repository.ProfileCacheRepository
import fr.social.gouv.agora.infrastructure.profile.repository.ProfileRepositoryImpl
import fr.social.gouv.agora.infrastructure.profile.repository.ProfileCacheRepository.CacheResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.cache.annotation.EnableCaching
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
@EnableCaching
@ImportAutoConfiguration(
    CacheAutoConfiguration::class,
    RedisAutoConfiguration::class,
)
internal class ProfileStatusRepositoryImplTest {

    @Autowired
    private lateinit var repository: ProfileRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: ProfileDatabaseRepository

    @MockBean
    private lateinit var cacheRepository: ProfileCacheRepository

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

    private val userUUID = UUID.fromString("bc9e81be-eb4d-11ed-a05b-0242ac120003")

    @Test
    fun `askForDemographicInfo - when invalid UUID - should return false`() {
        // When
        val result = repository.askForDemographicInfo("invalid UUID")

        // Then
        assertThat(result).isEqualTo(false)
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `askForDemographicInfo - when userId is null - should return false`() {
        // When
        val result = repository.askForDemographicInfo(null)

        // Then
        assertThat(result).isEqualTo(false)
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `askForDemographicInfo - when CacheNotInitialized and database return null - should return true`() {
        //Given
        given(cacheRepository.getProfileStatus(userUUID)).willReturn(CacheResult.CacheNotInitialized)
        given(databaseRepository.getProfile(userUUID)).willReturn(null)

        // When
        val result = repository.askForDemographicInfo(userUUID.toString())

        // Then
        assertThat(result).isEqualTo(true)
        then(cacheRepository).should(times(1)).getProfileStatus(userUUID)
        then(databaseRepository).should(only()).getProfile(userUUID)
        then(cacheRepository).should(times(1)).insertProfile(userUUID, null)
    }

    @Test
    fun `askForDemographicInfo - when CacheNotInitialized and database return DTO - should return false`() {
        //Given
        given(cacheRepository.getProfileStatus(userUUID)).willReturn(CacheResult.CacheNotInitialized)
        given(databaseRepository.getProfile(userUUID)).willReturn(profileDTO)

        // When
        val result = repository.askForDemographicInfo(userUUID.toString())

        // Then
        assertThat(result).isEqualTo(false)
        then(cacheRepository).should(times(1)).getProfileStatus(userUUID)
        then(databaseRepository).should(only()).getProfile(userUUID)
        then(cacheRepository).should(times(1)).insertProfile(userUUID, profileDTO)
    }

    @Test
    fun `askForDemographicInfo - when CachedProfileFound - should do nothing and return false`() {
        //Given
        given(cacheRepository.getProfileStatus(userUUID)).willReturn(CacheResult.CachedProfileFound)

        // When
        val result = repository.askForDemographicInfo(userUUID.toString())

        // Then
        assertThat(result).isEqualTo(false)
        then(cacheRepository).should(only()).getProfileStatus(userUUID)
        then(databaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `askForDemographicInfo - when CachedProfileNotFound and SYS DATE superior ASK DATE + 30 days - should return true`() {
        //Given
        given(cacheRepository.getProfileStatus(userUUID)).willReturn(CacheResult.CachedProfileNotFound(
            CachedProfileDTO("2023-01-06",profileDTO)
        ))

        // When
        val result = repository.askForDemographicInfo(userUUID.toString())

        // Then
        assertThat(result).isEqualTo(true)
        then(cacheRepository).should(times(1)).getProfileStatus(userUUID)
        then(cacheRepository).should(times(1)).updateDemandeProfileDate(userUUID)
        then(databaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `askForDemographicInfo - when CachedProfileNotFound and SYS DATE inferior ASK DATE + 30 days - should return false`() {
        //Given
        given(cacheRepository.getProfileStatus(userUUID)).willReturn(CacheResult.CachedProfileNotFound(
            CachedProfileDTO("2023-05-06",profileDTO)))

        // When
        val result = repository.askForDemographicInfo(userUUID.toString())

        // Then
        assertThat(result).isEqualTo(false)
        then(cacheRepository).should(only()).getProfileStatus(userUUID)
        then(databaseRepository).shouldHaveNoInteractions()
    }
}