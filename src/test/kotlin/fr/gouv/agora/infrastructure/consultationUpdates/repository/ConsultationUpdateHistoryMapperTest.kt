package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.TestUtils
import fr.gouv.agora.TestUtils.lenientGiven
import fr.gouv.agora.TestUtils.willReturn
import fr.gouv.agora.domain.ConsultationUpdateHistory
import fr.gouv.agora.domain.ConsultationUpdateHistoryStatus
import fr.gouv.agora.domain.ConsultationUpdateHistoryType
import fr.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateHistoryWithDateDTO
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.time.Month
import java.util.*

@ExtendWith(MockitoExtension::class)
class ConsultationUpdateHistoryMapperTest {

    private lateinit var mapper: ConsultationUpdateHistoryMapper

    companion object {
        @JvmStatic
        fun toDomainCases() = arrayOf(
            input(
                testName = "when has a history item with null updateDate - should return status INCOMING",
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                historyItems = listOf(
                    HistoryMapperTestInput(
                        stepNumber = 1,
                        updateDate = null,
                        expectedStatus = ConsultationUpdateHistoryStatus.INCOMING,
                    ),
                )
            ),
            input(
                testName = "when has a history item with status INCOMING and actionText - should return null actionText",
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                historyItems = listOf(
                    HistoryMapperTestInput(
                        stepNumber = 1,
                        updateDate = null,
                        actionText = "actionText",
                        expectedStatus = ConsultationUpdateHistoryStatus.INCOMING,
                        expectedActionText = null,
                    ),
                )
            ),
            input(
                testName = "when has a history item updateDate is after serverDate - should return status INCOMING",
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                historyItems = listOf(
                    HistoryMapperTestInput(
                        stepNumber = 1,
                        updateDate = LocalDateTime.of(2024, Month.JANUARY, 6, 21, 30),
                        expectedStatus = ConsultationUpdateHistoryStatus.INCOMING,
                    ),
                )
            ),
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
                testName = "when has a history item with CURRENT status and actionText - should return same actionText",
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 2, 12, 30),
                historyItems = listOf(
                    HistoryMapperTestInput(
                        stepNumber = 1,
                        updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                        actionText = "actionText",
                        expectedStatus = ConsultationUpdateHistoryStatus.CURRENT,
                        expectedActionText = "actionText",
                    ),
                )
            ),
            input(
                testName = "when has 2 history items before serverDate - should return first step as DONE and last step as CURRENT",
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 2, 12, 30),
                historyItems = listOf(
                    HistoryMapperTestInput(
                        stepNumber = 1,
                        updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                        expectedStatus = ConsultationUpdateHistoryStatus.DONE,
                    ),
                    HistoryMapperTestInput(
                        stepNumber = 2,
                        updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 18, 45),
                        expectedStatus = ConsultationUpdateHistoryStatus.CURRENT,
                    ),
                )
            ),
            input(
                testName = "when has 2 history items before serverDate with any step order - should return first step as DONE and last step as CURRENT",
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 2, 12, 30),
                historyItems = listOf(
                    HistoryMapperTestInput(
                        stepNumber = 2,
                        updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 18, 45),
                        expectedStatus = ConsultationUpdateHistoryStatus.CURRENT,
                    ),
                    HistoryMapperTestInput(
                        stepNumber = 1,
                        updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                        actionText = "actionText",
                        expectedStatus = ConsultationUpdateHistoryStatus.DONE,
                        expectedActionText = "actionText",
                    ),
                )
            ),
            input(
                testName = "when has two history items with date before serverDate and same stepNumber - should return the first one ordered by date DESC",
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 2, 12, 30),
                historyItems = listOf(
                    HistoryMapperTestInput(
                        stepNumber = 1,
                        updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30),
                        expectedStatus = null,
                    ),
                    HistoryMapperTestInput(
                        stepNumber = 1,
                        updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 14, 0),
                        expectedStatus = ConsultationUpdateHistoryStatus.CURRENT,
                    ),
                )
            ),
            input(
                testName = "when has two history items one with null date and the other with date before serverDate and same stepNumber - should return item without null date",
                serverDate = LocalDateTime.of(2024, Month.JANUARY, 2, 12, 30),
                historyItems = listOf(
                    HistoryMapperTestInput(
                        stepNumber = 1,
                        updateDate = null,
                        expectedStatus = null,
                    ),
                    HistoryMapperTestInput(
                        stepNumber = 1,
                        updateDate = LocalDateTime.of(2024, Month.JANUARY, 1, 14, 0),
                        expectedStatus = ConsultationUpdateHistoryStatus.CURRENT,
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

    @Test
    fun `toDomain - when type is UPDATE - should type UPDATE`() {
        // Given
        mockCurrentDate(LocalDateTime.of(2024, Month.JANUARY, 1, 9, 30))
        val updateUUID = UUID.randomUUID()
        val dto = mock(ConsultationUpdateHistoryWithDateDTO::class.java).also {
            given(it.stepNumber).willReturn(1)
            given(it.consultationUpdateId).willReturn(updateUUID)
            given(it.type).willReturn("update")
            given(it.title).willReturn("title")
            given(it.updateDate).willReturn(Date(0))
            given(it.actionText).willReturn("actionText")
            given(it.slug).willReturn("slug")
        }

        // When
        val result = mapper.toDomain(listOf(dto))

        // Then
        assertThat(result).isEqualTo(
            listOf(
                ConsultationUpdateHistory(
                    type = ConsultationUpdateHistoryType.UPDATE,
                    consultationUpdateId = updateUUID.toString(),
                    status = ConsultationUpdateHistoryStatus.CURRENT,
                    title = "title",
                    updateDate = Date(0),
                    actionText = "actionText",
                    slug = "slug"
                ),
            )
        )
    }

    @Test
    fun `toDomain - when type is RESULTS - should type RESULTS`() {
        // Given
        mockCurrentDate(LocalDateTime.of(2024, Month.JANUARY, 1, 9, 30))
        val updateUUID = UUID.randomUUID()
        val dto = mock(ConsultationUpdateHistoryWithDateDTO::class.java).also {
            given(it.stepNumber).willReturn(1)
            given(it.consultationUpdateId).willReturn(updateUUID)
            given(it.type).willReturn("results")
            given(it.title).willReturn("title")
            given(it.updateDate).willReturn(Date(0))
            given(it.actionText).willReturn("actionText")
            given(it.slug).willReturn("slug")
        }

        // When
        val result = mapper.toDomain(listOf(dto))

        // Then
        assertThat(result).isEqualTo(
            listOf(
                ConsultationUpdateHistory(
                    type = ConsultationUpdateHistoryType.RESULTS,
                    consultationUpdateId = updateUUID.toString(),
                    status = ConsultationUpdateHistoryStatus.CURRENT,
                    title = "title",
                    updateDate = Date(0),
                    actionText = "actionText",
                    slug = "slug"
                ),
            )
        )
    }

    @ParameterizedTest(name = "toDomain - {0}")
    @MethodSource("toDomainCases")
    fun `toDomain - should return expected`(
        @Suppress("UNUSED_PARAMETER")
        testName: String,
        serverDate: LocalDateTime,
        historyItems: List<HistoryMapperTestInput>,
    ) {
        // Given
        mockCurrentDate(serverDate)
        val historyDTOs = historyItems.map { historyItem ->
            mock(ConsultationUpdateHistoryWithDateDTO::class.java).also {
                given(it.stepNumber).willReturn(historyItem.stepNumber)
                given(it.updateDate).willReturn(historyItem.updateDate?.toDate())
                lenientGiven(it.type).willReturn("update")
                lenientGiven(it.consultationUpdateId).willReturn(null)
                lenientGiven(it.title).willReturn("title")
                lenientGiven(it.actionText).willReturn(historyItem.actionText)
                lenientGiven(it.slug).willReturn("slug")
            }
        }

        // When
        val result = mapper.toDomain(historyDTOs)

        // Then
        val expectedResults = historyItems.sortedByDescending { it.updateDate }.mapNotNull { historyItem ->
            historyItem.expectedStatus?.let {
                ConsultationUpdateHistory(
                    type = ConsultationUpdateHistoryType.UPDATE,
                    consultationUpdateId = null,
                    status = historyItem.expectedStatus,
                    title = "title",
                    updateDate = historyItem.updateDate?.toDate(),
                    actionText = historyItem.expectedActionText,
                    slug = "slug"
                )
            }
        }
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
    val actionText: String? = null,
    val expectedStatus: ConsultationUpdateHistoryStatus?,
    val expectedActionText: String? = null,
)
