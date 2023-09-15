package fr.social.gouv.agora.usecase.consultationPaginated

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.usecase.consultation.ConsultationPreviewFinishedMapper
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewFinishedRepository
import fr.social.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.Mockito.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class GetConsultationFinishedPaginatedListUseCaseTest {

    @Autowired
    private lateinit var useCase: GetConsultationFinishedPaginatedListUseCase

    @MockBean
    private lateinit var consultationPreviewFinishedRepository: ConsultationPreviewFinishedRepository

    @MockBean
    private lateinit var thematiqueRepository: ThematiqueRepository

    @MockBean
    private lateinit var consultationUpdateRepository: ConsultationUpdateRepository

    @MockBean
    private lateinit var mapper: ConsultationPreviewFinishedMapper

    @Test
    fun `getConsultationFinishedPaginatedList - when pageNumber is lower or equals 0 - should return null`() {
        // When
        val result = useCase.getConsultationFinishedPaginatedList(
            pageNumber = 0,
        )

        // Then
        assertThat(result).isEqualTo(null)
        then(consultationPreviewFinishedRepository).shouldHaveNoInteractions()
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(consultationUpdateRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationFinishedPaginatedList - when pageNumber is higher than maxPageNumber - should return null`() {
        // Given
        val consultationFinishedList =
            (0 until 21).map { index -> mockConsultationFinished(index = index) }
        given(consultationPreviewFinishedRepository.getConsultationFinishedList()).willReturn(consultationFinishedList.map { it.consultationPreviewFinishedInfo })

        // When
        val result = useCase.getConsultationFinishedPaginatedList(
            pageNumber = 3,
        )

        // Then
        assertThat(result).isEqualTo(null)
        then(consultationPreviewFinishedRepository).should(only()).getConsultationFinishedList()
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(consultationUpdateRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }


    @Test
    fun `getConsultationFinishedPaginatedList - when have less than 20 results and pageNumber is 1 - should return ConsultationPreviewFinished with maxPageNumber 1`() {
        // Given
        val consultationFinishedList =
            (0 until 11).map { index -> mockConsultationFinished(index = index) }
        given(consultationPreviewFinishedRepository.getConsultationFinishedList()).willReturn(consultationFinishedList.map { it.consultationPreviewFinishedInfo })

        // When
        val result = useCase.getConsultationFinishedPaginatedList(
            pageNumber = 1,
        )

        // Then
        assertThat(result).isEqualTo(
            ConsultationFinishedPaginatedList(
                consultationFinishedList = consultationFinishedList.map { it.consultationPreviewFinished },
                maxPageNumber = 1,
            )
        )
        then(consultationPreviewFinishedRepository).should(only()).getConsultationFinishedList()
        consultationFinishedList.forEach {
            then(consultationUpdateRepository).should().getFinishedConsultationUpdate(it.consultationPreviewFinishedInfo.id)
            then(thematiqueRepository).should().getThematique(it.consultationPreviewFinishedInfo.thematiqueId)
            then(mapper).should()
                .toConsultationPreviewFinished(
                    consultationPreviewInfo = it.consultationPreviewFinishedInfo,
                    consultationUpdate = it.consultationUpdate,
                    thematique = it.thematique
                )
        }
        then(mapper).shouldHaveNoMoreInteractions()
    }


    @Test
    fun `getConsultationFinishedPaginatedList - when have more than 20 results and pageNumber is 1 - should return ConsultationPreviewFinished limited to 20`() {
        // Given
        val consultationFinishedListFirstPage =
            (20 downTo 1).map { index -> mockConsultationFinished(index = index) }
        val consultationFinishedListOtherPage =
            (21 until 50).map { index -> mockConsultationFinished(index = index) }

        given(consultationPreviewFinishedRepository.getConsultationFinishedList())
            .willReturn(consultationFinishedListFirstPage.map { it.consultationPreviewFinishedInfo } + consultationFinishedListOtherPage.map { it.consultationPreviewFinishedInfo })

        // When
        val result = useCase.getConsultationFinishedPaginatedList(
            pageNumber = 1,
        )

        // Then
        assertThat(result?.consultationFinishedList?.size).isEqualTo(20)
        assertThat(result).isEqualTo(
            ConsultationFinishedPaginatedList(
                consultationFinishedList = consultationFinishedListFirstPage.map { it.consultationPreviewFinished },
                maxPageNumber = 3,
            )
        )

        then(consultationPreviewFinishedRepository).should(only()).getConsultationFinishedList()
        consultationFinishedListFirstPage.forEach {
            then(consultationUpdateRepository).should().getFinishedConsultationUpdate(it.consultationPreviewFinishedInfo.id)
            then(thematiqueRepository).should().getThematique(it.consultationPreviewFinishedInfo.thematiqueId)
            then(mapper).should()
                .toConsultationPreviewFinished(
                    consultationPreviewInfo = it.consultationPreviewFinishedInfo,
                    consultationUpdate = it.consultationUpdate,
                    thematique = it.thematique
                )
        }
        then(mapper).shouldHaveNoMoreInteractions()
    }


    @Test
    fun `getConsultationFinishedPaginatedList - when has not enough to fill page 2 entirely and pageNumber is 2 - should return ConsultationPreviewFinished starting from 20th index and limited to 20 items or less`() {
        // Given
        val consultationFinishedListFirstPage =
            (20 downTo 1).map { index -> mockConsultationFinished(index = index) }
        val consultationFinishedListSecondPage =
            (21 until 31).map { index -> mockConsultationFinished(index = index) }

        given(consultationPreviewFinishedRepository.getConsultationFinishedList())
            .willReturn(consultationFinishedListFirstPage.map { it.consultationPreviewFinishedInfo } + consultationFinishedListSecondPage.map { it.consultationPreviewFinishedInfo })

        // When
        val result = useCase.getConsultationFinishedPaginatedList(
            pageNumber = 2,
        )

        // Then
        assertThat(result?.consultationFinishedList?.size).isEqualTo(10)
        assertThat(result).isEqualTo(
            ConsultationFinishedPaginatedList(
                consultationFinishedList = consultationFinishedListSecondPage.map { it.consultationPreviewFinished },
                maxPageNumber = 2,
            )
        )

        then(consultationPreviewFinishedRepository).should(only()).getConsultationFinishedList()
        consultationFinishedListSecondPage.forEach {
            then(consultationUpdateRepository).should().getFinishedConsultationUpdate(it.consultationPreviewFinishedInfo.id)
            then(thematiqueRepository).should().getThematique(it.consultationPreviewFinishedInfo.thematiqueId)
            then(mapper).should()
                .toConsultationPreviewFinished(
                    consultationPreviewInfo = it.consultationPreviewFinishedInfo,
                    consultationUpdate = it.consultationUpdate,
                    thematique = it.thematique
                )
        }
        then(mapper).shouldHaveNoMoreInteractions()
    }

    private fun mockConsultationFinished(index: Int): ConsultationMockResult {
        val consultationPreviewFinishedInfo = mock(ConsultationPreviewFinishedInfo::class.java).also {
            given(it.thematiqueId).willReturn("thematiqueId$index")
            given(it.id).willReturn("consultationId$index")
        }
        val thematique = mock(Thematique::class.java)
        given(thematiqueRepository.getThematique("thematiqueId$index")).willReturn(thematique)

        val consultationUpdate = mock(ConsultationUpdate::class.java)
        given(consultationUpdateRepository.getFinishedConsultationUpdate("consultationId$index")).willReturn(consultationUpdate)

        val consultationPreviewFinished = mock(ConsultationPreviewFinished::class.java)
        given(
            mapper.toConsultationPreviewFinished(
                consultationPreviewInfo = consultationPreviewFinishedInfo,
                consultationUpdate = consultationUpdate,
                thematique = thematique,
            )
        ).willReturn(consultationPreviewFinished)
        return ConsultationMockResult(
            consultationPreviewFinishedInfo = consultationPreviewFinishedInfo,
            consultationUpdate = consultationUpdate,
            thematique = thematique,
            consultationPreviewFinished = consultationPreviewFinished,
        )
    }

}

private data class ConsultationMockResult(
    val consultationPreviewFinishedInfo: ConsultationPreviewFinishedInfo,
    val consultationUpdate: ConsultationUpdate,
    val thematique: Thematique,
    val consultationPreviewFinished: ConsultationPreviewFinished,
)