package fr.social.gouv.agora.infrastructure.profile.repository

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.infrastructure.profile.dto.ProfileDTO
import fr.social.gouv.agora.infrastructure.profile.repository.ProfileCacheRepository.CacheResult
import fr.social.gouv.agora.usecase.profile.repository.ProfileInsertionResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
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
internal class ProfileRepositoryImplTest {

    @Autowired
    private lateinit var repository: ProfileRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: ProfileDatabaseRepository

    @MockBean
    private lateinit var cacheRepository: ProfileCacheRepository

    @MockBean
    private lateinit var mapper: ProfileMapper

    private val profileDTO = mock(ProfileDTO::class.java)
    private val profile = mock(Profile::class.java)
    private val userUUID = UUID.fromString("bc9e81be-eb4d-11ed-a05b-0242ac120003")

    @Nested
    inner class GetProfileCases {
        @Test
        fun `getProfile - when invalid UUID - should return null`() {
            // When
            val result = repository.getProfile(userId = "invalid UUID")

            // Then
            assertThat(result).isEqualTo(null)
            then(cacheRepository).shouldHaveNoInteractions()
            then(databaseRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `getProfile - when cacheResult is CacheNotInitialized and result not null from database - should return mapped object from database and insert it in cache`() {
            // Given
            given(cacheRepository.getProfile(userUUID = userUUID)).willReturn(CacheResult.CacheNotInitialized)
            given(databaseRepository.getProfile(userUUId = userUUID)).willReturn(profileDTO)
            given(mapper.toDomain(profileDTO)).willReturn(profile)

            // When
            val result = repository.getProfile(userId = userUUID.toString())

            // Then
            assertThat(result).isEqualTo(profile)
            then(cacheRepository).should(times(1)).getProfile(userUUID = userUUID)
            then(databaseRepository).should(only()).getProfile(userUUId = userUUID)
            then(cacheRepository).should(times(1)).insertProfile(userUUID, profileDTO)
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(mapper).should(only()).toDomain(profileDTO)
        }

        @Test
        fun `getProfile - when cacheResult is CacheNotInitialized and result is null from database - should return null and insert null in cache`() {
            // Given
            given(cacheRepository.getProfile(userUUID = userUUID)).willReturn(CacheResult.CacheNotInitialized)
            given(databaseRepository.getProfile(userUUId = userUUID)).willReturn(null)

            // When
            val result = repository.getProfile(userId = userUUID.toString())

            // Then
            assertThat(result).isEqualTo(null)
            then(cacheRepository).should(times(1)).getProfile(userUUID = userUUID)
            then(databaseRepository).should(only()).getProfile(userUUId = userUUID)
            then(cacheRepository).should(times(1)).insertProfile(userUUID, null)
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getProfile - when cacheResult is CachedProfileNotFound - should return null from cache`() {
            // Given
            given(cacheRepository.getProfile(userUUID = userUUID)).willReturn(CacheResult.CachedProfileNotFound)

            // When
            val result = repository.getProfile(userId = userUUID.toString())

            // Then
            assertThat(result).isEqualTo(null)
            then(cacheRepository).should(only()).getProfile(userUUID = userUUID)
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getProfile - when cacheResult is CachedProfile - should return mapped object from cache`() {
            // Given
            given(cacheRepository.getProfile(userUUID = userUUID)).willReturn(CacheResult.CachedProfile(profileDTO))
            given(mapper.toDomain(profileDTO)).willReturn(profile)

            // When
            val result = repository.getProfile(userId = userUUID.toString())

            // Then
            assertThat(result).isEqualTo(profile)
            then(cacheRepository).should(only()).getProfile(userUUID = userUUID)
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).should(only()).toDomain(profileDTO)
        }
    }

    @Nested
    inner class InsertProfileCases {

        private val profileInserting = mock(ProfileInserting::class.java)

        @Test
        fun `insertProfile - when mapper returns null - should return FAILURE`() {
            given(mapper.toDto(profileInserting)).willReturn(null)

            // When
            val result = repository.insertProfile(profileInserting)

            // Then
            assertThat(result).isEqualTo(ProfileInsertionResult.FAILURE)
            then(databaseRepository).shouldHaveNoInteractions()
            then(cacheRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `insertProfile - when mapper returns DTO - should return SUCCESS`() {
            // Given
            given(mapper.toDto(profileInserting)).willReturn(profileDTO)
            val savedProfileDTO = mock(ProfileDTO::class.java).also {
                given(it.userId).willReturn(UUID.randomUUID())
            }
            given(databaseRepository.save(profileDTO)).willReturn(savedProfileDTO)

            // When
            val result = repository.insertProfile(profileInserting)

            // Then
            assertThat(result).isEqualTo(ProfileInsertionResult.SUCCESS)
            then(databaseRepository).should(only()).save(profileDTO)
            then(cacheRepository).should(only()).insertProfile(
                userUUID = savedProfileDTO.userId,
                profileDTO = savedProfileDTO,
            )
        }

    }

}