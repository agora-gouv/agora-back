package fr.social.gouv.agora.infrastructure.profile

import fr.social.gouv.agora.domain.Profile
import fr.social.gouv.agora.infrastructure.profile.dto.ProfileDTO
import fr.social.gouv.agora.infrastructure.profile.repository.ProfileCacheRepository
import fr.social.gouv.agora.infrastructure.profile.repository.ProfileDatabaseRepository
import fr.social.gouv.agora.infrastructure.profile.repository.ProfileCacheRepository.CacheResult
import fr.social.gouv.agora.infrastructure.profile.repository.ProfileMapper
import fr.social.gouv.agora.infrastructure.profile.repository.ProfileRepositoryImpl
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
internal class ProfileRepositoryImplTest {

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

    private val profile = Profile(
        id = "bc9e81be-eb4d-11ed-a05b-0242ac120000",)

    private val userUUID = UUID.fromString("bc9e81be-eb4d-11ed-a05b-0242ac120003")

    @Test
    fun `getProfile - when invalid UUID - should return null`() {
        // When
        val result = repository.getProfile("invalid UUID")

        // Then
        assertThat(result).isEqualTo(null)
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getProfile - when cacheResult is CacheNotInitialized and result not null from database- should return mapped object from database and insert it in cache`() {
        // Given
        given(cacheRepository.getProfile(userUUID)).willReturn(CacheResult.CacheNotInitialized)
        given(databaseRepository.getProfile(userUUID)).willReturn(profileDTO)
        given(mapper.toDomain(profileDTO)).willReturn(profile)

        // When
        val result = repository.getProfile(userUUID.toString())

        // Then
        assertThat(result).isEqualTo(profile)
        then(cacheRepository).should(times(1)).getProfile(userUUID)
        then(databaseRepository).should(only()).getProfile(userUUID)
        then(cacheRepository).should(times(1)).insertProfile(userUUID, profileDTO)
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(mapper).should(only()).toDomain(profileDTO)
    }

    @Test
    fun `getProfile - when cacheResult is CacheNotInitialized and result is null from database- should return null and insert ProfileNotFound in cache`() {
        // Given
        given(cacheRepository.getProfile(userUUID)).willReturn(CacheResult.CacheNotInitialized)
        given(databaseRepository.getProfile(userUUID)).willReturn(null)

        // When
        val result = repository.getProfile(userUUID.toString())

        // Then
        assertThat(result).isEqualTo(null)
        then(cacheRepository).should(times(1)).getProfile(userUUID)
        then(databaseRepository).should(only()).getProfile(userUUID)
        then(cacheRepository).should(times(1)).insertProfile(userUUID, null)
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getProfile - when cacheResult is CachedProfileNotFound - should return mapped object from cache`() {
        // Given
        given(cacheRepository.getProfile(userUUID)).willReturn(CacheResult.CachedProfileNotFound)

        // When
        val result = repository.getProfile(userUUID.toString())

        // Then
        assertThat(result).isEqualTo(null)
        then(cacheRepository).should(only()).getProfile(userUUID)
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getProfile - when cacheResult is CachedProfile - should return mapped object from cache`() {
        // Given
        given(cacheRepository.getProfile(userUUID)).willReturn(CacheResult.CachedProfile(profileDTO))
        given(mapper.toDomain(profileDTO)).willReturn(profile)

        // When
        val result = repository.getProfile(userUUID.toString())

        // Then
        assertThat(result).isEqualTo(profile)
        then(cacheRepository).should(only()).getProfile(userUUID)
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).should(only()).toDomain(profileDTO)
    }
}