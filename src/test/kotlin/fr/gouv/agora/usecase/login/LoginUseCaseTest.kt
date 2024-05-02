package fr.gouv.agora.usecase.login

import fr.gouv.agora.domain.LoginRequest
import fr.gouv.agora.domain.SignupRequest
import fr.gouv.agora.domain.UserInfo
import fr.gouv.agora.usecase.login.repository.UserDataRepository
import fr.gouv.agora.usecase.login.repository.UserRepository
import fr.gouv.agora.usecase.suspiciousUser.IsSuspiciousUserUseCase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class LoginUseCaseTest {

    @InjectMocks
    private lateinit var useCase: LoginUseCase

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var userDataRepository: UserDataRepository

    @Mock
    private lateinit var isSuspiciousUserUseCase: IsSuspiciousUserUseCase

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
        then(isSuspiciousUserUseCase).shouldHaveNoInteractions()
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
        then(isSuspiciousUserUseCase).shouldHaveNoInteractions()
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
        then(isSuspiciousUserUseCase).shouldHaveNoInteractions()
    }

    @Test
    fun `signUp - should add userData and return result from repository`() {
        // Given
        val signupRequest = mock(SignupRequest::class.java).also {
            given(it.ipAddressHash).willReturn("ipHash")
            given(it.userAgent).willReturn("userAgent")
        }
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
        then(isSuspiciousUserUseCase).should(only()).notifySignup(ipAddressHash = "ipHash", userAgent = "userAgent")
    }

}