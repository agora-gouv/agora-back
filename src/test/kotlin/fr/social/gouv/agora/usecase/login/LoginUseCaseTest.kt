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
    fun `login - when repository returns user and fcmToken are the same - should return user`() {
        // Given
        val userInfo = mock(UserInfo::class.java).also {
            given(it.fcmToken).willReturn("fcmToken")
        }
        given(repository.getUserById(userId = "userId")).willReturn(userInfo)

        // When
        val result = useCase.login(
            loginTokenData = LoginTokenData(
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
            loginTokenData = LoginTokenData(
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
}