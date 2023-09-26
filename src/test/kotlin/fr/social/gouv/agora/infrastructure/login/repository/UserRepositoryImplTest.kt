package fr.social.gouv.agora.infrastructure.login.repository

import fr.social.gouv.agora.domain.UserInfo
import fr.social.gouv.agora.infrastructure.login.dto.UserDTO
import fr.social.gouv.agora.infrastructure.login.repository.LoginCacheRepository.CacheResult
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
internal class UserRepositoryImplTest {

    @Autowired
    private lateinit var repository: UserRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: LoginDatabaseRepository

    @MockBean
    private lateinit var cacheRepository: LoginCacheRepository

    @MockBean
    private lateinit var mapper: UserInfoMapper

    @Nested
    inner class GetAllQagInfoTestCases {

        @Test
        fun `getAllUsers - should call database and return mapped results`() {
            // Given
            val userDTO = mock(UserDTO::class.java)
            given(databaseRepository.findAll()).willReturn(listOf(userDTO))

            val userInfo = mock(UserInfo::class.java)
            given(mapper.toDomain(userDTO)).willReturn(userInfo)

            // When
            val result = repository.getAllUsers()

            // Then
            assertThat(result).isEqualTo(listOf(userInfo))
            inOrder(databaseRepository, mapper).also {
                then(databaseRepository).should(it).findAll()
                then(mapper).should(it).toDomain(userDTO)
                it.verifyNoMoreInteractions()
            }
            then(cacheRepository).shouldHaveNoInteractions()
        }

    }

