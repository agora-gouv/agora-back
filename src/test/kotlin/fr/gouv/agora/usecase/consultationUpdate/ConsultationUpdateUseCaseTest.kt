package fr.gouv.agora.usecase.consultationUpdate

import fr.gouv.agora.TestUtils
import fr.gouv.agora.domain.ConsultationUpdate
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
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

    companion object {

        private val consultationUpdate = mock(ConsultationUpdate::class.java)

        @JvmStatic
        fun getConsultationUpdateTestCases() = arrayOf(
            input(
                testCaseName = "when currentDate is before start date - should return null",
                currentDate = LocalDateTime.of(1998, Month.JULY, 12, 20, 20),
                startDate = LocalDateTime.of(2000, Month.JANUARY, 1, 6, 30),
                endDate = LocalDateTime.of(2001, Month.FEBRUARY, 2, 23, 30),
                expectedOngoingUpdate = null,
                expectedFinishedUpdate = null,
                expectedFinalResult = null,
            ),
            input(
                testCaseName = "when currentDate is between start and end date but has no update - should return null",
                currentDate = LocalDateTime.of(2000, Month.JANUARY, 1, 12, 30),
                startDate = LocalDateTime.of(2000, Month.JANUARY, 1, 6, 30),
                endDate = LocalDateTime.of(2001, Month.FEBRUARY, 2, 23, 30),
                expectedOngoingUpdate = null,
                expectedFinishedUpdate = null,
                expectedFinalResult = null,
            ),
            input(
                testCaseName = "when currentDate is between start and end date, and has ongoing update - should return it",
                currentDate = LocalDateTime.of(2000, Month.JANUARY, 1, 12, 30),
                startDate = LocalDateTime.of(2000, Month.JANUARY, 1, 6, 30),
                endDate = LocalDateTime.of(2001, Month.FEBRUARY, 2, 23, 30),
                expectedOngoingUpdate = consultationUpdate,
                expectedFinishedUpdate = null,
                expectedFinalResult = consultationUpdate,
            ),
            input(
                testCaseName = "when currentDate is exactly start date, and has ongoing update - should return it",
                currentDate = LocalDateTime.of(2000, Month.JANUARY, 1, 6, 30),
                startDate = LocalDateTime.of(2000, Month.JANUARY, 1, 6, 30),
                endDate = LocalDateTime.of(2001, Month.FEBRUARY, 2, 23, 30),
                expectedOngoingUpdate = consultationUpdate,
                expectedFinishedUpdate = null,
                expectedFinalResult = consultationUpdate,
            ),
            input(
                testCaseName = "when currentDate is after end date but has no update - should return null",
                currentDate = LocalDateTime.of(2005, Month.APRIL, 9, 21, 15),
                startDate = LocalDateTime.of(2000, Month.JANUARY, 1, 6, 30),
                endDate = LocalDateTime.of(2001, Month.FEBRUARY, 2, 23, 30),
                expectedOngoingUpdate = null,
                expectedFinishedUpdate = null,
                expectedFinalResult = null,
            ),
            input(
                testCaseName = "when currentDate is after end date, and has finished update - should return it",
                currentDate = LocalDateTime.of(2005, Month.APRIL, 9, 21, 15),
                startDate = LocalDateTime.of(2000, Month.JANUARY, 1, 6, 30),
                endDate = LocalDateTime.of(2001, Month.FEBRUARY, 2, 23, 30),
                expectedOngoingUpdate = null,
                expectedFinishedUpdate = consultationUpdate,
                expectedFinalResult = consultationUpdate,
            ),
            input(
                testCaseName = "when currentDate is exactly end date, and has finished update - should return it",
                currentDate = LocalDateTime.of(2001, Month.FEBRUARY, 2, 23, 30),
                startDate = LocalDateTime.of(2000, Month.JANUARY, 1, 6, 30),
                endDate = LocalDateTime.of(2001, Month.FEBRUARY, 2, 23, 30),
                expectedOngoingUpdate = null,
                expectedFinishedUpdate = consultationUpdate,
                expectedFinalResult = consultationUpdate,
            ),
        )

        private fun input(
            testCaseName: String,
            currentDate: LocalDateTime,
            startDate: LocalDateTime,
            endDate: LocalDateTime,
            expectedOngoingUpdate: ConsultationUpdate?,
            expectedFinishedUpdate: ConsultationUpdate?,
            expectedFinalResult: ConsultationUpdate?,
        ) = arrayOf(
            testCaseName,
            currentDate,
            startDate,
            endDate,
            expectedOngoingUpdate,
            expectedFinishedUpdate,
            expectedFinalResult,
        )
    }

    @ParameterizedTest(name = "getConsultationUpdate consultationInfo - {0}")
    @MethodSource("getConsultationUpdateTestCases")
    fun `getConsultationUpdate - when consultationInfo - should return result depending on date`(
        testCaseName: String,
        currentDate: LocalDateTime,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        expectedOngoingUpdate: ConsultationUpdate?,
        expectedFinishedUpdate: ConsultationUpdate?,
        expectedFinalResult: ConsultationUpdate?,
    ) {
        // Given
        mockCurrentDate(currentDate)
        expectedOngoingUpdate?.let {
            given(repository.getOngoingConsultationUpdate(consultationId = "consultationId")).willReturn(it)
        }
        expectedFinishedUpdate?.let {
            given(repository.getFinishedConsultationUpdate(consultationId = "consultationId")).willReturn(it)
        }

        // When
        val result = useCase.getConsultationUpdate(mockConsultation(startDate = startDate, endDate = endDate))

        // Then
        assertThat(result).isEqualTo(expectedFinalResult)
        expectedOngoingUpdate?.let {
            then(repository).should(only()).getOngoingConsultationUpdate(consultationId = "consultationId")
        }
        expectedFinishedUpdate?.let {
            then(repository).should(only()).getFinishedConsultationUpdate(consultationId = "consultationId")
        }
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