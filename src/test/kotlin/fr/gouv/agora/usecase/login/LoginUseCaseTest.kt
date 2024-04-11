package fr.gouv.agora.usecase.login

import fr.gouv.agora.domain.LoginRequest
import fr.gouv.agora.domain.SignupRequest
import fr.gouv.agora.domain.UserInfo
import fr.gouv.agora.infrastructure.utils.UuidUtils
import fr.gouv.agora.usecase.login.repository.UserDataRepository
import fr.gouv.agora.usecase.login.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
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
    private lateinit var userRepository: UserRepository

    @MockBean
    private lateinit var userDataRepository: UserDataRepository

    private val loginRequest = mock(LoginRequest::class.java).also {
        given(it.userId).willReturn("userId")
        given(it.ipAddressHash).willReturn("ipAddressHash")
    }

    @Test
    fun `findUser - should return result from Repository`() {
        // Given
        val userInfo = mock(UserInfo::class.java)
        given(userRepository.getUserById(userId = "userId")).willReturn(userInfo)

        // When
        val result = useCase.findUser(userId = "userId")

        // Then
        assertThat(result).isEqualTo(userInfo)
        then(userRepository).should(only()).getUserById(userId = "userId")
        then(userDataRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `login - when repository does not return user - should return null`() {
        // Given
        given(userRepository.getUserById(userId = "userId")).willReturn(null)

        // When
        val result = useCase.login(loginRequest = loginRequest)

        // Then
        assertThat(result).isEqualTo(null)
        then(userRepository).should(only()).getUserById(userId = "userId")
        then(userDataRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `login - when repository returns user - should return updated user and add userData`() {
        // Given
        val userInfo = mock(UserInfo::class.java)
        given(userRepository.getUserById(userId = "userId")).willReturn(userInfo)

        val updatedUserInfo = mock(UserInfo::class.java)
        given(userRepository.updateUser(loginRequest = loginRequest)).willReturn(updatedUserInfo)

        // When
        val result = useCase.login(loginRequest = loginRequest)

        // Then
        assertThat(result).isEqualTo(updatedUserInfo)
        then(userRepository).should().getUserById(userId = "userId")
        then(userRepository).should().updateUser(loginRequest = loginRequest)
        then(userRepository).shouldHaveNoMoreInteractions()
        then(userDataRepository).should(only()).addUserData(loginRequest = loginRequest)
    }

    @Test
    fun `signUp - should add userData and return result from repository`() {
        // Given
        val signupRequest = mock(SignupRequest::class.java)
        val userInfo = mock(UserInfo::class.java).also {
            given(it.userId).willReturn("userId")
        }
        given(userRepository.generateUser(signupRequest = signupRequest)).willReturn(userInfo)

        // When
        val result = useCase.signUp(signupRequest = signupRequest)

        // Then
        assertThat(result).isEqualTo(userInfo)
        then(userRepository).should(only()).generateUser(signupRequest = signupRequest)
        then(userDataRepository).should(only()).addUserData(signupRequest = signupRequest, generatedUserId = "userId")
    }

}