package fr.gouv.agora.usecase.consultationResponse

import fr.gouv.agora.usecase.consultationResponse.repository.ConsultationResponseRateLimitRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class CheckConsultationResponseRateLimitUseCaseTest {

    @InjectMocks
    private lateinit var useCase: CheckConsultationResponseRateLimitUseCase

    @Mock
    private lateinit var rateLimitRepository: ConsultationResponseRateLimitRepository

    @Nested
    inner class `isRateLimited - when IP is not rate limited` {

        @Test
        fun `should return false`() {
            // Given
            given(rateLimitRepository.isIpRateLimited("ipHash123")).willReturn(false)

            // When
            val result = useCase.isRateLimited("ipHash123")

            // Then
            assertThat(result).isFalse()
        }

        @Test
        fun `should increment the IP counter`() {
            // Given
            given(rateLimitRepository.isIpRateLimited("ipHash123")).willReturn(false)

            // When
            useCase.isRateLimited("ipHash123")

            // Then
            then(rateLimitRepository).should().incrementIpCount("ipHash123")
        }
    }

    @Nested
    inner class `isRateLimited - when IP is rate limited` {

        @Test
        fun `should return true`() {
            // Given
            given(rateLimitRepository.isIpRateLimited("ipHash123")).willReturn(true)

            // When
            val result = useCase.isRateLimited("ipHash123")

            // Then
            assertThat(result).isTrue()
        }

        @Test
        fun `should not increment the IP counter`() {
            // Given
            given(rateLimitRepository.isIpRateLimited("ipHash123")).willReturn(true)

            // When
            useCase.isRateLimited("ipHash123")

            // Then
            then(rateLimitRepository).should().isIpRateLimited("ipHash123")
            then(rateLimitRepository).shouldHaveNoMoreInteractions()
        }
    }
}
