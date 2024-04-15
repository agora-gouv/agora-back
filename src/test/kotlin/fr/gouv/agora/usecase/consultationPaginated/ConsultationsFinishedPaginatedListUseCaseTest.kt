package fr.gouv.agora.usecase.consultationPaginated

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.usecase.consultation.ConsultationPreviewFinishedMapper
import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationFinishedPaginatedListCacheRepository
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationPreviewFinishedRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class ConsultationsFinishedPaginatedListUseCaseTest {

    @InjectMocks
    private lateinit var useCase: ConsultationsFinishedPaginatedListUseCase

    @Mock
    private lateinit var consultationPreviewFinishedRepository: ConsultationPreviewFinishedRepository

    @Mock
    private lateinit var thematiqueRepository: ThematiqueRepository

    @Mock
    private lateinit var mapper: ConsultationPreviewFinishedMapper

    @Mock
    private lateinit var cacheRepository: ConsultationFinishedPaginatedListCacheRepository

    companion object {
        @JvmStatic
        fun getConsultationFinishedPaginatedListTestCases() = arrayOf(
            input(
                consultationFinishedCount = 1,
                pageNumber = 1,
                expectedOffset = 0,
                expectedMaxPageNumber = 1,
            ),
            input(
                consultationFinishedCount = 21,
                pageNumber = 1,
                expectedOffset = 0,
                expectedMaxPageNumber = 2,
            ),
            input(
                consultationFinishedCount = 39,
                pageNumber = 2,
                expectedOffset = 20,
                expectedMaxPageNumber = 2,
            ),
            input(
                consultationFinishedCount = 80,
                pageNumber = 3,
                expectedOffset = 40,
                expectedMaxPageNumber = 4,
            ),
        )

        private fun input(
            consultationFinishedCount: Int,
            pageNumber: Int,
            expectedOffset: Int,
            expectedMaxPageNumber: Int,
        ) = arrayOf(consultationFinishedCount, pageNumber, expectedOffset, expectedMaxPageNumber)
    }

    @Test
    fun `getConsultationFinishedPaginatedList - when pageNumber is lower or equals 0 - should return null`() {
        // When
        val result = useCase.getConsultationFinishedPaginatedList(pageNumber = 0)

        // Then
        assertThat(result).isEqualTo(null)
        then(consultationPreviewFinishedRepository).shouldHaveNoInteractions()
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationFinishedPaginatedList - when has cache - should return cached content`() {
        // Given
        val pageContent = mock(ConsultationFinishedPaginatedList::class.java)
        given(cacheRepository.getConsultationFinishedPage(pageNumber = 1)).willReturn(pageContent)

        // When
        val result = useCase.getConsultationFinishedPaginatedList(pageNumber = 1)

        // Then
        assertThat(result).isEqualTo(pageContent)
    }

    @Test
    fun `getConsultationFinishedPaginatedList - when no cache pageNumber is higher than maxPageNumber - should return null`() {
        // Given
        given(consultationPreviewFinishedRepository.getConsultationFinishedCount()).willReturn(10)

        // When
        val result = useCase.getConsultationFinishedPaginatedList(pageNumber = 7)

        // Then
        assertThat(result).isEqualTo(null)
        then(consultationPreviewFinishedRepository).should(only()).getConsultationFinishedCount()
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationFinishedPaginatedList - when no cache and have a correct pageNumber - should return consultationInfo with a matching thematique mapped to ConsultationPreviewFinished`() {
        // Given
        val consultationInfo = mock(ConsultationWithUpdateInfo::class.java).also {
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        val consultationInfoWithoutThematique = mock(ConsultationWithUpdateInfo::class.java).also {
            given(it.thematiqueId).willReturn("unknownThematiqueId")
        }
        val thematique = mock(Thematique::class.java).also { given(it.id).willReturn("thematiqueId") }
        val consultationPreviewFinished = mock(ConsultationPreviewFinished::class.java)

        given(consultationPreviewFinishedRepository.getConsultationFinishedCount()).willReturn(34)
        given(consultationPreviewFinishedRepository.getConsultationFinishedList(offset = 0))
            .willReturn(listOf(consultationInfo, consultationInfoWithoutThematique))
        given(thematiqueRepository.getThematiqueList()).willReturn(listOf(thematique))
        given(
            mapper.toConsultationPreviewFinished(
                consultationInfo = consultationInfo,
                thematique = thematique,
            )
        ).willReturn(consultationPreviewFinished)

        // When
        val result = useCase.getConsultationFinishedPaginatedList(pageNumber = 1)

        // Then
        val pageContent = ConsultationFinishedPaginatedList(
            consultationFinishedList = listOf(consultationPreviewFinished),
            maxPageNumber = 2,
        )
        assertThat(result).isEqualTo(pageContent)

        then(cacheRepository).should().getConsultationFinishedPage(pageNumber = 1)
        then(cacheRepository).should().initConsultationFinishedPage(pageNumber = 1, content = pageContent)
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(consultationPreviewFinishedRepository).should().getConsultationFinishedCount()
        then(consultationPreviewFinishedRepository).should().getConsultationFinishedList(offset = 0)
        then(consultationPreviewFinishedRepository).shouldHaveNoMoreInteractions()
        then(thematiqueRepository).should().getThematiqueList()
        then(mapper).should(only())
            .toConsultationPreviewFinished(consultationInfo = consultationInfo, thematique = thematique)
        then(mapper).shouldHaveNoMoreInteractions()
    }

    @ParameterizedTest(name = "getConsultationFinishedPaginatedList - when consultationFinishedCount is {0} and pageNumber is {1} - should use offset {2} return maxPageNumber {3}")
    @MethodSource("getConsultationFinishedPaginatedListTestCases")
    fun `getConsultationFinishedPaginatedList - when no cache have a correct pageNumber - should use correct offset`(
        consultationFinishedCount: Int,
        pageNumber: Int,
        expectedOffset: Int,
        expectedMaxPageNumber: Int,
    ) {
        // Given
        val consultationInfo = mock(ConsultationWithUpdateInfo::class.java).also {
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        val thematique = mock(Thematique::class.java).also { given(it.id).willReturn("thematiqueId") }
        val consultationPreviewFinished = mock(ConsultationPreviewFinished::class.java)

        given(consultationPreviewFinishedRepository.getConsultationFinishedCount()).willReturn(consultationFinishedCount)
        given(consultationPreviewFinishedRepository.getConsultationFinishedList(offset = expectedOffset))
            .willReturn(listOf(consultationInfo))
        given(thematiqueRepository.getThematiqueList()).willReturn(listOf(thematique))
        given(
            mapper.toConsultationPreviewFinished(
                consultationInfo = consultationInfo,
                thematique = thematique,
            )
        ).willReturn(consultationPreviewFinished)

        // When
        val result = useCase.getConsultationFinishedPaginatedList(pageNumber = pageNumber)

        // Then
        val pageContent = ConsultationFinishedPaginatedList(
            consultationFinishedList = listOf(consultationPreviewFinished),
            maxPageNumber = expectedMaxPageNumber,
        )
        assertThat(result).isEqualTo(pageContent)

        then(cacheRepository).should().getConsultationFinishedPage(pageNumber = pageNumber)
        then(cacheRepository).should().initConsultationFinishedPage(pageNumber = pageNumber, content = pageContent)
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(consultationPreviewFinishedRepository).should().getConsultationFinishedCount()
        then(consultationPreviewFinishedRepository).should().getConsultationFinishedList(offset = expectedOffset)
        then(consultationPreviewFinishedRepository).shouldHaveNoMoreInteractions()
        then(thematiqueRepository).should().getThematiqueList()
        then(mapper).should(only())
            .toConsultationPreviewFinished(consultationInfo = consultationInfo, thematique = thematique)
        then(mapper).shouldHaveNoMoreInteractions()
    }

}
