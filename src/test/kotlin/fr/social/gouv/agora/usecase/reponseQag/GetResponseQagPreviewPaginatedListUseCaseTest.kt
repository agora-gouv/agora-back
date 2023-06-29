package fr.social.gouv.agora.usecase.reponseQag

import fr.social.gouv.agora.domain.ResponseQag
import fr.social.gouv.agora.domain.ResponseQagPreview
import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.responseQag.GetResponseQagPreviewPaginatedListUseCase
import fr.social.gouv.agora.usecase.responseQag.ResponseQagPaginatedList
import fr.social.gouv.agora.usecase.responseQag.ResponseQagPreviewMapper
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
    private lateinit var thematiqueRepository: ThematiqueRepository

    @MockBean
    private lateinit var qagRepository: QagInfoRepository

    @MockBean
    private lateinit var responseQagRepository: ResponseQagRepository

    @MockBean
    private lateinit var mapper: ResponseQagPreviewMapper

    @Test
    fun `getResponseQagPreviewPaginatedList - when pageNumber is lower or equals 0 - should return null`() {
        // When
        val result = useCase.getResponseQagPreviewPaginatedList(
            pageNumber = 0,
        )

        // Then
        assertThat(result).isEqualTo(null)
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(qagRepository).shouldHaveNoInteractions()
        then(responseQagRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getResponseQagPreviewPaginatedList - when pageNumber is higher than maxPageNumber - should return null`() {
        // Given
        val listResponseQag = (0 until 21).map { index -> mockResponseQag(index) }
        given(responseQagRepository.getAllResponseQag()).willReturn(listResponseQag.map { it.responseQag })

        // When
        val result = useCase.getResponseQagPreviewPaginatedList(
            pageNumber = 3,
        )

        // Then
        assertThat(result).isEqualTo(null)
        then(responseQagRepository).should(only()).getAllResponseQag()
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(qagRepository).shouldHaveNoInteractions()
    }


    @Test
    fun `getResponseQagPreviewPaginatedList - when have less than 20 results and pageNumber is 1 - should return ordered responseQagPreviews and maxPageNumber 1`() {
        // Given
        val listResponseQag = (0 until 10).map { index -> mockResponseQag(index) }
        given(responseQagRepository.getAllResponseQag()).willReturn(listResponseQag.map { it.responseQag })

        // When
        val result = useCase.getResponseQagPreviewPaginatedList(
            pageNumber = 1,
        )

        // Then
        assertThat(result).isEqualTo(
            ResponseQagPaginatedList(
                listResponseQag = listResponseQag.map { it.responseQagPreview },
                maxPageNumber = 1,
            )
        )
        then(responseQagRepository).should(only()).getAllResponseQag()
        listResponseQag.forEach {
            then(qagRepository).should().getQagInfo(it.responseQag.qagId)
            then(thematiqueRepository).should().getThematique(it.qagInfo.thematiqueId)
            then(mapper).should()
                .toResponseQagPreview(responseQag = it.responseQag, qagInfo = it.qagInfo, thematique = it.thematique)
        }
        then(mapper).shouldHaveNoMoreInteractions()
    }

    @Test
    fun `getResponseQagPreviewPaginatedList - when have more than 20 results and pageNumber is 1 - should return ordered responseQagPreviews limited to 20`() {
        // Given
        val listResponseQagFirstPage = (0 until 20).map { index -> mockResponseQag(index) }
        val listResponseQagOtherPage = (20 until 50).map { index -> mockResponseQag(index) }

        given(responseQagRepository.getAllResponseQag()).willReturn(listResponseQagFirstPage.map { it.responseQag } + listResponseQagOtherPage.map { it.responseQag })

        // When
        val result = useCase.getResponseQagPreviewPaginatedList(
            pageNumber = 1,
        )

        // Then
        assertThat(result?.listResponseQag?.size).isEqualTo(20)
        assertThat(result).isEqualTo(
            ResponseQagPaginatedList(
                listResponseQag = listResponseQagFirstPage.map { it.responseQagPreview },
                maxPageNumber = 3,
            )
        )

        then(responseQagRepository).should(only()).getAllResponseQag()
        listResponseQagFirstPage.forEach {
            then(qagRepository).should().getQagInfo(it.responseQag.qagId)
            then(thematiqueRepository).should().getThematique(it.qagInfo.thematiqueId)
            then(mapper).should()
                .toResponseQagPreview(responseQag = it.responseQag, qagInfo = it.qagInfo, thematique = it.thematique)
        }
        then(mapper).shouldHaveNoMoreInteractions()
    }

    @Test
    fun `getResponseQagPreviewPaginatedList - when has not enough to fill page 2 entirely and pageNumber is 2 - should return ordered responseQagPreviews starting from 20th index and limited to 20 items or less`() {
        // Given
        val listResponseQagFirstPage = (0 until 20).map { index -> mockResponseQag(index) }
        val listResponseQagSecondPage = (20 until 30).map { index -> mockResponseQag(index) }

        given(responseQagRepository.getAllResponseQag()).willReturn(listResponseQagFirstPage.map { it.responseQag } + listResponseQagSecondPage.map { it.responseQag })

        // When
        val result = useCase.getResponseQagPreviewPaginatedList(
            pageNumber = 2,
        )

        // Then

        assertThat(result?.listResponseQag?.size).isEqualTo(10)
        assertThat(result).isEqualTo(
            ResponseQagPaginatedList(
                listResponseQag = listResponseQagSecondPage.map { it.responseQagPreview },
                maxPageNumber = 2,
            )
        )

        then(responseQagRepository).should(only()).getAllResponseQag()
        listResponseQagSecondPage.forEach {
            then(qagRepository).should().getQagInfo(it.responseQag.qagId)
            then(thematiqueRepository).should().getThematique(it.qagInfo.thematiqueId)
            then(mapper).should()
                .toResponseQagPreview(responseQag = it.responseQag, qagInfo = it.qagInfo, thematique = it.thematique)
        }
        then(mapper).shouldHaveNoMoreInteractions()
    }

    private fun mockResponseQag(index: Int): MockResult {
        val responseQag = mock(ResponseQag::class.java).also { given(it.qagId).willReturn("qagId$index") }
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.thematiqueId).willReturn("thematiqueId$index")
        }
        val thematique = mock(Thematique::class.java)

        given(qagRepository.getQagInfo("qagId$index")).willReturn(qagInfo)
        given(thematiqueRepository.getThematique("thematiqueId$index")).willReturn(thematique)

        val reponseQagPreview = mock(ResponseQagPreview::class.java)
        given(
            mapper.toResponseQagPreview(
                responseQag = responseQag,
                qagInfo = qagInfo,
                thematique = thematique,
            )
        ).willReturn(reponseQagPreview)
        return MockResult(
            responseQag = responseQag,
            qagInfo = qagInfo,
            thematique = thematique,
            responseQagPreview = reponseQagPreview,
        )
    }

}

private data class MockResult(
    val responseQag: ResponseQag,
    val qagInfo: QagInfo,
    val thematique: Thematique,
    val responseQagPreview: ResponseQagPreview,
)