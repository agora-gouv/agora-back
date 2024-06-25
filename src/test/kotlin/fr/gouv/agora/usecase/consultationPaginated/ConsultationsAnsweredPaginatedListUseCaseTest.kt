package fr.gouv.agora.usecase.consultationPaginated

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.usecase.consultation.ConsultationPreviewFinishedMapper
import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationAnsweredPaginatedListCacheRepository
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationPreviewAnsweredRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class ConsultationsAnsweredPaginatedListUseCaseTest {

    @InjectMocks
    private lateinit var useCase: ConsultationsAnsweredPaginatedListUseCase

    @Mock
    private lateinit var consultationPreviewAnsweredRepository: ConsultationPreviewAnsweredRepository

    @Mock
    private lateinit var thematiqueRepository: ThematiqueRepository

    @Mock
    private lateinit var mapper: ConsultationPreviewFinishedMapper

    @Mock
    private lateinit var cacheRepository: ConsultationAnsweredPaginatedListCacheRepository

    companion object {
        @JvmStatic
        fun getConsultationAnsweredPaginatedListTestCases() = arrayOf(
            input(
                consultationAnsweredCount = 1,
                pageNumber = 1,
                expectedOffset = 0,
                expectedMaxPageNumber = 1,
            ),
            input(
                consultationAnsweredCount = 21,
                pageNumber = 1,
                expectedOffset = 0,
                expectedMaxPageNumber = 2,
            ),
            input(
                consultationAnsweredCount = 39,
                pageNumber = 2,
                expectedOffset = 20,
                expectedMaxPageNumber = 2,
            ),
            input(
                consultationAnsweredCount = 80,
                pageNumber = 3,
                expectedOffset = 40,
                expectedMaxPageNumber = 4,
            ),
        )

        private fun input(
            consultationAnsweredCount: Int,
            pageNumber: Int,
            expectedOffset: Int,
            expectedMaxPageNumber: Int,
        ) = arrayOf(consultationAnsweredCount, pageNumber, expectedOffset, expectedMaxPageNumber)
    }

    @Test
    fun `getConsultationAnsweredPaginatedList - when pageNumber is lower or equals 0 - should return null`() {
        // When
        val result = useCase.getConsultationAnsweredPaginatedList(userId = "userId", pageNumber = 0)

        // Then
        assertThat(result).isEqualTo(null)
        then(consultationPreviewAnsweredRepository).shouldHaveNoInteractions()
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationAnsweredPaginatedList - when has cache - should return cached content`() {
        // Given
        val pageContent = mock(ConsultationAnsweredPaginatedList::class.java)
        given(cacheRepository.getConsultationAnsweredPage(userId = "userId", pageNumber = 1)).willReturn(pageContent)

        // When
        val result = useCase.getConsultationAnsweredPaginatedList(userId = "userId", pageNumber = 1)

        // Then
        assertThat(result).isEqualTo(pageContent)
    }

    @Test
    fun `getConsultationAnsweredPaginatedList - when no cache pageNumber is higher than maxPageNumber - should return null`() {
        // Given
        given(consultationPreviewAnsweredRepository.getConsultationAnsweredCount(userId = "userId")).willReturn(10)

        // When
        val result = useCase.getConsultationAnsweredPaginatedList(userId = "userId", pageNumber = 7)

        // Then
        assertThat(result).isEqualTo(null)
        then(consultationPreviewAnsweredRepository).should(only()).getConsultationAnsweredCount(userId = "userId")
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationAnsweredPaginatedList - when no cache and have a correct pageNumber - should return consultationInfo with a matching thematique mapped to ConsultationPreviewFinished`() {
        // Given
        val consultationInfo = mock(ConsultationWithUpdateInfo::class.java).also {
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        val consultationInfoWithoutThematique = mock(ConsultationWithUpdateInfo::class.java).also {
            given(it.thematiqueId).willReturn("unknownThematiqueId")
        }
        val thematique = mock(Thematique::class.java)
        val consultationPreviewAnswered = mock(ConsultationPreviewFinished::class.java)

        given(consultationPreviewAnsweredRepository.getConsultationAnsweredCount(userId = "userId")).willReturn(34)
        given(consultationPreviewAnsweredRepository.getConsultationAnsweredList(userId = "userId", offset = 0))
            .willReturn(listOf(consultationInfo, consultationInfoWithoutThematique))
        given(thematiqueRepository.getThematique("thematiqueId")).willReturn(thematique)
        given(
            mapper.toConsultationPreviewFinished(
                consultationInfo = consultationInfo,
                thematique = thematique,
            )
        ).willReturn(consultationPreviewAnswered)

        // When
        val result = useCase.getConsultationAnsweredPaginatedList(userId = "userId", pageNumber = 1)

        // Then
        val pageContent = ConsultationAnsweredPaginatedList(
            consultationAnsweredList = listOf(consultationPreviewAnswered),
            maxPageNumber = 2,
        )
        assertThat(result).isEqualTo(pageContent)

        then(cacheRepository).should().getConsultationAnsweredPage(userId = "userId", pageNumber = 1)
        then(cacheRepository).should()
            .initConsultationAnsweredPage(userId = "userId", pageNumber = 1, content = pageContent)
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(consultationPreviewAnsweredRepository).should().getConsultationAnsweredCount(userId = "userId")
        then(consultationPreviewAnsweredRepository).should().getConsultationAnsweredList(userId = "userId", offset = 0)
        then(consultationPreviewAnsweredRepository).shouldHaveNoMoreInteractions()
        then(mapper).should(only())
            .toConsultationPreviewFinished(consultationInfo = consultationInfo, thematique = thematique)
        then(mapper).shouldHaveNoMoreInteractions()
    }

    @ParameterizedTest(name = "getConsultationAnsweredPaginatedList - when consultationAnsweredCount is {0} and pageNumber is {1} - should use offset {2} return maxPageNumber {3}")
    @MethodSource("getConsultationAnsweredPaginatedListTestCases")
    fun `getConsultationAnsweredPaginatedList - when no cache have a correct pageNumber - should use correct offset`(
        consultationAnsweredCount: Int,
        pageNumber: Int,
        expectedOffset: Int,
        expectedMaxPageNumber: Int,
    ) {
        // Given
        val consultationInfo = mock(ConsultationWithUpdateInfo::class.java).also {
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        val thematique = mock(Thematique::class.java)
        val consultationPreviewAnswered = mock(ConsultationPreviewFinished::class.java)

        given(consultationPreviewAnsweredRepository.getConsultationAnsweredCount(userId = "userId"))
            .willReturn(consultationAnsweredCount)
        given(
            consultationPreviewAnsweredRepository.getConsultationAnsweredList(
                userId = "userId",
                offset = expectedOffset
            )
        ).willReturn(listOf(consultationInfo))
        given(thematiqueRepository.getThematique("thematiqueId")).willReturn(thematique)
        given(
            mapper.toConsultationPreviewFinished(
                consultationInfo = consultationInfo,
                thematique = thematique,
            )
        ).willReturn(consultationPreviewAnswered)

        // When
        val result = useCase.getConsultationAnsweredPaginatedList(userId = "userId", pageNumber = pageNumber)

        // Then
        val pageContent = ConsultationAnsweredPaginatedList(
            consultationAnsweredList = listOf(consultationPreviewAnswered),
            maxPageNumber = expectedMaxPageNumber,
        )
        assertThat(result).isEqualTo(pageContent)

        then(cacheRepository).should().getConsultationAnsweredPage(userId = "userId", pageNumber = pageNumber)
        then(cacheRepository).should()
            .initConsultationAnsweredPage(userId = "userId", pageNumber = pageNumber, content = pageContent)
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(consultationPreviewAnsweredRepository).should().getConsultationAnsweredCount(userId = "userId")
        then(consultationPreviewAnsweredRepository).should()
            .getConsultationAnsweredList(userId = "userId", offset = expectedOffset)
        then(consultationPreviewAnsweredRepository).shouldHaveNoMoreInteractions()
        then(mapper).should(only())
            .toConsultationPreviewFinished(consultationInfo = consultationInfo, thematique = thematique)
        then(mapper).shouldHaveNoMoreInteractions()
    }

}
