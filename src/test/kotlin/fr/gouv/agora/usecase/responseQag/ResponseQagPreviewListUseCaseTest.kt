package fr.gouv.agora.usecase.responseQag

import fr.gouv.agora.domain.IncomingResponsePreview
import fr.gouv.agora.domain.ResponseQagPreview
import fr.gouv.agora.domain.ResponseQagText
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.usecase.qag.repository.LowPriorityQagRepository
import fr.gouv.agora.usecase.qag.repository.QagInfo
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import fr.gouv.agora.usecase.responseQag.repository.ResponseQagPreviewCacheRepository
import fr.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class ResponseQagPreviewListUseCaseTest {

    @Autowired
    private lateinit var useCase: ResponseQagPreviewListUseCase

    @MockBean
    private lateinit var cacheRepository: ResponseQagPreviewCacheRepository

    @MockBean
    private lateinit var qagInfoRepository: QagInfoRepository

    @MockBean
    private lateinit var responseQagRepository: ResponseQagRepository

    @MockBean
    private lateinit var thematiqueRepository: ThematiqueRepository

    @MockBean
    private lateinit var lowPriorityQagRepository: LowPriorityQagRepository

    @MockBean
    private lateinit var mapper: ResponseQagPreviewListMapper

    @MockBean
    private lateinit var orderMapper: ResponseQagPreviewOrderMapper

    @Test
    fun `getResponseQagPreviewList - when has cache - should return cached response`() {
        // Given
        val responseQagPreviewList = mock(ResponseQagPreviewList::class.java)
        given(cacheRepository.getResponseQagPreviewList()).willReturn(responseQagPreviewList)

        // When
        val result = useCase.getResponseQagPreviewList()

        // Then
        assertThat(result).isEqualTo(responseQagPreviewList)
        then(cacheRepository).should(only()).getResponseQagPreviewList()
        then(qagInfoRepository).shouldHaveNoInteractions()
        then(responseQagRepository).shouldHaveNoInteractions()
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(lowPriorityQagRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getResponseQagPreviewList - when has no Qags - should return 2x emptyList and put it to cache`() {
        // Given
        given(cacheRepository.getResponseQagPreviewList()).willReturn(null)
        given(qagInfoRepository.getQagSelectedWithoutResponsesWithSupportCount()).willReturn(emptyList())
        given(qagInfoRepository.getQagWithResponses()).willReturn(emptyList())

        // When
        val result = useCase.getResponseQagPreviewList()

        // Then
        val expectedResponseQagPreviewList = ResponseQagPreviewList(
            incomingResponses = emptyList(),
            responses = emptyList(),
        )
        assertThat(result).isEqualTo(expectedResponseQagPreviewList)
        then(cacheRepository).should().getResponseQagPreviewList()
        then(cacheRepository).should().initResponseQagPreviewList(expectedResponseQagPreviewList)
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(qagInfoRepository).should().getQagSelectedWithoutResponsesWithSupportCount()
        then(qagInfoRepository).should().getQagWithResponses()
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(responseQagRepository).shouldHaveNoInteractions()
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(lowPriorityQagRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getResponseQagPreviewList - when has qags without matching thematique - should return 2x emptyList and put it to cache`() {
        // Given
        given(cacheRepository.getResponseQagPreviewList()).willReturn(null)
        val qagWithSupportCount = mock(QagInfoWithSupportCount::class.java).also {
            given(it.id).willReturn("qagId1")
            given(it.thematiqueId).willReturn("unknownThematiqueId")
        }
        given(qagInfoRepository.getQagSelectedWithoutResponsesWithSupportCount()).willReturn(listOf(qagWithSupportCount))
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn("qagId2")
            given(it.thematiqueId).willReturn("unknownThematiqueId")
        }
        given(qagInfoRepository.getQagWithResponses()).willReturn(listOf(qagInfo))
        given(responseQagRepository.getResponsesQag(listOf("qagId2"))).willReturn(emptyList())

        val thematique = mock(Thematique::class.java).also {
            given(it.id).willReturn("thematiqueId")
        }
        given(thematiqueRepository.getThematiqueList()).willReturn(listOf(thematique))

        given(lowPriorityQagRepository.getLowPriorityQagIds(listOf("qagId1", "qagId2")))
            .willReturn(listOf("qagIdWithLowPriority"))
        val qagWithOrder = mock(QagWithSupportCountAndOrder::class.java).also {
            given(it.qagWithSupportCount).willReturn(qagWithSupportCount)
        }
        given(
            orderMapper.buildOrderResult(
                lowPriorityQagIds = listOf("qagIdWithLowPriority"),
                incomingResponses = listOf(qagWithSupportCount),
                responses = emptyList(),
            )
        ).willReturn(ResponseQagPreviewOrderResult(incomingResponses = listOf(qagWithOrder), responses = emptyList()))

        // When
        val result = useCase.getResponseQagPreviewList()

        // Then
        val expectedResponseQagPreviewList = ResponseQagPreviewList(
            incomingResponses = emptyList(),
            responses = emptyList(),
        )
        assertThat(result).isEqualTo(expectedResponseQagPreviewList)
        then(cacheRepository).should().getResponseQagPreviewList()
        then(cacheRepository).should().initResponseQagPreviewList(expectedResponseQagPreviewList)
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(qagInfoRepository).should().getQagSelectedWithoutResponsesWithSupportCount()
        then(qagInfoRepository).should().getQagWithResponses()
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(responseQagRepository).should(only()).getResponsesQag(listOf("qagId2"))
        then(thematiqueRepository).should(only()).getThematiqueList()
        then(lowPriorityQagRepository).should(only()).getLowPriorityQagIds(listOf("qagId1", "qagId2"))
    }

    @Test
    fun `getResponseQagPreviewList - when has qags without responses - should return mapped incomingResponse`() {
        // Given
        given(cacheRepository.getResponseQagPreviewList()).willReturn(null)
        val qag = mock(QagInfoWithSupportCount::class.java).also {
            given(it.id).willReturn("qagId")
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(qagInfoRepository.getQagSelectedWithoutResponsesWithSupportCount()).willReturn(listOf(qag))
        given(qagInfoRepository.getQagWithResponses()).willReturn(emptyList())

        val thematique = mock(Thematique::class.java).also {
            given(it.id).willReturn("thematiqueId")
        }
        given(thematiqueRepository.getThematiqueList()).willReturn(listOf(thematique))

        given(lowPriorityQagRepository.getLowPriorityQagIds(listOf("qagId"))).willReturn(listOf("qagIdWithLowPriority"))
        val qagWithOrder = mock(QagWithSupportCountAndOrder::class.java).also {
            given(it.qagWithSupportCount).willReturn(qag)
        }
        given(
            orderMapper.buildOrderResult(
                lowPriorityQagIds = listOf("qagIdWithLowPriority"),
                incomingResponses = listOf(qag),
                responses = emptyList(),
            )
        ).willReturn(ResponseQagPreviewOrderResult(incomingResponses = listOf(qagWithOrder), responses = emptyList()))

        val incomingResponsePreview = mock(IncomingResponsePreview::class.java)
        given(mapper.toIncomingResponsePreview(qagWithOrder, thematique)).willReturn(incomingResponsePreview)

        // When
        val result = useCase.getResponseQagPreviewList()

        // Then
        val expectedResponseQagPreviewList = ResponseQagPreviewList(
            incomingResponses = listOf(incomingResponsePreview),
            responses = emptyList(),
        )
        assertThat(result).isEqualTo(expectedResponseQagPreviewList)
        then(cacheRepository).should().getResponseQagPreviewList()
        then(cacheRepository).should().initResponseQagPreviewList(expectedResponseQagPreviewList)
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(qagInfoRepository).should().getQagSelectedWithoutResponsesWithSupportCount()
        then(qagInfoRepository).should().getQagWithResponses()
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(responseQagRepository).shouldHaveNoInteractions()
        then(thematiqueRepository).should(only()).getThematiqueList()
        then(lowPriorityQagRepository).should(only()).getLowPriorityQagIds(listOf("qagId"))
    }

    @Test
    fun `getResponseQagPreviewList - when has qags with responses - should return mapped response`() {
        // Given
        given(cacheRepository.getResponseQagPreviewList()).willReturn(null)
        given(qagInfoRepository.getQagSelectedWithoutResponsesWithSupportCount()).willReturn(emptyList())
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn("qagId")
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(qagInfoRepository.getQagWithResponses()).willReturn(listOf(qagInfo))

        val thematique = mock(Thematique::class.java).also {
            given(it.id).willReturn("thematiqueId")
        }
        given(thematiqueRepository.getThematiqueList()).willReturn(listOf(thematique))

        val response = mock(ResponseQagText::class.java).also {
            given(it.qagId).willReturn("qagId")
        }
        given(responseQagRepository.getResponsesQag(listOf("qagId"))).willReturn(listOf(response))

        given(lowPriorityQagRepository.getLowPriorityQagIds(listOf("qagId"))).willReturn(listOf("qagIdWithLowPriority"))
        val qagWithOrder = mock(QagWithResponseAndOrder::class.java).also {
            given(it.qagInfo).willReturn(qagInfo)
        }
        given(
            orderMapper.buildOrderResult(
                lowPriorityQagIds = listOf("qagIdWithLowPriority"),
                incomingResponses = emptyList(),
                responses = listOf(qagInfo to response),
            )
        ).willReturn(ResponseQagPreviewOrderResult(incomingResponses = emptyList(), responses = listOf(qagWithOrder)))

        val responseQagPreview = mock(ResponseQagPreview::class.java)
        given(mapper.toResponseQagPreview(qagWithOrder, thematique)).willReturn(responseQagPreview)

        // When
        val result = useCase.getResponseQagPreviewList()

        // Then
        val expectedResponseQagPreviewList = ResponseQagPreviewList(
            incomingResponses = emptyList(),
            responses = listOf(responseQagPreview),
        )
        assertThat(result).isEqualTo(expectedResponseQagPreviewList)
        then(cacheRepository).should().getResponseQagPreviewList()
        then(cacheRepository).should().initResponseQagPreviewList(expectedResponseQagPreviewList)
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(qagInfoRepository).should().getQagSelectedWithoutResponsesWithSupportCount()
        then(qagInfoRepository).should().getQagWithResponses()
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(responseQagRepository).should(only()).getResponsesQag(listOf("qagId"))
        then(thematiqueRepository).should(only()).getThematiqueList()
        then(lowPriorityQagRepository).should(only()).getLowPriorityQagIds(listOf("qagId"))
    }

}