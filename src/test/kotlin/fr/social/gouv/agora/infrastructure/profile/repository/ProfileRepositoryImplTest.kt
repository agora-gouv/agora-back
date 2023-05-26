package fr.social.gouv.agora.infrastructure.profile.repository

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.infrastructure.profile.dto.ProfileDTO
import fr.social.gouv.agora.infrastructure.profile.repository.ProfileCacheRepository.CacheResult
import fr.social.gouv.agora.usecase.profile.repository.ProfileEditResult
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
            assertThat(result).isEqualTo(ProfileEditResult.FAILURE)
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
            assertThat(result).isEqualTo(ProfileEditResult.SUCCESS)
            then(databaseRepository).should(only()).save(profileDTO)
            then(cacheRepository).should(only()).insertProfile(
                userUUID = savedProfileDTO.userId,
                profileDTO = savedProfileDTO,
            )
        }

    }

    @Nested
    inner class UpdateProfileCases {

        private val profileInserting = mock(ProfileInserting::class.java)

        @Test
        fun `updateProfile - when mapper returns null - should return Failure`() {
            // Given
            given(mapper.toDto(profileInserting)).willReturn(null)

            // When
            val result = repository.updateProfile(profileInserting)

            // Then
            assertThat(result).isEqualTo(ProfileEditResult.FAILURE)
            then(cacheRepository).shouldHaveNoInteractions()
            then(databaseRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `updateProfile - when cacheResult is CachedProfileNotFound - should return failure`() {
            // Given
            val userId = UUID.randomUUID()
            val newProfileDTO = mock(ProfileDTO::class.java).also {
                given(it.userId).willReturn(userId)
            }
            given(mapper.toDto(profileInserting)).willReturn(newProfileDTO)
            given(cacheRepository.getProfile(userId)).willReturn(CacheResult.CachedProfileNotFound)

            // When
            val result = repository.updateProfile(profileInserting)

            // Then
            assertThat(result).isEqualTo(ProfileEditResult.FAILURE)
            then(cacheRepository).should(only()).getProfile(userId)
            then(databaseRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `updateProfile - when cacheResult is CachedProfile - should update database and cache then return success`() {
            // Given
            val userId = UUID.randomUUID()
            val newProfileDTO = mock(ProfileDTO::class.java).also {
                given(it.userId).willReturn(userId)
            }
            given(mapper.toDto(profileInserting)).willReturn(newProfileDTO)

            val oldProfileDTO = mock(ProfileDTO::class.java)
            given(cacheRepository.getProfile(userId)).willReturn(CacheResult.CachedProfile(oldProfileDTO))

            val updatedProfileDTO = mock(ProfileDTO::class.java)
            given(mapper.updateProfile(oldProfileDTO, newProfileDTO)).willReturn(updatedProfileDTO)

            val savedProfileDTO = mock(ProfileDTO::class.java).also {
                given(it.userId).willReturn(userId)
            }
            given(databaseRepository.save(updatedProfileDTO)).willReturn(savedProfileDTO)

            // When
            val result = repository.updateProfile(profileInserting)

            // Then
            assertThat(result).isEqualTo(ProfileEditResult.SUCCESS)
            then(cacheRepository).should().getProfile(userId)
            then(cacheRepository).should().insertProfile(userId, savedProfileDTO)
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).save(updatedProfileDTO)
        }

        @Test
        fun `updateProfile - when cacheResult is CacheNotInitialized and database returns null - should return failure`() {
            // Given
            val userId = UUID.randomUUID()
            val newProfileDTO = mock(ProfileDTO::class.java).also {
                given(it.userId).willReturn(userId)
            }
            given(mapper.toDto(profileInserting)).willReturn(newProfileDTO)
            given(cacheRepository.getProfile(userId)).willReturn(CacheResult.CacheNotInitialized)
            given(databaseRepository.getProfile(userId)).willReturn(null)

            // When
            val result = repository.updateProfile(profileInserting)

            // Then
            assertThat(result).isEqualTo(ProfileEditResult.FAILURE)
            then(cacheRepository).should(only()).getProfile(userId)
            then(databaseRepository).should(only()).getProfile(userId)
        }

        @Test
        fun `updateProfile - when cacheResult is CacheNotInitialized and database returns dto - should update database and cache then return success`() {
            // Given
            val userId = UUID.randomUUID()
            val newProfileDTO = mock(ProfileDTO::class.java).also {
                given(it.userId).willReturn(userId)
            }
            given(mapper.toDto(profileInserting)).willReturn(newProfileDTO)

            val oldProfileDTO = mock(ProfileDTO::class.java)
            given(cacheRepository.getProfile(userId)).willReturn(CacheResult.CacheNotInitialized)
            given(databaseRepository.getProfile(userId)).willReturn(oldProfileDTO)

            val updatedProfileDTO = mock(ProfileDTO::class.java)
            given(mapper.updateProfile(oldProfileDTO, newProfileDTO)).willReturn(updatedProfileDTO)

            val savedProfileDTO = mock(ProfileDTO::class.java).also {
                given(it.userId).willReturn(userId)
            }
            given(databaseRepository.save(updatedProfileDTO)).willReturn(savedProfileDTO)

            // When
            val result = repository.updateProfile(profileInserting)

            // Then
            assertThat(result).isEqualTo(ProfileEditResult.SUCCESS)
            then(cacheRepository).should().getProfile(userId)
            then(cacheRepository).should().insertProfile(userId, savedProfileDTO)
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should().getProfile(userId)
            then(databaseRepository).should().save(updatedProfileDTO)
            then(databaseRepository).shouldHaveNoMoreInteractions()
        }
    }

}