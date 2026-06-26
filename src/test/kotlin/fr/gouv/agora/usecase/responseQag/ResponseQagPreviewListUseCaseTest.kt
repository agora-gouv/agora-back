package fr.gouv.agora.usecase.responseQag

import fr.gouv.agora.domain.ResponseQagText
import fr.gouv.agora.usecase.qag.repository.LowPriorityQagRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import fr.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import java.util.Date

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
internal class ResponseQagPreviewListUseCaseTest {

    @InjectMocks
    private lateinit var useCase: ResponseQagPreviewListUseCase

    @Mock
    private lateinit var qagInfoRepository: QagInfoRepository

    @Mock
    private lateinit var responseQagRepository: ResponseQagRepository

    @Mock
    private lateinit var thematiqueRepository: ThematiqueRepository

    @Mock
    private lateinit var lowPriorityQagRepository: LowPriorityQagRepository

    @Mock
    private lateinit var mapper: ResponseQagPreviewListMapper

    @Mock
    private lateinit var orderMapper: ResponseQagPreviewOrderMapper

    @Nested
    inner class `getResponseQagPreviewList - minDate filtering` {

        @Test
        fun `getResponseQagPreviewList - when minDate is null - should not filter responses`() {
            // Given
            val qagSelected = mock(QagInfoWithSupportCount::class.java).also {
                given(it.id).willReturn("qagId")
            }
            given(qagInfoRepository.getQagsSelectedForResponse()).willReturn(listOf(qagSelected))

            val response = mock(ResponseQagText::class.java).also {
                given(it.qagId).willReturn("qagId")
                given(it.responseDate).willReturn(Date(500))
            }
            given(responseQagRepository.getResponsesQag(listOf("qagId"))).willReturn(listOf(response))

            given(lowPriorityQagRepository.getLowPriorityQagIds(listOf("qagId"))).willReturn(emptyList())
            val orderResult = ResponseQagPreviewOrderResult(incomingResponses = emptyList(), responses = emptyList())
            given(orderMapper.buildOrderResult(
                lowPriorityQagIds = emptyList(),
                incomingResponses = emptyList(),
                responses = listOf(Pair(qagSelected, response)),
            )).willReturn(orderResult)

            // When
            useCase.getResponseQagPreviewList(minDate = null)

            // Then - orderMapper is called with the response (not filtered out)
            then(orderMapper).should().buildOrderResult(
                lowPriorityQagIds = emptyList(),
                incomingResponses = emptyList(),
                responses = listOf(Pair(qagSelected, response)),
            )
        }

        @Test
        fun `getResponseQagPreviewList - when minDate is provided and response is before minDate - should filter out the response`() {
            // Given
            val minDate = Date(1_000)
            val qagSelected = mock(QagInfoWithSupportCount::class.java).also {
                given(it.id).willReturn("qagId")
            }
            given(qagInfoRepository.getQagsSelectedForResponse()).willReturn(listOf(qagSelected))

            // Response date is before minDate -> should be filtered out
            val response = mock(ResponseQagText::class.java).also {
                given(it.qagId).willReturn("qagId")
                given(it.responseDate).willReturn(Date(500))
            }
            given(responseQagRepository.getResponsesQag(listOf("qagId"))).willReturn(listOf(response))

            given(lowPriorityQagRepository.getLowPriorityQagIds(listOf("qagId"))).willReturn(emptyList())
            val orderResult = ResponseQagPreviewOrderResult(incomingResponses = emptyList(), responses = emptyList())
            given(orderMapper.buildOrderResult(
                lowPriorityQagIds = emptyList(),
                incomingResponses = listOf(qagSelected),
                responses = emptyList(),
            )).willReturn(orderResult)

            // When
            useCase.getResponseQagPreviewList(minDate = minDate)

            // Then - orderMapper is called without the response (filtered out), qag goes to incomingResponses
            then(orderMapper).should().buildOrderResult(
                lowPriorityQagIds = emptyList(),
                incomingResponses = listOf(qagSelected),
                responses = emptyList(),
            )
        }

        @Test
        fun `getResponseQagPreviewList - when minDate is provided and response is after minDate - should keep the response`() {
            // Given
            val minDate = Date(1_000)
            val qagSelected = mock(QagInfoWithSupportCount::class.java).also {
                given(it.id).willReturn("qagId")
            }
            given(qagInfoRepository.getQagsSelectedForResponse()).willReturn(listOf(qagSelected))

            // Response date is after minDate -> should be kept
            val response = mock(ResponseQagText::class.java).also {
                given(it.qagId).willReturn("qagId")
                given(it.responseDate).willReturn(Date(2_000))
            }
            given(responseQagRepository.getResponsesQag(listOf("qagId"))).willReturn(listOf(response))

            given(lowPriorityQagRepository.getLowPriorityQagIds(listOf("qagId"))).willReturn(emptyList())
            val orderResult = ResponseQagPreviewOrderResult(incomingResponses = emptyList(), responses = emptyList())
            given(orderMapper.buildOrderResult(
                lowPriorityQagIds = emptyList(),
                incomingResponses = emptyList(),
                responses = listOf(Pair(qagSelected, response)),
            )).willReturn(orderResult)

            // When
            useCase.getResponseQagPreviewList(minDate = minDate)

            // Then - orderMapper is called with the response (kept)
            then(orderMapper).should().buildOrderResult(
                lowPriorityQagIds = emptyList(),
                incomingResponses = emptyList(),
                responses = listOf(Pair(qagSelected, response)),
            )
        }

        @Test
        fun `getResponseQagPreviewList - when minDate is provided and response is exactly on minDate - should keep the response`() {
            // Given
            val minDate = Date(1_000)
            val qagSelected = mock(QagInfoWithSupportCount::class.java).also {
                given(it.id).willReturn("qagId")
            }
            given(qagInfoRepository.getQagsSelectedForResponse()).willReturn(listOf(qagSelected))

            // Response date equals minDate -> should be kept (>= minDate)
            val response = mock(ResponseQagText::class.java).also {
                given(it.qagId).willReturn("qagId")
                given(it.responseDate).willReturn(Date(1_000))
            }
            given(responseQagRepository.getResponsesQag(listOf("qagId"))).willReturn(listOf(response))

            given(lowPriorityQagRepository.getLowPriorityQagIds(listOf("qagId"))).willReturn(emptyList())
            val orderResult = ResponseQagPreviewOrderResult(incomingResponses = emptyList(), responses = emptyList())
            given(orderMapper.buildOrderResult(
                lowPriorityQagIds = emptyList(),
                incomingResponses = emptyList(),
                responses = listOf(Pair(qagSelected, response)),
            )).willReturn(orderResult)

            // When
            useCase.getResponseQagPreviewList(minDate = minDate)

            // Then - orderMapper is called with the response (kept, date == minDate)
            then(orderMapper).should().buildOrderResult(
                lowPriorityQagIds = emptyList(),
                incomingResponses = emptyList(),
                responses = listOf(Pair(qagSelected, response)),
            )
        }

        @Test
        fun `getResponseQagPreviewList - when no qagsSelectedForResponse - should return empty lists`() {
            // Given
            given(qagInfoRepository.getQagsSelectedForResponse()).willReturn(emptyList())
            given(responseQagRepository.getResponsesQag(emptyList())).willReturn(emptyList())

            // When
            val result = useCase.getResponseQagPreviewList(minDate = Date(1_000))

            // Then
            assertThat(result.responses).isEmpty()
            assertThat(result.incomingResponses).isEmpty()
        }
    }
}
