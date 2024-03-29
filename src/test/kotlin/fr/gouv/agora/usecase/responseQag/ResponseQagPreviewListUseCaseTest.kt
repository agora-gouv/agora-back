package fr.gouv.agora.usecase.responseQag

import fr.gouv.agora.domain.IncomingResponsePreview
import fr.gouv.agora.domain.ResponseQagPreview
import fr.gouv.agora.domain.ResponseQagText
import fr.gouv.agora.domain.Thematique
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
    private lateinit var mapper: ResponseQagPreviewListMapper

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
    }

    @Test
    fun `getResponseQagPreviewList - when has qags without matching thematique - should return 2x emptyList and put it to cache`() {
        // Given
        given(cacheRepository.getResponseQagPreviewList()).willReturn(null)
        val qagWithSupportCount = mock(QagInfoWithSupportCount::class.java).also {
            given(it.thematiqueId).willReturn("unknownThematiqueId")
        }
        given(qagInfoRepository.getQagSelectedWithoutResponsesWithSupportCount()).willReturn(listOf(qagWithSupportCount))
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn("qagId")
            given(it.thematiqueId).willReturn("unknownThematiqueId")
        }
        given(qagInfoRepository.getQagWithResponses()).willReturn(listOf(qagInfo))

        val thematique = mock(Thematique::class.java).also {
            given(it.id).willReturn("thematiqueId")
        }
        given(thematiqueRepository.getThematiqueList()).willReturn(listOf(thematique))

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
        then(responseQagRepository).should(only()).getResponsesQag(listOf("qagId"))
        then(thematiqueRepository).should(only()).getThematiqueList()
    }

    @Test
    fun `getResponseQagPreviewList - when has qags without responses - should return mapped incomingResponse`() {
        // Given
        given(cacheRepository.getResponseQagPreviewList()).willReturn(null)
        val qag = mock(QagInfoWithSupportCount::class.java).also {
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(qagInfoRepository.getQagSelectedWithoutResponsesWithSupportCount()).willReturn(listOf(qag))
        given(qagInfoRepository.getQagWithResponses()).willReturn(emptyList())

        val thematique = mock(Thematique::class.java).also {
            given(it.id).willReturn("thematiqueId")
        }
        given(thematiqueRepository.getThematiqueList()).willReturn(listOf(thematique))

        val incomingResponsePreview = mock(IncomingResponsePreview::class.java)
        given(mapper.toIncomingResponsePreview(qag, thematique)).willReturn(incomingResponsePreview)

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

        val responseQag = mock(ResponseQagText::class.java).also {
            given(it.qagId).willReturn("qagId")
        }
        given(responseQagRepository.getResponsesQag(listOf("qagId"))).willReturn(listOf(responseQag))

        val responseQagPreview = mock(ResponseQagPreview::class.java)
        given(mapper.toResponseQagPreview(qagInfo, thematique, responseQag)).willReturn(responseQagPreview)

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
    }

}