package fr.social.gouv.agora.usecase.moderatus

import fr.social.gouv.agora.domain.LoginTokenData
import fr.social.gouv.agora.domain.UserInfo
import fr.social.gouv.agora.infrastructure.login.DecodeResult
import fr.social.gouv.agora.infrastructure.login.LoginTokenGenerator
import fr.social.gouv.agora.usecase.login.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class ModeratusLoginUseCaseTest {

    @Autowired
    private lateinit var useCase: ModeratusLoginUseCase

    @MockBean
    private lateinit var loginTokenGenerator: LoginTokenGenerator

    @MockBean
    private lateinit var userRepository: UserRepository

    @Test
    fun `login - when decode is failure - should return false`() {
        // Given
        given(loginTokenGenerator.decodeLoginToken(encryptedMessage = "loginToken"))
            .willReturn(DecodeResult.Failure)
        given(userRepository.getUserById(userId = "userId")).willReturn(null)

        // When
        val result = useCase.login(loginToken = "loginToken")

        // Then
        assertThat(result).isFalse
    }

    @Test
    fun `login - when decode success but userRepository returns null - should return false`() {
        // Given
        given(loginTokenGenerator.decodeLoginToken(encryptedMessage = "loginToken"))
            .willReturn(DecodeResult.Success(loginTokenData = LoginTokenData(userId = "userId")))
        given(userRepository.getUserById(userId = "userId")).willReturn(null)

        // When
        val result = useCase.login(loginToken = "loginToken")

        // Then
        assertThat(result).isFalse
    }

    @Test
    fun `login - when decode success and has response from userRepository - should return true`() {
        // Given
        given(loginTokenGenerator.decodeLoginToken(encryptedMessage = "loginToken"))
            .willReturn(DecodeResult.Success(loginTokenData = LoginTokenData(userId = "userId")))

        val userInfo = mock(UserInfo::class.java)
        given(userRepository.getUserById(userId = "userId")).willReturn(userInfo)

        // When
        val result = useCase.login(loginToken = "loginToken")

        // Then
        assertThat(result).isTrue
    }

}