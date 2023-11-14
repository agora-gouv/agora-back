package fr.gouv.agora.usecase.responseQag

import fr.gouv.agora.domain.*
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import fr.gouv.agora.usecase.responseQag.repository.ResponseQagPreviewCacheRepository
import fr.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
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
        given(qagInfoRepository.getQagResponsesWithSupportCount()).willReturn(emptyList())

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
        then(qagInfoRepository).should(only()).getQagResponsesWithSupportCount()
        then(responseQagRepository).shouldHaveNoInteractions()
        then(thematiqueRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getResponseQagPreviewList - when has qags without matching thematique - should return 2x emptyList and put it to cache`() {
        // Given
        given(cacheRepository.getResponseQagPreviewList()).willReturn(null)
        val qag = mock(QagInfoWithSupportCount::class.java).also {
            given(it.id).willReturn("qagId")
            given(it.thematiqueId).willReturn("unknownThematiqueId")
        }
        given(qagInfoRepository.getQagResponsesWithSupportCount()).willReturn(listOf(qag))

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
        then(qagInfoRepository).should(only()).getQagResponsesWithSupportCount()
        then(responseQagRepository).should(only()).getResponsesQag(listOf("qagId"))
        then(thematiqueRepository).should(only()).getThematiqueList()
    }

    @Test
    fun `getResponseQagPreviewList - when has qags without responses - should return mapped incomingResponse`() {
        // Given
        given(cacheRepository.getResponseQagPreviewList()).willReturn(null)
        val qag = mock(QagInfoWithSupportCount::class.java).also {
            given(it.id).willReturn("qagId")
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(qagInfoRepository.getQagResponsesWithSupportCount()).willReturn(listOf(qag))

        val thematique = mock(Thematique::class.java).also {
            given(it.id).willReturn("thematiqueId")
        }
        given(thematiqueRepository.getThematiqueList()).willReturn(listOf(thematique))

        given(responseQagRepository.getResponsesQag(listOf("qagId"))).willReturn(emptyList())

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
        then(qagInfoRepository).should(only()).getQagResponsesWithSupportCount()
        then(responseQagRepository).should(only()).getResponsesQag(listOf("qagId"))
        then(thematiqueRepository).should(only()).getThematiqueList()
    }

    @Test
    fun `getResponseQagPreviewList - when has qags with responses - should return mapped response`() {
        // Given
        given(cacheRepository.getResponseQagPreviewList()).willReturn(null)
        val qag = mock(QagInfoWithSupportCount::class.java).also {
            given(it.id).willReturn("qagId")
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(qagInfoRepository.getQagResponsesWithSupportCount()).willReturn(listOf(qag))

        val thematique = mock(Thematique::class.java).also {
            given(it.id).willReturn("thematiqueId")
        }
        given(thematiqueRepository.getThematiqueList()).willReturn(listOf(thematique))

        val responseQag = mock(ResponseQagText::class.java).also {
            given(it.qagId).willReturn("qagId")
        }
        given(responseQagRepository.getResponsesQag(listOf("qagId"))).willReturn(listOf(responseQag))

        val responseQagPreview = mock(ResponseQagPreview::class.java)
        given(mapper.toResponseQagPreview(qag, thematique, responseQag)).willReturn(responseQagPreview)

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
        then(qagInfoRepository).should(only()).getQagResponsesWithSupportCount()
        then(responseQagRepository).should(only()).getResponsesQag(listOf("qagId"))
        then(thematiqueRepository).should(only()).getThematiqueList()
    }

}