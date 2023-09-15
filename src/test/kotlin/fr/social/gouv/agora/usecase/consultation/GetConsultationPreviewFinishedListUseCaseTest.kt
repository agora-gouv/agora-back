package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewFinishedRepository
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewOngoingRepository
import fr.social.gouv.agora.usecase.consultationUpdate.ConsultationUpdateUseCase
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

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class GetConsultationPreviewFinishedListUseCaseTest {

    @Autowired
    private lateinit var useCase: GetConsultationPreviewFinishedListUseCase

    @MockBean
    private lateinit var consultationPreviewFinishedRepository: ConsultationPreviewFinishedRepository

    @MockBean
    private lateinit var consultationPreviewOngoingRepository: ConsultationPreviewOngoingRepository

    @MockBean
    private lateinit var consultationUpdateUseCase: ConsultationUpdateUseCase

    @MockBean
    private lateinit var thematiqueRepository: ThematiqueRepository

    @MockBean
    private lateinit var mapper: ConsultationPreviewFinishedMapper

    @Test
    fun `getConsultationPreviewFinishedList - when has consultation finished but no update - should return emptyList`() {
        // Given
        val consultationInfo = mock(ConsultationPreviewFinishedInfo::class.java)
        given(consultationPreviewFinishedRepository.getConsultationFinishedList()).willReturn(listOf(consultationInfo))
        given(consultationUpdateUseCase.getConsultationUpdate(consultationInfo)).willReturn(null)

        // When
        val result = useCase.getConsultationPreviewFinishedList()

        // Then
        assertThat(result).isEqualTo(emptyList<ConsultationPreviewFinished>())
        then(consultationPreviewFinishedRepository).should(only()).getConsultationFinishedList()
        then(consultationUpdateUseCase).should(only()).getConsultationUpdate(consultationInfo)
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationPreviewFinishedList - when has consultation finished, update but no thematique - should return emptyList`() {
        // Given
        val consultationInfo = mock(ConsultationPreviewFinishedInfo::class.java).also {
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(consultationPreviewFinishedRepository.getConsultationFinishedList()).willReturn(listOf(consultationInfo))
        val consultationUpdate = mock(ConsultationUpdate::class.java)
        given(consultationUpdateUseCase.getConsultationUpdate(consultationInfo)).willReturn(consultationUpdate)
        given(thematiqueRepository.getThematique(thematiqueId = "thematiqueId")).willReturn(null)

        // When
        val result = useCase.getConsultationPreviewFinishedList()

        // Then
        assertThat(result).isEqualTo(emptyList<ConsultationPreviewFinished>())
        then(consultationPreviewFinishedRepository).should(only()).getConsultationFinishedList()
        then(consultationUpdateUseCase).should(only()).getConsultationUpdate(consultationInfo)
        then(thematiqueRepository).should(only()).getThematique(thematiqueId = "thematiqueId")
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationPreviewFinishedList - when has consultation finished, update and thematique - should return mapped consultation`() {
        // Given
        val consultationInfo = mock(ConsultationPreviewFinishedInfo::class.java).also {
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(consultationPreviewFinishedRepository.getConsultationFinishedList()).willReturn(listOf(consultationInfo))

        val consultationUpdate = mock(ConsultationUpdate::class.java)
        given(consultationUpdateUseCase.getConsultationUpdate(consultationInfo)).willReturn(consultationUpdate)

        val thematique = mock(Thematique::class.java)
        given(thematiqueRepository.getThematique(thematiqueId = "thematiqueId")).willReturn(thematique)

        val consultationPreview = mock(ConsultationPreviewFinished::class.java)
        given(mapper.toConsultationPreviewFinished(consultationInfo, consultationUpdate, thematique))
            .willReturn(consultationPreview)

        // When
        val result = useCase.getConsultationPreviewFinishedList()

        // Then
        assertThat(result).isEqualTo(listOf(consultationPreview))
        then(consultationPreviewFinishedRepository).should(only()).getConsultationFinishedList()
        then(consultationUpdateUseCase).should(only()).getConsultationUpdate(consultationInfo)
        then(thematiqueRepository).should(only()).getThematique(thematiqueId = "thematiqueId")
        then(mapper).should(only()).toConsultationPreviewFinished(consultationInfo, consultationUpdate, thematique)
    }

    @Test
    fun `getConsultationPreviewFinishedList - when has more than 5 items - should return mapped consultations limited to 5`() {
        // Given
        val consultationInfo = mock(ConsultationPreviewFinishedInfo::class.java).also {
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(consultationPreviewFinishedRepository.getConsultationFinishedList()).willReturn(
            (0 until 10).map { consultationInfo }
        )

        val consultationUpdate = mock(ConsultationUpdate::class.java)
        given(consultationUpdateUseCase.getConsultationUpdate(consultationInfo)).willReturn(consultationUpdate)

        val thematique = mock(Thematique::class.java)
        given(thematiqueRepository.getThematique(thematiqueId = "thematiqueId")).willReturn(thematique)

        val consultationPreview = mock(ConsultationPreviewFinished::class.java)
        given(mapper.toConsultationPreviewFinished(consultationInfo, consultationUpdate, thematique))
            .willReturn(consultationPreview)

        // When
        val result = useCase.getConsultationPreviewFinishedList()

        // Then
        assertThat(result).isEqualTo(
            listOf(
                consultationPreview,
                consultationPreview,
                consultationPreview,
                consultationPreview,
                consultationPreview,
            )
        )
        then(consultationPreviewFinishedRepository).should(only()).getConsultationFinishedList()
        then(consultationUpdateUseCase).should(times(5)).getConsultationUpdate(consultationInfo)
        then(thematiqueRepository).should(times(1)).getThematique(thematiqueId = "thematiqueId")
        then(mapper).should(times(5)).toConsultationPreviewFinished(consultationInfo, consultationUpdate, thematique)
    }

    @Test
    fun `getConsultationPreviewFinishedList - when has no consultation finished - should look for ongoing instead and return mapped consultations limited to 5`() {
        // Given
        val consultationInfo = mock(ConsultationPreviewOngoingInfo::class.java).also {
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(consultationPreviewFinishedRepository.getConsultationFinishedList()).willReturn(emptyList())
        given(consultationPreviewOngoingRepository.getConsultationPreviewOngoingList()).willReturn(
            (0 until 10).map { consultationInfo }
        )

        val consultationUpdate = mock(ConsultationUpdate::class.java)
        given(consultationUpdateUseCase.getConsultationUpdate(consultationInfo)).willReturn(consultationUpdate)

        val thematique = mock(Thematique::class.java)
        given(thematiqueRepository.getThematique(thematiqueId = "thematiqueId")).willReturn(thematique)

        val consultationPreview = mock(ConsultationPreviewFinished::class.java)
        given(mapper.toConsultationPreviewFinished(consultationInfo, consultationUpdate, thematique))
            .willReturn(consultationPreview)

        // When
        val result = useCase.getConsultationPreviewFinishedList()

        // Then
        assertThat(result).isEqualTo(
            listOf(
                consultationPreview,
                consultationPreview,
                consultationPreview,
                consultationPreview,
                consultationPreview,
            )
        )
        then(consultationPreviewFinishedRepository).should(only()).getConsultationFinishedList()
        then(consultationUpdateUseCase).should(times(5)).getConsultationUpdate(consultationInfo)
        then(thematiqueRepository).should(times(1)).getThematique(thematiqueId = "thematiqueId")
        then(mapper).should(times(5)).toConsultationPreviewFinished(consultationInfo, consultationUpdate, thematique)
    }

}