    @Nested
    inner class GetUserByIdCases {

        private val userId = UUID.randomUUID()

        @Test
        fun `getUserById - when invalid user UUID - should return null without doing anything`() {
            // When
            val result = repository.getUserById(userId = "invalid userId")

            // Then
            assertThat(result).isEqualTo(null)
            then(cacheRepository).shouldHaveNoInteractions()
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getUserById - when CacheNotInitialized & database and has no result - should initialize cache with not found then return null`() {
            // Given
            given(cacheRepository.getUser(userUUID = userId)).willReturn(CacheResult.CacheNotInitialized)
            given(databaseRepository.getUserById(userId = userId)).willReturn(null)

            // When
            val result = repository.getUserById(userId = userId.toString())

            // Then
            assertThat(result).isEqualTo(null)
            inOrder(cacheRepository, databaseRepository).also {
                then(cacheRepository).should(it).getUser(userUUID = userId)
                then(databaseRepository).should(it).getUserById(userId = userId)
                then(cacheRepository).should(it).insertUserNotFound(userUUID = userId)
                it.verifyNoMoreInteractions()
            }
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getUserById - when CacheNotInitialized & database and has result - should initialize cache with database then return mapped dto`() {
            // Given
            val userDTO = mock(UserDTO::class.java)
            given(cacheRepository.getUser(userUUID = userId)).willReturn(CacheResult.CacheNotInitialized)
            given(databaseRepository.getUserById(userId = userId)).willReturn(userDTO)

            // When
            val result = repository.getUserById(userId = userId.toString())

            // Then
            assertThat(result).isEqualTo(null)
            inOrder(cacheRepository, databaseRepository, mapper).also {
                then(cacheRepository).should(it).getUser(userUUID = userId)
                then(databaseRepository).should(it).getUserById(userId = userId)
                then(cacheRepository).should(it).insertUser(userDTO = userDTO)
                then(mapper).should(it).toDomain(userDTO)
                it.verifyNoMoreInteractions()
            }
        }

        @Test
        fun `getUserById - when cache is initialized - should return mapped result`() {
            // Given
            val userDTO = mock(UserDTO::class.java)
            given(cacheRepository.getUser(userId)).willReturn(CacheResult.CachedUser(userDTO))

            val userInfo = mock(UserInfo::class.java)
            given(mapper.toDomain(userDTO)).willReturn(userInfo)

            // When
            val result = repository.getUserById(userId = userId.toString())

            // Then
            assertThat(result).isEqualTo(userInfo)
            inOrder(cacheRepository, databaseRepository, mapper).also {
                then(cacheRepository).should(it).getUser(userId)
                then(mapper).should(it).toDomain(userDTO)
                it.verifyNoMoreInteractions()
            }
            then(databaseRepository).shouldHaveNoInteractions()
        }
    }

    @Nested
    inner class UpdateUserCases {

        private val userId = UUID.randomUUID()

        @Test
        fun `updateUser - when invalid user UUID - should return null without doing anything`() {
            // When
            val result = repository.updateUser(userId = "invalid userId", fcmToken = "fcmToken")

            // Then
            assertThat(result).isEqualTo(null)
            then(cacheRepository).shouldHaveNoInteractions()
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `updateUser - when cache is not initialized and has no result - should return null`() {
            // Given
            given(cacheRepository.getUser(userUUID = userId)).willReturn(CacheResult.CacheNotInitialized)
            given(databaseRepository.getUserById(userId = userId)).willReturn(null)

            // When
            val result = repository.updateUser(userId = userId.toString(), fcmToken = "fcmToken")

            // Then
            assertThat(result).isEqualTo(null)
            inOrder(cacheRepository, databaseRepository).also {
                then(cacheRepository).should(it).getUser(userUUID = userId)
                then(databaseRepository).should(it).getUserById(userId = userId)
                then(cacheRepository).should(it).insertUserNotFound(userUUID = userId)
                it.verifyNoMoreInteractions()
            }
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `updateUser - when cache user not found - should return null`() {
            // Given
            given(cacheRepository.getUser(userUUID = userId)).willReturn(CacheResult.CacheUserNotFound)

            // When
            val result = repository.updateUser(userId = userId.toString(), fcmToken = "fcmToken")

            // Then
            assertThat(result).isEqualTo(null)
            then(cacheRepository).should(only()).getUser(userUUID = userId)
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `updateUser - when cache returns DTO - should return updatedUser`() {
            // Given
            val userDTO = mock(UserDTO::class.java)
            given(cacheRepository.getUser(userUUID = userId)).willReturn(CacheResult.CachedUser(userDTO))

            val updatedUserDTO = mock(UserDTO::class.java)
            given(mapper.updateDto(dto = userDTO, fcmToken = "fcmToken")).willReturn(updatedUserDTO)

            val savedUserDTO = mock(UserDTO::class.java)
            given(databaseRepository.save(updatedUserDTO)).willReturn(savedUserDTO)

            val userInfo = mock(UserInfo::class.java)
            given(mapper.toDomain(savedUserDTO)).willReturn(userInfo)

            // When
            val result = repository.updateUser(userId = userId.toString(), fcmToken = "fcmToken")

            // Then
            assertThat(result).isEqualTo(userInfo)
            inOrder(cacheRepository, databaseRepository, mapper).also {
                then(cacheRepository).should(it).getUser(userUUID = userId)
                then(mapper).should(it).updateDto(dto = userDTO, fcmToken = "fcmToken")
                then(databaseRepository).should(it).save(updatedUserDTO)
                then(cacheRepository).should(it).updateUser(userDTO = savedUserDTO)
                then(mapper).should(it).toDomain(dto = savedUserDTO)
                it.verifyNoMoreInteractions()
            }
        }

    }

    @Test
    fun `generateUser - should generate dto and insert it in database and cache then return saved dto`() {
        // Given
        val userDTO = mock(UserDTO::class.java)
        given(mapper.generateDto(fcmToken = "fcmToken")).willReturn(userDTO)

        val savedUserDTO = mock(UserDTO::class.java)
        given(databaseRepository.save(userDTO)).willReturn(savedUserDTO)

        val userInfo = mock(UserInfo::class.java)
        given(mapper.toDomain(savedUserDTO)).willReturn(userInfo)

        // When
        val result = repository.generateUser(fcmToken = "fcmToken")

        // Then
        assertThat(result).isEqualTo(userInfo)
        inOrder(cacheRepository, databaseRepository, mapper).also { inOrder ->
            then(mapper).should(inOrder).generateDto(fcmToken = "fcmToken")
            then(databaseRepository).should(inOrder).save(userDTO)
            then(cacheRepository).should(inOrder).insertUser(savedUserDTO)
            then(mapper).should(inOrder).toDomain(savedUserDTO)
            inOrder.verifyNoMoreInteractions()
        }
    }

}

