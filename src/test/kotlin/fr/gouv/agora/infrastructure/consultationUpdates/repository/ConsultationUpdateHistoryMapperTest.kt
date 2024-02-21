package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.TestUtils
import fr.gouv.agora.domain.ConsultationUpdateHistory
import fr.gouv.agora.domain.ConsultationUpdateHistoryStatus
import fr.gouv.agora.domain.ConsultationUpdateHistoryType
import fr.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateHistoryWithDateDTO
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime
import java.time.Month

@ExtendWith(SpringExtension::class)
@SpringBootTest
class ConsultationUpdateHistoryMapperTest {

    private lateinit var mapper: ConsultationUpdateHistoryMapper

    companion object {
        @JvmStatic
        fun toDomainCases() = arrayOf(
            input(
                testName = "when has a history item before serverDate but no other step further - should return status CURRENT",
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 2, 12, 30),
                historyItems = listOf(
                    HistoryMapperTestInput(
                        stepNumber = 1,
                        updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                        expectedStatus = ConsultationUpdateHistoryStatus.CURRENT,
                    ),
                )
            ),
            input(
                testName = "when has two history items with date same stepNumber - should return only first item",
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 2, 12, 30),
                historyItems = listOf(
                    HistoryMapperTestInput(
                        stepNumber = 1,
                        updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                        expectedStatus = ConsultationUpdateHistoryStatus.CURRENT,
                    ),
                    HistoryMapperTestInput(
                        stepNumber = 1,
                        updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 14, 0),
                        expectedStatus = null,
                    ),
                )
            ),
        )

        private fun input(
            testName: String,
            serverDate: LocalDateTime,
            historyItems: List<HistoryMapperTestInput>,
        ) = arrayOf(testName, serverDate, historyItems)

    }

    // When type is update should return type update
    // When type is response should return type response

    @ParameterizedTest(name = "toDomain - {0}")
    @MethodSource("toDomainCases")
    fun `toDomain - should return expected`(
        testName: String,
        serverDate: LocalDateTime,
        historyItems: List<HistoryMapperTestInput>,
    ) {
        // Given
        mockCurrentDate(serverDate)
        val historyDTOs = historyItems.map { dto ->
            mock(ConsultationUpdateHistoryWithDateDTO::class.java).also {
                given(it.stepNumber).willReturn(dto.stepNumber)
                given(it.updateDate).willReturn(dto.updateDate?.toDate())
                given(it.type).willReturn("update")
                given(it.consultationUpdateId).willReturn(null)
                given(it.title).willReturn("title")
                given(it.actionText).willReturn("actionText")
            }
        }

        // When
        val result = mapper.toDomain(historyDTOs)

        // Then
        val expectedResults = historyItems.mapNotNull { historyItem ->
            historyItem.expectedStatus?.let {
                ConsultationUpdateHistory(
                    stepNumber = historyItem.stepNumber,
                    type = ConsultationUpdateHistoryType.UPDATE,
                    consultationUpdateId = null,
                    status = historyItem.expectedStatus,
                    title = "title",
                    updateDate = historyItem.updateDate?.toDate(),
                    actionText = "actionText",
                )
            }
        }.reversed()
        assertThat(result).isEqualTo(expectedResults)
    }

    private fun mockCurrentDate(currentDate: LocalDateTime) {
        mapper = ConsultationUpdateHistoryMapper(
            clock = TestUtils.getFixedClock(currentDate),
        )
    }

}

data class HistoryMapperTestInput(
    val stepNumber: Int,
    val updateDate: LocalDateTime?,
    val expectedStatus: ConsultationUpdateHistoryStatus?,
)