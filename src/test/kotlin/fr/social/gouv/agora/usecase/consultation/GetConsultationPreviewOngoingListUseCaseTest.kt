package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewOngoingRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
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
internal class GetConsultationPreviewOngoingListUseCaseTest {

    @Autowired
    private lateinit var useCase: GetConsultationPreviewOngoingListUseCase

    @MockBean
    private lateinit var consultationPreviewOngoingRepository: ConsultationPreviewOngoingRepository

    @MockBean
    private lateinit var thematiqueRepository: ThematiqueRepository

    @MockBean
    private lateinit var consultationResponseRepository: GetConsultationResponseRepository

    private val consultationPreviewOngoingInfo = mock(ConsultationPreviewOngoingInfo::class.java).also {
        given(it.id).willReturn("consultationId")
        given(it.title).willReturn("title")
        given(it.coverUrl).willReturn("coverUrl")
        given(it.thematiqueId).willReturn("thematiqueId")
        given(it.endDate).willReturn(Date(1))
    }

    private val consultationPreviewOngoing = ConsultationPreviewOngoing(
        id = "consultationId",
        title = "title",
        coverUrl = "coverUrl",
        endDate = Date(1),
        thematique = mock(Thematique::class.java),
        hasAnswered = false,
    )

    @Test
    fun `getConsultationPreviewOngoingList - when has empty consultation - should return emptyList`() {
        // Given
        given(consultationPreviewOngoingRepository.getConsultationPreviewOngoingList()).willReturn(emptyList())

        // When
        val result = useCase.getConsultationPreviewOngoingList(userId = "userId")

        // Then
        assertThat(result).isEqualTo(emptyList<ConsultationPreviewOngoing>())
        then(consultationPreviewOngoingRepository).should(only()).getConsultationPreviewOngoingList()
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(consultationResponseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationPreviewOngoingList - when has consultationInfo but no thematique - should return null`() {
        // Given
        val consultationPreviewOngoingInfo = mock(ConsultationPreviewOngoingInfo::class.java).also {
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(consultationPreviewOngoingRepository.getConsultationPreviewOngoingList())
            .willReturn(listOf(consultationPreviewOngoingInfo))
        given(thematiqueRepository.getThematique(thematiqueId = "thematiqueId")).willReturn(null)

        // When
        val result = useCase.getConsultationPreviewOngoingList(userId = "userId")

        // Then
        assertThat(result).isEqualTo(emptyList<ConsultationPreviewOngoing>())
        then(consultationPreviewOngoingRepository).should(only()).getConsultationPreviewOngoingList()
        then(thematiqueRepository).should(only()).getThematique(thematiqueId = "thematiqueId")
        then(consultationResponseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationPreviewOngoingList - when has consultationInfo and thematique but no response from user - should return Consultation with hasAnswered false`() {
        // Given
        given(consultationPreviewOngoingRepository.getConsultationPreviewOngoingList())
            .willReturn(listOf(consultationPreviewOngoingInfo))

        val thematique = mock(Thematique::class.java)
        given(thematiqueRepository.getThematique(thematiqueId = "thematiqueId")).willReturn(thematique)

        given(consultationResponseRepository.getConsultationResponses(consultationId = "consultationId"))
            .willReturn(emptyList())

        // When
        val result = useCase.getConsultationPreviewOngoingList(userId = "userId")

        // Then
        assertThat(result).isEqualTo(
            listOf(consultationPreviewOngoing.copy(thematique = thematique, hasAnswered = false))
        )
        then(consultationPreviewOngoingRepository).should(only()).getConsultationPreviewOngoingList()
        then(thematiqueRepository).should(only()).getThematique(thematiqueId = "thematiqueId")
        then(consultationResponseRepository).should(only()).getConsultationResponses(consultationId = "consultationId")
    }

    @Test
    fun `getConsultationPreviewOngoingList - when has consultationInfo and thematique and has response from user - should return Consultation with hasAnswered true`() {
        // Given
        given(consultationPreviewOngoingRepository.getConsultationPreviewOngoingList())
            .willReturn(listOf(consultationPreviewOngoingInfo))

        val thematique = mock(Thematique::class.java)
        given(thematiqueRepository.getThematique(thematiqueId = "thematiqueId")).willReturn(thematique)

        val responseConsultation = mock(ReponseConsultation::class.java).also {
            given(it.userId).willReturn("userId")
        }
        given(consultationResponseRepository.getConsultationResponses(consultationId = "consultationId"))
            .willReturn(listOf(responseConsultation))

        // When
        val result = useCase.getConsultationPreviewOngoingList(userId = "userId")

        // Then
        assertThat(result).isEqualTo(
            listOf(consultationPreviewOngoing.copy(thematique = thematique, hasAnswered = true))
        )
        then(consultationPreviewOngoingRepository).should(only()).getConsultationPreviewOngoingList()
        then(thematiqueRepository).should(only()).getThematique(thematiqueId = "thematiqueId")
        then(consultationResponseRepository).should(only()).getConsultationResponses(consultationId = "consultationId")
    }

}