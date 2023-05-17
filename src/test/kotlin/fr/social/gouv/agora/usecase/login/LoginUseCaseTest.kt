package fr.social.gouv.agora.usecase.login

import fr.social.gouv.agora.domain.LoginTokenData
import fr.social.gouv.agora.domain.UserInfo
import fr.social.gouv.agora.usecase.login.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class LoginUseCaseTest {

    @Autowired
    private lateinit var useCase: LoginUseCase

    @MockBean
    private lateinit var repository: UserRepository

    @Test
    fun `findUser - should return result from Repository`() {
        // Given
        val userInfo = mock(UserInfo::class.java)
        given(repository.getUserById(userId = "userId")).willReturn(userInfo)

        // When
        val result = useCase.findUser(userId = "userId")

        // Then
        assertThat(result).isEqualTo(userInfo)
        then(repository).should(only()).getUserById(userId = "userId")
    }

    @Test
    fun `login - when deviceId is not equal to deviceId from loginTokenData - should return null`() {
        // When
        val result = useCase.login(
            deviceId = "deviceId",
            loginTokenData = LoginTokenData(
                deviceId = "notTheSameDeviceId",
                userId = "userId",
            ),
            fcmToken = "fcmToken",
        )

        // Then
        assertThat(result).isEqualTo(null)
        then(repository).shouldHaveNoInteractions()
    }

    @Test
    fun `login - when repository returns user and fcmToken are the same - should return user`() {
        // Given
        val userInfo = mock(UserInfo::class.java).also {
            given(it.fcmToken).willReturn("fcmToken")
        }
        given(repository.getUserById(userId = "userId")).willReturn(userInfo)

        // When
        val result = useCase.login(
            deviceId = "deviceId",
            loginTokenData = LoginTokenData(
                deviceId = "deviceId",
                userId = "userId",
            ),
            fcmToken = "fcmToken",
        )

        // Then
        assertThat(result).isEqualTo(userInfo)
        then(repository).should(only()).getUserById(userId = "userId")
    }

    @Test
    fun `login - when repository returns user and fcmToken are the different - should updateFcmToken then return user`() {
        // Given
        val userInfo = mock(UserInfo::class.java).also {
            given(it.fcmToken).willReturn("oldFcmToken")
        }
        given(repository.getUserById(userId = "userId")).willReturn(userInfo)

        val updatedUserInfo = mock(UserInfo::class.java)
        given(repository.updateUserFcmToken(userId = "userId", fcmToken = "fcmToken")).willReturn(updatedUserInfo)

        // When
        val result = useCase.login(
            deviceId = "deviceId",
            loginTokenData = LoginTokenData(
                deviceId = "deviceId",
                userId = "userId",
            ),
            fcmToken = "fcmToken",
        )

        // Then
        assertThat(result).isEqualTo(updatedUserInfo)
        then(repository).should().getUserById(userId = "userId")
        then(repository).should().updateUserFcmToken(userId = "userId", fcmToken = "fcmToken")
        then(repository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun `signUp - when user with deviceId exists - should return null`() {
        // Given
        val userInfo = mock(UserInfo::class.java)
        given(repository.getUserByDeviceId(deviceId = "deviceId")).willReturn(userInfo)

        // When
        val result = useCase.signUp(deviceId = "deviceId", fcmToken = "fcmToken")

        // Then
        assertThat(result).isEqualTo(null)
        then(repository).should(only()).getUserByDeviceId(deviceId = "deviceId")
    }

    @Test
    fun `signUp - when user with deviceId does not exists - should return result from generateUser`() {
        // Given
        given(repository.getUserByDeviceId(deviceId = "deviceId")).willReturn(null)
        val userInfo = mock(UserInfo::class.java)
        given(repository.generateUser(deviceId = "deviceId", fcmToken = "fcmToken")).willReturn(userInfo)

        // When
        val result = useCase.signUp(deviceId = "deviceId", fcmToken = "fcmToken")

        // Then
        assertThat(result).isEqualTo(userInfo)
        then(repository).should().getUserByDeviceId(deviceId = "deviceId")
        then(repository).should().generateUser(deviceId = "deviceId", fcmToken = "fcmToken")
        then(repository).shouldHaveNoMoreInteractions()
    }

}