package fr.social.gouv.agora.usecase.consultationUpdate

import fr.social.gouv.agora.TestUtils
import fr.social.gouv.agora.domain.ConsultationUpdate
import fr.social.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.social.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime
import java.time.Month

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class ConsultationUpdateUseCaseTest {

    private lateinit var useCase: ConsultationUpdateUseCase

    @MockBean
    private lateinit var repository: ConsultationUpdateRepository

    @Test
    fun `getConsultationUpdate - when currentDate is before start date - should return null`() {
        // Given
        mockCurrentDate(LocalDateTime.of(1998, Month.JULY, 12, 20, 20))
        given(repository.getOngoingConsultationUpdate(consultationId = "consultationId")).willReturn(null)

        // When
        val result = useCase.getConsultationUpdate(
            mockConsultation(
                startDate = LocalDateTime.of(2000, Month.JANUARY, 1, 6, 30),
                endDate = LocalDateTime.of(2001, Month.FEBRUARY, 2, 23, 30),
            )
        )

        // Then
        assertThat(result).isNull()
        then(repository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun `getConsultationUpdate - when currentDate is between start and end date but has no update - should return null`() {
        // Given
        mockCurrentDate(LocalDateTime.of(2000, Month.JANUARY, 1, 12, 30))
        given(repository.getOngoingConsultationUpdate(consultationId = "consultationId")).willReturn(null)

        // When
        val result = useCase.getConsultationUpdate(
            mockConsultation(
                startDate = LocalDateTime.of(2000, Month.JANUARY, 1, 6, 30),
                endDate = LocalDateTime.of(2001, Month.FEBRUARY, 2, 23, 30),
            )
        )

        // Then
        assertThat(result).isNull()
        then(repository).should(only()).getOngoingConsultationUpdate(consultationId = "consultationId")
    }

    @Test
    fun `getConsultationUpdate - when currentDate is between start and end date, and has ongoing update - should return it`() {
        // Given
        mockCurrentDate(LocalDateTime.of(2000, Month.JANUARY, 1, 12, 30))
        val consultationUpdate = mock(ConsultationUpdate::class.java)
        given(repository.getOngoingConsultationUpdate(consultationId = "consultationId")).willReturn(consultationUpdate)

        // When
        val result = useCase.getConsultationUpdate(
            mockConsultation(
                startDate = LocalDateTime.of(2000, Month.JANUARY, 1, 6, 30),
                endDate = LocalDateTime.of(2001, Month.FEBRUARY, 2, 23, 30),
            )
        )

        // Then
        assertThat(result).isEqualTo(consultationUpdate)
        then(repository).should(only()).getOngoingConsultationUpdate(consultationId = "consultationId")
    }

    @Test
    fun `getConsultationUpdate - when currentDate is exactly start date, and has ongoing update - should return it`() {
        // Given
        val startDate = LocalDateTime.of(2000, Month.JANUARY, 1, 6, 30)
        mockCurrentDate(startDate)
        val consultationUpdate = mock(ConsultationUpdate::class.java)
        given(repository.getOngoingConsultationUpdate(consultationId = "consultationId")).willReturn(consultationUpdate)

        // When
        val result = useCase.getConsultationUpdate(
            mockConsultation(
                startDate = startDate,
                endDate = LocalDateTime.of(2001, Month.FEBRUARY, 2, 23, 30),
            )
        )

        // Then
        assertThat(result).isEqualTo(consultationUpdate)
        then(repository).should(only()).getOngoingConsultationUpdate(consultationId = "consultationId")
    }

    @Test
    fun `getConsultationUpdate - when currentDate is after end date but has no update - should return null`() {
        // Given
        mockCurrentDate(LocalDateTime.of(2005, Month.APRIL, 9, 21, 15))
        given(repository.getFinishedConsultationUpdate(consultationId = "consultationId")).willReturn(null)

        // When
        val result = useCase.getConsultationUpdate(
            mockConsultation(
                startDate = LocalDateTime.of(2000, Month.JANUARY, 1, 6, 30),
                endDate = LocalDateTime.of(2001, Month.FEBRUARY, 2, 23, 30),
            )
        )

        // Then
        assertThat(result).isNull()
        then(repository).should(only()).getFinishedConsultationUpdate(consultationId = "consultationId")
    }

    @Test
    fun `getConsultationUpdate - when currentDate is after end date, and has finished update - should return it`() {
        // Given
        mockCurrentDate(LocalDateTime.of(2005, Month.APRIL, 9, 21, 15))
        val consultationUpdate = mock(ConsultationUpdate::class.java)
        given(repository.getFinishedConsultationUpdate(consultationId = "consultationId")).willReturn(consultationUpdate)

        // When
        val result = useCase.getConsultationUpdate(
            mockConsultation(
                startDate = LocalDateTime.of(2000, Month.JANUARY, 1, 6, 30),
                endDate = LocalDateTime.of(2001, Month.FEBRUARY, 2, 23, 30),
            )
        )

        // Then
        assertThat(result).isEqualTo(consultationUpdate)
        then(repository).should(only()).getFinishedConsultationUpdate(consultationId = "consultationId")
    }

    @Test
    fun `getConsultationUpdate - when currentDate is exactly end date, and has finished update - should return it`() {
        // Given
        val endDate = LocalDateTime.of(2001, Month.FEBRUARY, 2, 23, 30)
        mockCurrentDate(endDate)
        val consultationUpdate = mock(ConsultationUpdate::class.java)
        given(repository.getFinishedConsultationUpdate(consultationId = "consultationId")).willReturn(consultationUpdate)

        // When
        val result = useCase.getConsultationUpdate(
            mockConsultation(
                startDate = LocalDateTime.of(2000, Month.JANUARY, 1, 6, 30),
                endDate = endDate,
            )
        )

        // Then
        assertThat(result).isEqualTo(consultationUpdate)
        then(repository).should(only()).getFinishedConsultationUpdate(consultationId = "consultationId")
    }

    private fun mockCurrentDate(currentDate: LocalDateTime) {
        useCase = ConsultationUpdateUseCase(
            repository = repository,
            clock = TestUtils.getFixedClock(currentDate),
        )
    }

    private fun mockConsultation(startDate: LocalDateTime, endDate: LocalDateTime): ConsultationInfo {
        return mock(ConsultationInfo::class.java).also {
            given(it.id).willReturn("consultationId")
            given(it.startDate).willReturn(startDate.toDate())
            given(it.endDate).willReturn(endDate.toDate())
        }
    }

}