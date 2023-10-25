package fr.social.gouv.agora.usecase.reponseQag

import fr.social.gouv.agora.domain.ResponseQag
import fr.social.gouv.agora.domain.ResponseQagPreview
import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.responseQag.GetResponseQagPreviewPaginatedListUseCase
import fr.social.gouv.agora.usecase.responseQag.ResponseQagPaginatedList
import fr.social.gouv.agora.usecase.responseQag.ResponseQagPreviewListMapper
import fr.social.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class GetResponseQagPreviewPaginatedListUseCaseTest {

    @Autowired
    private lateinit var useCase: GetResponseQagPreviewPaginatedListUseCase

    @MockBean
    private lateinit var responseQagRepository: ResponseQagRepository

    @MockBean
    private lateinit var qagInfoRepository: QagInfoRepository

    @MockBean
    private lateinit var thematiqueRepository: ThematiqueRepository

    @MockBean
    private lateinit var mapper: ResponseQagPreviewListMapper

    @Test
    fun `getResponseQagPreviewPaginatedList - when pageNumber is lower or equals 0 - should return null`() {
        // When
        val result = useCase.getResponseQagPreviewPaginatedList(
            pageNumber = 0,
        )

        // Then
        assertThat(result).isEqualTo(null)
        then(responseQagRepository).shouldHaveNoInteractions()
        then(qagInfoRepository).shouldHaveNoInteractions()
        then(thematiqueRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getResponseQagPreviewPaginatedList - when pageNumber is higher than maxPageNumber - should return null`() {
        // Given
        given(responseQagRepository.getResponsesQagCount()).willReturn(5)

        // When
        val result = useCase.getResponseQagPreviewPaginatedList(pageNumber = 2)

        // Then
        assertThat(result).isEqualTo(null)
        then(responseQagRepository).should(only()).getResponsesQagCount()
        then(qagInfoRepository).shouldHaveNoInteractions()
        then(thematiqueRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getResponseQagPreviewPaginatedList - when has responses without qag - should return emptyList`() {
        // Given
        given(responseQagRepository.getResponsesQagCount()).willReturn(1)

        val responseQag = mock(ResponseQag::class.java).also {
            given(it.qagId).willReturn("qagId")
        }
        given(responseQagRepository.getResponsesQag(offset = 0)).willReturn(listOf(responseQag))

        given(qagInfoRepository.getQagsInfo(qagIds = listOf("qagId"))).willReturn(emptyList())
        given(thematiqueRepository.getThematiqueList()).willReturn(emptyList())

        // When
        val result = useCase.getResponseQagPreviewPaginatedList(pageNumber = 1)

        // Then
        assertThat(result).isEqualTo(
            ResponseQagPaginatedList(
                responsesQag = emptyList(),
                maxPageNumber = 1,
            )
        )
        then(responseQagRepository).should().getResponsesQagCount()
        then(responseQagRepository).should().getResponsesQag(offset = 0)
        then(responseQagRepository).shouldHaveNoMoreInteractions()
        then(qagInfoRepository).should(only()).getQagsInfo(listOf("qagId"))
        then(thematiqueRepository).should(only()).getThematiqueList()
    }

    @Test
    fun `getResponseQagPreviewPaginatedList - when has responses with qag but no thematique - should return emptyList`() {
        // Given
        given(responseQagRepository.getResponsesQagCount()).willReturn(50)

        val responseQag = mock(ResponseQag::class.java).also {
            given(it.qagId).willReturn("qagId")
        }
        given(responseQagRepository.getResponsesQag(offset = 20)).willReturn(listOf(responseQag))

        val qag = mock(QagInfo::class.java).also {
            given(it.id).willReturn("qagId")
        }
        given(qagInfoRepository.getQagsInfo(qagIds = listOf("qagId"))).willReturn(listOf(qag))
        given(thematiqueRepository.getThematiqueList()).willReturn(emptyList())

        // When
        val result = useCase.getResponseQagPreviewPaginatedList(pageNumber = 2)

        // Then
        assertThat(result).isEqualTo(
            ResponseQagPaginatedList(
                responsesQag = emptyList(),
                maxPageNumber = 3,
            )
        )
        then(responseQagRepository).should().getResponsesQagCount()
        then(responseQagRepository).should().getResponsesQag(offset = 20)
        then(responseQagRepository).shouldHaveNoMoreInteractions()
        then(qagInfoRepository).should(only()).getQagsInfo(listOf("qagId"))
        then(thematiqueRepository).should(only()).getThematiqueList()
    }

    @Test
    fun `getResponseQagPreviewPaginatedList - when has responses with qag and thematique - should return mapped response`() {
        // Given
        given(responseQagRepository.getResponsesQagCount()).willReturn(81)

        val responseQag = mock(ResponseQag::class.java).also {
            given(it.qagId).willReturn("qagId")
        }
        given(responseQagRepository.getResponsesQag(offset = 80)).willReturn(listOf(responseQag))

        val qag = mock(QagInfo::class.java).also {
            given(it.id).willReturn("qagId")
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(qagInfoRepository.getQagsInfo(qagIds = listOf("qagId"))).willReturn(listOf(qag))

        val thematique = mock(Thematique::class.java).also {
            given(it.id).willReturn("thematiqueId")
        }
        given(thematiqueRepository.getThematiqueList()).willReturn(listOf(thematique))

        val responseQagPreview = mock(ResponseQagPreview::class.java)
        given(mapper.toResponseQagPreview(qag = qag, thematique = thematique, responseQag = responseQag))
            .willReturn(responseQagPreview)

        // When
        val result = useCase.getResponseQagPreviewPaginatedList(pageNumber = 5)

        // Then
        assertThat(result).isEqualTo(
            ResponseQagPaginatedList(
                responsesQag = listOf(responseQagPreview),
                maxPageNumber = 5,
            )
        )
        then(responseQagRepository).should().getResponsesQagCount()
        then(responseQagRepository).should().getResponsesQag(offset = 80)
        then(responseQagRepository).shouldHaveNoMoreInteractions()
        then(qagInfoRepository).should(only()).getQagsInfo(listOf("qagId"))
        then(thematiqueRepository).should(only()).getThematiqueList()
    }

}