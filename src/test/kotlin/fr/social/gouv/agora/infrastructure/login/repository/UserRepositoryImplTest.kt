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
        fun `getAllUsers - when cache is not initialized - should initialize cache with database then return mapped results`() {
            // Given
            given(cacheRepository.getAllUserList()).willReturn(CacheResult.CacheNotInitialized)
            val userDTO = mock(UserDTO::class.java)
            given(databaseRepository.findAll()).willReturn(listOf(userDTO))

            val userInfo = mock(UserInfo::class.java)
            given(mapper.toDomain(userDTO)).willReturn(userInfo)

            // When
            val result = repository.getAllUsers()

            // Then
            assertThat(result).isEqualTo(listOf(userInfo))
            inOrder(cacheRepository, databaseRepository, mapper).also {
                then(cacheRepository).should(it).getAllUserList()
                then(databaseRepository).should(it).findAll()
                then(cacheRepository).should(it).initializeCache(listOf(userDTO))
                then(mapper).should(it).toDomain(userDTO)
                it.verifyNoMoreInteractions()
            }
        }

        @Test
        fun `getAllUsers - when cache is initialized - should return mapped result`() {
            // Given
            val userDTO = mock(UserDTO::class.java)
            val allUserDTO = listOf(userDTO)
            given(cacheRepository.getAllUserList()).willReturn(CacheResult.CachedUserList(allUserDTO))

            val userInfo = mock(UserInfo::class.java)
            given(mapper.toDomain(userDTO)).willReturn(userInfo)

            // When
            val result = repository.getAllUsers()

            // Then
            assertThat(result).isEqualTo(listOf(userInfo))
            inOrder(cacheRepository, databaseRepository, mapper).also {
                then(cacheRepository).should(it).getAllUserList()
                then(mapper).should(it).toDomain(userDTO)
                it.verifyNoMoreInteractions()
            }
            then(databaseRepository).shouldHaveNoInteractions()
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
        fun `getUserById - when CacheNotInitialized & database and has no result - should initialize cache with database then return null`() {
            // Given
            given(cacheRepository.getAllUserList()).willReturn(CacheResult.CacheNotInitialized)
            given(databaseRepository.findAll()).willReturn(emptyList())

            // When
            val result = repository.getUserById(userId = userId.toString())

            // Then
            assertThat(result).isEqualTo(null)
            inOrder(cacheRepository, databaseRepository).also {
                then(cacheRepository).should(it).getAllUserList()
                then(databaseRepository).should(it).findAll()
                then(cacheRepository).should(it).initializeCache(emptyList())
                it.verifyNoMoreInteractions()
            }
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getUserById - when cache is initialized and has result - should return mapped result`() {
            // Given
            val userDTO = mock(UserDTO::class.java).also {
                given(it.id).willReturn(userId)
            }
            val allUserDTO = listOf(userDTO)
            given(cacheRepository.getAllUserList()).willReturn(CacheResult.CachedUserList(allUserDTO))

            val userInfo = mock(UserInfo::class.java)
            given(mapper.toDomain(userDTO)).willReturn(userInfo)

            // When
            val result = repository.getUserById(userId = userId.toString())

            // Then
            assertThat(result).isEqualTo(userInfo)
            inOrder(cacheRepository, databaseRepository, mapper).also {
                then(cacheRepository).should(it).getAllUserList()
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
            given(cacheRepository.getAllUserList()).willReturn(CacheResult.CacheNotInitialized)
            given(databaseRepository.findAll()).willReturn(emptyList())

            // When
            val result = repository.updateUser(
                userId = userId.toString(),
                fcmToken = "fcmToken"
            )

            // Then
            assertThat(result).isEqualTo(null)
            inOrder(cacheRepository, databaseRepository).also {
                then(cacheRepository).should(it).getAllUserList()
                then(databaseRepository).should(it).findAll()
                then(cacheRepository).should(it).initializeCache(emptyList())
                it.verifyNoMoreInteractions()
            }
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `updateUser - when cache returns DTO - should return updatedUser`() {
            // Given
            val userDTO = mock(UserDTO::class.java).also {
                given(it.id).willReturn(userId)
            }
            given(cacheRepository.getAllUserList()).willReturn(CacheResult.CachedUserList(listOf(userDTO)))

            val updatedUserDTO = mock(UserDTO::class.java)
            given(mapper.updateDto(dto = userDTO, fcmToken = "fcmToken"))
                .willReturn(updatedUserDTO)

            val savedUserDTO = mock(UserDTO::class.java)
            given(databaseRepository.save(updatedUserDTO)).willReturn(savedUserDTO)

            val userInfo = mock(UserInfo::class.java)
            given(mapper.toDomain(savedUserDTO)).willReturn(userInfo)
            // When
            val result = repository.updateUser(
                userId = userId.toString(),
                fcmToken = "fcmToken",
            )

            // Then
            assertThat(result).isEqualTo(userInfo)
            then(databaseRepository).should(only()).save(updatedUserDTO)
            then(cacheRepository).should(times(1)).getAllUserList()
            then(cacheRepository).should(times(1)).updateUser(userDTO = savedUserDTO)
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(mapper).should(times(1)).updateDto(dto = userDTO, fcmToken = "fcmToken")
            then(mapper).should(times(1)).toDomain(dto = savedUserDTO)
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

