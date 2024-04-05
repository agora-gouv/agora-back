package fr.gouv.agora.usecase.responseQag

import fr.gouv.agora.domain.ResponseQag
import fr.gouv.agora.domain.ResponseQagVideo
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.gouv.agora.usecase.qag.repository.QagInfo
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDate
import java.time.Month

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class ResponseQagPreviewOrderMapperTest {

    @Autowired
    private lateinit var mapper: ResponseQagPreviewOrderMapper

    companion object {
        @JvmStatic
        fun buildOrderTestCases() = arrayOf(
            input(
                testName = "when no lowPriorityQags and 1 incomingResponse only - should return incomingResponse with order 0",
                lowPriorityQagIds = emptyList(),
                incomingResponses = listOf(
                    BuildOrderTestInput(
                        qagId = "qagId",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 1),
                        expectedOrder = 0,
                    ),
                ),
                responses = emptyList(),
            ),
            input(
                testName = "when no lowPriorityQags and incomingResponses only - should return incomingResponses ordered by date DESC",
                lowPriorityQagIds = emptyList(),
                incomingResponses = listOf(
                    BuildOrderTestInput(
                        qagId = "qagId1",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 1),
                        expectedOrder = 1,
                    ),
                    BuildOrderTestInput(
                        qagId = "qagId0",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 2),
                        expectedOrder = 0,
                    ),
                ),
                responses = emptyList(),
            ),
            input(
                testName = "when no lowPriorityQags and 1 response only - should return response with order 0",
                lowPriorityQagIds = emptyList(),
                incomingResponses = emptyList(),
                responses = listOf(
                    BuildOrderTestInput(
                        qagId = "qagId",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 1),
                        expectedOrder = 0,
                    ),
                ),
            ),
            input(
                testName = "when no lowPriorityQags and response only - should return response ordered by responseDate DESC",
                lowPriorityQagIds = emptyList(),
                incomingResponses = emptyList(),
                responses = listOf(
                    BuildOrderTestInput(
                        qagId = "qagId1",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 1),
                        expectedOrder = 1,
                    ),
                    BuildOrderTestInput(
                        qagId = "qagId0",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 2),
                        expectedOrder = 0,
                    ),
                ),
            ),
            input(
                testName = "when only lowPriorityQags and 1 incomingResponse only - should return incomingResponse with order 0",
                lowPriorityQagIds = listOf("qagId"),
                incomingResponses = listOf(
                    BuildOrderTestInput(
                        qagId = "qagId",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 1),
                        expectedOrder = 0,
                    ),
                ),
                responses = emptyList(),
            ),
            input(
                testName = "when only lowPriorityQags and incomingResponses only - should return incomingResponses ordered by date DESC",
                lowPriorityQagIds = listOf("qagId0", "qagId1"),
                incomingResponses = listOf(
                    BuildOrderTestInput(
                        qagId = "qagId1",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 1),
                        expectedOrder = 1,
                    ),
                    BuildOrderTestInput(
                        qagId = "qagId0",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 2),
                        expectedOrder = 0,
                    ),
                ),
                responses = emptyList(),
            ),
            input(
                testName = "when only lowPriorityQags and 1 response only - should return response with order 0",
                lowPriorityQagIds = listOf("qagId"),
                incomingResponses = emptyList(),
                responses = listOf(
                    BuildOrderTestInput(
                        qagId = "qagId",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 1),
                        expectedOrder = 0,
                    ),
                ),
            ),
            input(
                testName = "when only lowPriorityQags and responses only - should return responses ordered by date DESC",
                lowPriorityQagIds = listOf("qagId0", "qagId1"),
                incomingResponses = emptyList(),
                responses = listOf(
                    BuildOrderTestInput(
                        qagId = "qagId1",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 1),
                        expectedOrder = 1,
                    ),
                    BuildOrderTestInput(
                        qagId = "qagId0",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 2),
                        expectedOrder = 0,
                    ),
                ),
            ),
            input(
                testName = "when no lowPriorityQags and has incomingResponse and response - should return incomingResponse and response ordered by responseDate DESC",
                lowPriorityQagIds = emptyList(),
                incomingResponses = listOf(
                    BuildOrderTestInput(
                        qagId = "qagId1",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 1),
                        expectedOrder = 1,
                    ),
                ),
                responses = listOf(
                    BuildOrderTestInput(
                        qagId = "qagId0",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 2),
                        expectedOrder = 0,
                    ),
                ),
            ),
            input(
                testName = "when has some lowPriorityQags and incomingResponses only - should return incomingResponses with normal priority first then low priority, all ordered by date DESC",
                lowPriorityQagIds = listOf("qagId2"),
                incomingResponses = listOf(
                    BuildOrderTestInput(
                        qagId = "qagId2",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 10),
                        expectedOrder = 2,
                    ),
                    BuildOrderTestInput(
                        qagId = "qagId1",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 1),
                        expectedOrder = 1,
                    ),
                    BuildOrderTestInput(
                        qagId = "qagId0",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 2),
                        expectedOrder = 0,
                    ),
                ),
                responses = emptyList(),
            ),
            input(
                testName = "when has some lowPriorityQags and responses only - should return responses with normal priority first then low priority, all ordered by date DESC",
                lowPriorityQagIds = listOf("qagId2"),
                incomingResponses = emptyList(),
                responses = listOf(
                    BuildOrderTestInput(
                        qagId = "qagId2",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 10),
                        expectedOrder = 2,
                    ),
                    BuildOrderTestInput(
                        qagId = "qagId1",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 1),
                        expectedOrder = 1,
                    ),
                    BuildOrderTestInput(
                        qagId = "qagId0",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 2),
                        expectedOrder = 0,
                    ),
                ),
            ),
            input(
                testName = "when has some lowPriorityQags, some incomingResponses and responses - should return ordered by normal priority first then low priority, all ordered by date DESC",
                lowPriorityQagIds = listOf("qagId2", "qagId3"),
                incomingResponses = listOf(
                    BuildOrderTestInput(
                        qagId = "qagId2",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 10),
                        expectedOrder = 2,
                    ),
                    BuildOrderTestInput(
                        qagId = "qagId1",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 1),
                        expectedOrder = 1,
                    )
                ),
                responses = listOf(
                    BuildOrderTestInput(
                        qagId = "qagId0",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 2),
                        expectedOrder = 0,
                    ),
                    BuildOrderTestInput(
                        qagId = "qagId3",
                        qagOrResponseData = LocalDate.of(2024, Month.JANUARY, 2),
                        expectedOrder = 3,
                    ),
                ),
            ),
        )

        private fun input(
            testName: String,
            lowPriorityQagIds: List<String>,
            incomingResponses: List<BuildOrderTestInput>,
            responses: List<BuildOrderTestInput>,
        ) = arrayOf(testName, lowPriorityQagIds, incomingResponses, responses)
    }

    @ParameterizedTest(name = "buildOrderResult - {0}")
    @MethodSource("buildOrderTestCases")
    fun `buildOrderResult - should return expected`(
        testName: String,
        lowPriorityQagIds: List<String>,
        incomingResponses: List<BuildOrderTestInput>,
        responses: List<BuildOrderTestInput>,
    ) {
        // Given
        val incomingResponsesData = incomingResponses.map(::mockIncomingResponse)
        val responsesData = responses.map(::mockResponse)

        // When
        val result = mapper.buildOrderResult(
            lowPriorityQagIds = lowPriorityQagIds,
            incomingResponses = incomingResponsesData.map { it.qagWithSupportCount },
            responses = responsesData.map { it.qagInfo to it.responseQag },
        )

        // Then
        assertThat(result).isEqualTo(
            ResponseQagPreviewOrderResult(
                incomingResponses = incomingResponsesData.sortedBy { it.expectedOrder }.map {
                    QagWithSupportCountAndOrder(
                        qagWithSupportCount = it.qagWithSupportCount,
                        order = it.expectedOrder,
                    )
                },
                responses = responsesData.sortedBy { it.expectedOrder }.map {
                    QagWithResponseAndOrder(
                        qagInfo = it.qagInfo,
                        response = it.responseQag,
                        order = it.expectedOrder,
                    )
                }
            )
        )
    }

    private fun mockIncomingResponse(testInput: BuildOrderTestInput): IncomingResponseMockData {
        val qagWithSupportCount = mock(QagInfoWithSupportCount::class.java).also {
            given(it.id).willReturn(testInput.qagId)
            given(it.date).willReturn(testInput.qagOrResponseData.toDate())
        }

        return IncomingResponseMockData(
            qagWithSupportCount = qagWithSupportCount,
            expectedOrder = testInput.expectedOrder,
        )
    }

    private fun mockResponse(testInput: BuildOrderTestInput): ResponseMockData {
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn(testInput.qagId)
        }

        val responseQag = mock(ResponseQagVideo::class.java).also {
            given(it.responseDate).willReturn(testInput.qagOrResponseData.toDate())
        }

        return ResponseMockData(
            qagInfo = qagInfo,
            responseQag = responseQag,
            expectedOrder = testInput.expectedOrder,
        )
    }
}

private data class IncomingResponseMockData(
    val qagWithSupportCount: QagInfoWithSupportCount,
    val expectedOrder: Int,
)

private data class ResponseMockData(
    val qagInfo: QagInfo,
    val responseQag: ResponseQag,
    val expectedOrder: Int,
)

internal data class BuildOrderTestInput(
    val qagId: String,
    val qagOrResponseData: LocalDate,
    val expectedOrder: Int,
)
