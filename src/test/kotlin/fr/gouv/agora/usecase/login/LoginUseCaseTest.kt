package fr.gouv.agora.usecase.login

import fr.gouv.agora.domain.LoginRequest
import fr.gouv.agora.domain.UserInfo
import fr.gouv.agora.usecase.login.repository.UserRepository
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

    private val loginRequest = mock(LoginRequest::class.java).also {
        given(it.userId).willReturn("userId")
    }

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
    fun `login - when repository returns user - should return updated user`() {
        // Given
        val userInfo = mock(UserInfo::class.java)
        given(repository.getUserById(userId = "userId")).willReturn(userInfo)

        val updatedUserInfo = mock(UserInfo::class.java)
        given(repository.updateUser(loginRequest = loginRequest)).willReturn(updatedUserInfo)

        // When
        val result = useCase.login(loginRequest = loginRequest)

        // Then
        assertThat(result).isEqualTo(updatedUserInfo)
        then(repository).should().getUserById(userId = "userId")
        then(repository).should().updateUser(loginRequest = loginRequest)
        then(repository).shouldHaveNoMoreInteractions()
    }
}