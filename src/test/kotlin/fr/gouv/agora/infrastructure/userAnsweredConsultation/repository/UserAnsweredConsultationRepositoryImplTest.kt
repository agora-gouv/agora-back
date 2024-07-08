package fr.gouv.agora.infrastructure.userAnsweredConsultation.repository

import fr.gouv.agora.domain.UserAnsweredConsultation
import fr.gouv.agora.infrastructure.userAnsweredConsultation.dto.UserAnsweredConsultationDTO
import fr.gouv.agora.usecase.consultationResponse.repository.UserAnsweredConsultationResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class UserAnsweredConsultationRepositoryImplTest {

    @InjectMocks
    private lateinit var repository: UserAnsweredConsultationRepositoryImpl

    @Mock
    private lateinit var databaseRepository: UserAnsweredConsultationDatabaseRepository

    @Mock
    private lateinit var mapper: UserAnsweredConsultationMapper

    @Nested
    inner class HasAnsweredConsultationTestCases {

        @Test
        fun `hasAnsweredConsultation - when invalid user UUID - should return false`() {
            // When
            val result = repository.hasAnsweredConsultation(
                consultationId = UUID.randomUUID().toString(),
                userId = "invalid useId UUID",
            )

            // Then
            assertThat(result).isEqualTo(false)
            then(databaseRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `hasAnsweredConsultation - when database has no answeredConsultation for userId - should return false`() {
            // Given
            val consultationId = UUID.randomUUID().toString()
            val userUUID = UUID.randomUUID()
            given(
                databaseRepository.hasAnsweredConsultation(
                    consultationId = consultationId,
                    userId = userUUID
                )
            )
                .willReturn(0)

            // When
            val result = repository.hasAnsweredConsultation(
                consultationId = consultationId,
                userId = userUUID.toString(),
            )

            // Then
            assertThat(result).isEqualTo(false)
            then(databaseRepository).should(only())
                .hasAnsweredConsultation(consultationId = consultationId, userId = userUUID)
        }

        @Test
        fun `hasAnsweredConsultation - when database has answeredConsultation for userId - should return true`() {
            // Given
            val consultationId = UUID.randomUUID().toString()
            val userUUID = UUID.randomUUID()
            given(databaseRepository.hasAnsweredConsultation(consultationId = consultationId, userId = userUUID))
                .willReturn(1)

            // When
            val result = repository.hasAnsweredConsultation(
                consultationId = consultationId,
                userId = userUUID.toString(),
            )

            // Then
            assertThat(result).isEqualTo(true)
            then(databaseRepository).should(only())
                .hasAnsweredConsultation(consultationId = consultationId, userId = userUUID)
        }
    }

    @Nested
    inner class HasAnsweredConsultationsTestCases {
        @Test
        fun `hasAnsweredConsultations - when invalid user UUID - should return emptyMap`() {
            // Given
            val consultationId = UUID.randomUUID()

            // When
            val result = repository.hasAnsweredConsultations(
                consultationIds = listOf(consultationId.toString()),
                userId = "Invalid user UUID",
            )

            // Then
            assertThat(result).isEqualTo(emptyMap<String, Boolean>())
            then(databaseRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `hasAnsweredConsultations - when valid user UUID and has only valid consultation UUIDs - should return result from database`() {
            // Given
            val userId = UUID.randomUUID()
            val consultationId = UUID.randomUUID().toString()
            given(
                databaseRepository.getAnsweredConsultations(
                    consultationIds = listOf(consultationId),
                    userId = userId
                )
            )
                .willReturn(listOf(consultationId))

            // When
            val result = repository.hasAnsweredConsultations(
                consultationIds = listOf(consultationId),
                userId = userId.toString(),
            )

            // Then
            assertThat(result).isEqualTo(mapOf(consultationId to true))
            then(databaseRepository).should(only())
                .getAnsweredConsultations(consultationIds = listOf(consultationId), userId = userId)
        }
    }

    @Nested
    inner class GetUsersAnsweredConsultationTestCases {

        @Test
        fun `getUsersAnsweredConsultation - when valid consultation UUID - should return result from database`() {
            // Given
            val consultationId = UUID.randomUUID().toString()
            val userId = UUID.randomUUID()
            given(databaseRepository.getUsersAnsweredConsultation(consultationId)).willReturn(listOf(userId))

            // When
            val result = repository.getUsersAnsweredConsultation(consultationId = consultationId)

            // Then
            assertThat(result).isEqualTo(listOf(userId.toString()))
            then(databaseRepository).should(only()).getUsersAnsweredConsultation(consultationId = consultationId)
        }
    }

    @Nested
    inner class InsertUserAnsweredConsultationTestCases {
        @Test
        fun `insertUserAnsweredConsultation - when mapper returns null - should return Failure`() {
            //Given
            val userAnsweredConsultation = mock(UserAnsweredConsultation::class.java)
            given(mapper.toDto(userAnsweredConsultation)).willReturn(null)

            //When
            val result = repository.insertUserAnsweredConsultation(userAnsweredConsultation)

            //Then
            assertThat(result).isEqualTo(UserAnsweredConsultationResult.FAILURE)
            then(databaseRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `insertUserAnsweredConsultation - when mapper returns DTO - should return Success`() {
            //Given
            val userAnsweredConsultation = mock(UserAnsweredConsultation::class.java)
            val userAnsweredConsultationDTO = mock(UserAnsweredConsultationDTO::class.java)
            given(mapper.toDto(userAnsweredConsultation)).willReturn(userAnsweredConsultationDTO)

            //When
            val result = repository.insertUserAnsweredConsultation(userAnsweredConsultation)

            //Then
            assertThat(result).isEqualTo(UserAnsweredConsultationResult.SUCCESS)
            then(databaseRepository).should(only()).save(userAnsweredConsultationDTO)
        }
    }
}
