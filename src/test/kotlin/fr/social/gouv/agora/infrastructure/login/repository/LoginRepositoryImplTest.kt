package fr.social.gouv.agora.infrastructure.login.repository

import fr.social.gouv.agora.domain.UserInfo
import fr.social.gouv.agora.infrastructure.login.dto.UserDTO
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
internal class LoginRepositoryImplTest {

    @Autowired
    private lateinit var repository: LoginRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: LoginDatabaseRepository

    @MockBean
    private lateinit var cacheRepository: LoginCacheRepository

    @MockBean
    private lateinit var mapper: UserInfoMapper

    @Test
    fun `loginOrRegister - when cache return dto - should return mapped dto`() {
        // Given
        val userDTO = mock(UserDTO::class.java)
        given(cacheRepository.getUser(deviceId = "deviceId")).willReturn(userDTO)
        val userInfo = mock(UserInfo::class.java)
        given(mapper.toDomain(userDTO)).willReturn(userInfo)

        // When
        val result = repository.loginOrRegister(deviceId = "deviceId", fcmToken = "fcmToken")

        // Then
        assertThat(result).isEqualTo(userInfo)
        then(cacheRepository).should(only()).getUser(deviceId = "deviceId")
        then(databaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `loginOrRegister - when cache return null & database return dto - should insert dto in cache and return mapped dto`() {
        // Given
        given(cacheRepository.getUser(deviceId = "deviceId")).willReturn(null)

        val userDTO = mock(UserDTO::class.java)
        given(databaseRepository.getUser(deviceId = "deviceId")).willReturn(userDTO)

        val userInfo = mock(UserInfo::class.java)
        given(mapper.toDomain(userDTO)).willReturn(userInfo)

        // When
        val result = repository.loginOrRegister(deviceId = "deviceId", fcmToken = "fcmToken")

        // Then
        assertThat(result).isEqualTo(userInfo)
        then(cacheRepository).should(times(1)).getUser(deviceId = "deviceId")
        then(cacheRepository).should(times(1)).insertUser(userDTO)
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(databaseRepository).should(only()).getUser(deviceId = "deviceId")
    }

    @Test
    fun `loginOrRegister - when cache return null, database return null & ID does not exist in database - should generate dto and insert it in cache and database then return mapped dto`() {
        // Given
        given(cacheRepository.getUser(deviceId = "deviceId")).willReturn(null)
        given(databaseRepository.getUser(deviceId = "deviceId")).willReturn(null)

        val newUserUuid = UUID.randomUUID()
        val userDTO = mock(UserDTO::class.java).also {
            given(it.id).willReturn(newUserUuid)
        }
        given(mapper.generateDto(deviceId = "deviceId", fcmToken = "fcmToken")).willReturn(userDTO)
        given(databaseRepository.existsById(newUserUuid)).willReturn(false)

        val userInfo = mock(UserInfo::class.java)
        given(mapper.toDomain(userDTO)).willReturn(userInfo)

        // When
        val result = repository.loginOrRegister(deviceId = "deviceId", fcmToken = "fcmToken")

        // Then
        assertThat(result).isEqualTo(userInfo)
        inOrder(cacheRepository, databaseRepository, mapper).also { inOrder ->
            then(cacheRepository).should(inOrder).getUser(deviceId = "deviceId")
            then(databaseRepository).should(inOrder).getUser(deviceId = "deviceId")
            then(mapper).should(inOrder).generateDto(deviceId = "deviceId", fcmToken = "fcmToken")
            then(databaseRepository).should(inOrder).existsById(newUserUuid)
            then(cacheRepository).should(inOrder).insertUser(userDTO)
            then(databaseRepository).should(inOrder).save(userDTO)
            then(mapper).should(inOrder).toDomain(userDTO)
            inOrder.verifyNoMoreInteractions()
        }
    }

    @Test
    fun `loginOrRegister - when cache return null, database return null & first ID exist in database - should generate 2 dtos and insert the second one in cache and database then return mapped dto`() {
        // Given
        given(cacheRepository.getUser(deviceId = "deviceId")).willReturn(null)
        given(databaseRepository.getUser(deviceId = "deviceId")).willReturn(null)

        val newUserUuid1 = UUID.randomUUID()
        val userDTO1 = mock(UserDTO::class.java).also {
            given(it.id).willReturn(newUserUuid1)
        }
        val newUserUuid2 = UUID.randomUUID()
        val userDTO2 = mock(UserDTO::class.java).also {
            given(it.id).willReturn(newUserUuid2)
        }
        given(mapper.generateDto(deviceId = "deviceId", fcmToken = "fcmToken")).willReturn(userDTO1, userDTO2)
        given(databaseRepository.existsById(newUserUuid1)).willReturn(true)
        given(databaseRepository.existsById(newUserUuid2)).willReturn(false)

        val userInfo = mock(UserInfo::class.java)
        given(mapper.toDomain(userDTO2)).willReturn(userInfo)

        // When
        val result = repository.loginOrRegister(deviceId = "deviceId", fcmToken = "fcmToken")

        // Then
        assertThat(result).isEqualTo(userInfo)
        inOrder(cacheRepository, databaseRepository, mapper).also { inOrder ->
            then(cacheRepository).should(inOrder).getUser(deviceId = "deviceId")
            then(databaseRepository).should(inOrder).getUser(deviceId = "deviceId")
            then(mapper).should(inOrder).generateDto(deviceId = "deviceId", fcmToken = "fcmToken")
            then(databaseRepository).should(inOrder).existsById(newUserUuid1)
            then(mapper).should(inOrder).generateDto(deviceId = "deviceId", fcmToken = "fcmToken")
            then(databaseRepository).should(inOrder).existsById(newUserUuid2)
            then(cacheRepository).should(inOrder).insertUser(userDTO2)
            then(databaseRepository).should(inOrder).save(userDTO2)
            then(mapper).should(inOrder).toDomain(userDTO2)
            inOrder.verifyNoMoreInteractions()
        }
    }

    @Test
    fun `loginOrRegister - when cache return null, database return null & 10 generated ID exist in database - should generate return null without inserting user`() {
        // Given
        given(cacheRepository.getUser(deviceId = "deviceId")).willReturn(null)
        given(databaseRepository.getUser(deviceId = "deviceId")).willReturn(null)

        val newUserUuid = UUID.randomUUID()
        val userDTO = mock(UserDTO::class.java).also {
            given(it.id).willReturn(newUserUuid)
        }
        given(mapper.generateDto(deviceId = "deviceId", fcmToken = "fcmToken")).willReturn(userDTO)
        given(databaseRepository.existsById(newUserUuid)).willReturn(true)

        // When
        val result = repository.loginOrRegister(deviceId = "deviceId", fcmToken = "fcmToken")

        // Then
        assertThat(result).isNull()
        then(cacheRepository).should(times(1)).getUser(deviceId = "deviceId")
        then(databaseRepository).should(times(1)).getUser(deviceId = "deviceId")
        then(mapper).should(times(10)).generateDto(deviceId = "deviceId", fcmToken = "fcmToken")
        then(databaseRepository).should(times(10)).existsById(newUserUuid)
        then(databaseRepository).shouldHaveNoMoreInteractions()
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(mapper).shouldHaveNoMoreInteractions()
    }

}