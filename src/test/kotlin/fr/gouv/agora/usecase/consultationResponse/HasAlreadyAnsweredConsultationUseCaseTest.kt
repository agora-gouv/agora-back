package fr.gouv.agora.usecase.consultationResponse

import fr.gouv.agora.usecase.consultationResponse.repository.UserAnsweredConsultationRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class HasAlreadyAnsweredConsultationUseCaseTest {

    private lateinit var useCase: HasAlreadyAnsweredConsultationUseCase

    @Mock
    private lateinit var userAnsweredConsultationRepository: UserAnsweredConsultationRepository

    @BeforeEach
    fun setUp() {
        useCase = HasAlreadyAnsweredConsultationUseCase(
            userAnsweredConsultationRepository = userAnsweredConsultationRepository,
        )
    }

    @Nested
    inner class `hasAlreadyAnswered` {

        @Test
        fun `hasAlreadyAnswered - when repository returns true - should return true`() {
            // Given
            given(
                userAnsweredConsultationRepository.hasAnsweredConsultation(
                    consultationId = "consultId",
                    userId = "userId",
                )
            ).willReturn(true)

            // When
            val result = useCase.hasAlreadyAnswered(consultationId = "consultId", userId = "userId")

            // Then
            assertThat(result).isTrue()
            then(userAnsweredConsultationRepository).should().hasAnsweredConsultation(
                consultationId = "consultId",
                userId = "userId",
            )
            then(userAnsweredConsultationRepository).shouldHaveNoMoreInteractions()
        }

        @Test
        fun `hasAlreadyAnswered - when repository returns false - should return false`() {
            // Given
            given(
                userAnsweredConsultationRepository.hasAnsweredConsultation(
                    consultationId = "consultId",
                    userId = "userId",
                )
            ).willReturn(false)

            // When
            val result = useCase.hasAlreadyAnswered(consultationId = "consultId", userId = "userId")

            // Then
            assertThat(result).isFalse()
            then(userAnsweredConsultationRepository).should().hasAnsweredConsultation(
                consultationId = "consultId",
                userId = "userId",
            )
            then(userAnsweredConsultationRepository).shouldHaveNoMoreInteractions()
        }
    }
}
