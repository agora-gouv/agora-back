package fr.gouv.agora.usecase.moderatus

import fr.gouv.agora.domain.LoginTokenData
import fr.gouv.agora.domain.UserAuthorization
import fr.gouv.agora.domain.UserInfo
import fr.gouv.agora.infrastructure.login.DecodeResult
import fr.gouv.agora.infrastructure.login.LoginTokenGenerator
import fr.gouv.agora.usecase.login.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class ModeratusLoginUseCaseTest {

    @InjectMocks
    private lateinit var useCase: ModeratusLoginUseCase

    @Mock
    private lateinit var loginTokenGenerator: LoginTokenGenerator

    @Mock
    private lateinit var userRepository: UserRepository

    @Test
    fun `login - when decode is failure - should return Failure`() {
        // Given
        given(loginTokenGenerator.decodeLoginToken(encryptedMessage = "loginToken"))
            .willReturn(DecodeResult.Failure)

        // When
        val result = useCase.login(loginToken = "loginToken")

        // Then
        assertThat(result).isEqualTo(ModeratusLoginResult.Failure)
    }

    @Test
    fun `login - when decode success but userRepository returns null - should return Failure`() {
        // Given
        given(loginTokenGenerator.decodeLoginToken(encryptedMessage = "loginToken"))
            .willReturn(DecodeResult.Success(loginTokenData = LoginTokenData(userId = "userId")))
        given(userRepository.getUserById(userId = "userId")).willReturn(null)

        // When
        val result = useCase.login(loginToken = "loginToken")

        // Then
        assertThat(result).isEqualTo(ModeratusLoginResult.Failure)
    }

    @Test
    fun `login - when decode success and has response from userRepository but no authorization MODERATE_QAG - should return Failure`() {
        // Given
        given(loginTokenGenerator.decodeLoginToken(encryptedMessage = "loginToken"))
            .willReturn(DecodeResult.Success(loginTokenData = LoginTokenData(userId = "userId")))

        val userInfo = mock(UserInfo::class.java).also {
            given(it.authorizationList).willReturn(emptyList())
        }
        given(userRepository.getUserById(userId = "userId")).willReturn(userInfo)

        // When
        val result = useCase.login(loginToken = "loginToken")

        // Then
        assertThat(result).isEqualTo(ModeratusLoginResult.Failure)
    }

    @Test
    fun `login - when decode success and has response from userRepository and has authorization MODERATE_QAG - should return Success with userId`() {
        // Given
        given(loginTokenGenerator.decodeLoginToken(encryptedMessage = "loginToken"))
            .willReturn(DecodeResult.Success(loginTokenData = LoginTokenData(userId = "userId")))

        val userInfo = mock(UserInfo::class.java).also {
            given(it.authorizationList).willReturn(listOf(UserAuthorization.MODERATE_QAG))
            given(it.userId).willReturn("userId")
        }
        given(userRepository.getUserById(userId = "userId")).willReturn(userInfo)

        // When
        val result = useCase.login(loginToken = "loginToken")

        // Then
        assertThat(result).isEqualTo(ModeratusLoginResult.Success(userId = "userId"))
    }

}