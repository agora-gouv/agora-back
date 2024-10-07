package fr.gouv.agora.usecase.qag

import fr.gouv.agora.TestUtils.getFixedClock
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.gouv.agora.usecase.qag.repository.QagInfo
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import org.mockito.BDDMockito.only
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.time.Month

@ExtendWith(MockitoExtension::class)
internal class GetAskQagStatusUseCaseTest {

    private lateinit var useCase: GetAskQagStatusUseCase

    @Mock
    private lateinit var qagInfoRepository: QagInfoRepository

    private val userId = "userId"

    companion object {
        @JvmStatic
        fun getAskQagStatusDateTestCases() = arrayOf(
            input(
                testWhenDescription = "qagPostDate < serverDate < resetDate, same week",
                qagPostDate = LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0),
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30, 0),
                // resetDate = January 1st 14h
                expectedStatus = AskQagStatus.WEEKLY_LIMIT_REACHED,
            ),
            input(
                testWhenDescription = "qagPostDate < resetDate < serverDate, same week",
                qagPostDate = LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0),
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 5, 12, 30, 0),
                // resetDate = January 1st 14h
                expectedStatus = AskQagStatus.ENABLED,
            ),
            input(
                testWhenDescription = "qagPostDate < serverDate < resetDate, across a week",
                qagPostDate = LocalDateTime.of(2024, Month.JANUARY, 1, 22, 0, 0),
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 8, 12, 30, 0),
                // resetDate = January 8th 14h
                expectedStatus = AskQagStatus.WEEKLY_LIMIT_REACHED,
            ),
            input(
                testWhenDescription = "qagPostDate < serverDate < resetDate, qagPostDate is 1 second before weekly reset and serverDate is at weekly reset",
                qagPostDate = LocalDateTime.of(2024, Month.JANUARY, 1, 13, 59, 59),
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 8, 14, 0, 0),
                // resetDate = January 8th 14h
                expectedStatus = AskQagStatus.ENABLED,
            ),
            input(
                testWhenDescription = "qagPostDate = serverDate < resetDate, qagPostDate at a random time",
                qagPostDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30, 0),
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30, 0),
                // resetDate = January 1st 14h
                expectedStatus = AskQagStatus.WEEKLY_LIMIT_REACHED,
            ),
            input(
                testWhenDescription = "qagPostDate = serverDate, qagPostDate is at weekly reset and serverDate is at weekly reset",
                qagPostDate = LocalDateTime.of(2024, Month.JANUARY, 1, 14, 0, 0),
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 1, 14, 0, 0),
                // resetDate = January 1st 14h
                expectedStatus = AskQagStatus.WEEKLY_LIMIT_REACHED,
            ),
            input(
                testWhenDescription = "qagPostDate > serverDate + 1 week",
                qagPostDate = LocalDateTime.of(2024, Month.JANUARY, 8, 14, 0, 1),
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 1, 14, 5, 0),
                // resetDate = January 1st 14h
                expectedStatus = AskQagStatus.ENABLED,
            ),
            input(
                testWhenDescription = "qagPostDate > serverDate but qagPostDate - serverDate < 1 week",
                qagPostDate = LocalDateTime.of(2024, Month.JANUARY, 2, 12, 0, 0),
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 1, 14, 0, 0),
                // resetDate = January 1st 14h
                expectedStatus = AskQagStatus.WEEKLY_LIMIT_REACHED,
            ),
        )

        private fun input(
            testWhenDescription: String,
            qagPostDate: LocalDateTime,
            serverDate: LocalDateTime,
            expectedStatus: AskQagStatus,
        ) = arrayOf(testWhenDescription, qagPostDate, serverDate, expectedStatus)

    }

    @ParameterizedTest(name = "getAskQagStatus - when {0} - should return {3}")
    @MethodSource("getAskQagStatusDateTestCases")
    fun `getAskQagStatus - should return expected`(
        @Suppress("UNUSED_PARAMETER")
        testWhenDescription: String,
        qagPostDate: LocalDateTime,
        serverDate: LocalDateTime,
        expectedStatus: AskQagStatus,
    ) {
        useCase = GetAskQagStatusUseCase(
            qagInfoRepository = qagInfoRepository,
            clock = getFixedClock(serverDate),
        )

        val qagInfo = mock(QagInfo::class.java).also {
            given(it.date).willReturn(qagPostDate.toDate())
        }
        given(qagInfoRepository.getUserLastQagInfo(userId = userId)).willReturn(qagInfo)

        // When
        val result = useCase.getAskQagStatus(userId)

        // Then
        assertThat(result).isEqualTo(expectedStatus)
        then(qagInfoRepository).should(only()).getUserLastQagInfo(userId = userId)
    }

}
