package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.domain.Consultation
import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
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
internal class GetConsultationUseCaseTest {

    @Autowired
    private lateinit var useCase: GetConsultationUseCase

    @MockBean
    private lateinit var consultationInfoRepository: ConsultationInfoRepository

    @MockBean
    private lateinit var thematiqueRepository: ThematiqueRepository

    @MockBean
    private lateinit var consultationResponseRepository: GetConsultationResponseRepository

    private val consultationInfo = mock(ConsultationInfo::class.java).also {
        given(it.thematiqueId).willReturn("thematiqueId")
        given(it.id).willReturn("id")
        given(it.title).willReturn("title")
        given(it.coverUrl).willReturn("coverUrl")
        given(it.startDate).willReturn(Date(0))
        given(it.endDate).willReturn(Date(1))
        given(it.questionCount).willReturn("questionCount")
        given(it.estimatedTime).willReturn("estimatedTime")
        given(it.participantCountGoal).willReturn(34)
        given(it.description).willReturn("description")
        given(it.tipsDescription).willReturn("tipsDescription")
    }

    private val consultation = Consultation(
        id = "id",
        title = "title",
        coverUrl = "coverUrl",
        startDate = Date(0),
        endDate = Date(1),
        questionCount = "questionCount",
        estimatedTime = "estimatedTime",
        participantCountGoal = 34,
        description = "description",
        tipsDescription = "tipsDescription",
        thematique = mock(Thematique::class.java),
        hasAnswered = false,
    )

    @Test
    fun `getConsultation - when has no consultationInfo - should return null`() {
        // Given
        given(consultationInfoRepository.getConsultation(consultationId = "consultationId")).willReturn(null)

        // When
        val result = useCase.getConsultation(consultationId = "consultationId", userId = "userId")

        // Then
        assertThat(result).isEqualTo(null)
        then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultationId")
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(consultationResponseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultation - when has consultationInfo but no thematique - should return null`() {
        // Given
        val consultationInfo = mock(ConsultationInfo::class.java).also {
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(consultationInfoRepository.getConsultation(consultationId = "consultationId")).willReturn(consultationInfo)
        given(thematiqueRepository.getThematique(thematiqueId = "thematiqueId")).willReturn(null)

        // When
        val result = useCase.getConsultation(consultationId = "consultationId", userId = "userId")

        // Then
        assertThat(result).isEqualTo(null)
        then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultationId")
        then(thematiqueRepository).should(only()).getThematique(thematiqueId = "thematiqueId")
        then(consultationResponseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultation - when has consultationInfo and thematique - should return Consultation`() {
        // Given
        given(consultationInfoRepository.getConsultation(consultationId = "consultationId")).willReturn(consultationInfo)

        val thematique = mock(Thematique::class.java)
        given(thematiqueRepository.getThematique(thematiqueId = "thematiqueId")).willReturn(thematique)

        given(
            consultationResponseRepository.hasAnsweredConsultation(
                consultationId = "consultationId",
                userId = "userId",
            )
        ).willReturn(true)

        // When
        val result = useCase.getConsultation(consultationId = "consultationId", userId = "userId")

        // Then
        assertThat(result).isEqualTo(consultation.copy(thematique = thematique, hasAnswered = true))
        then(consultationInfoRepository).should(only()).getConsultation(consultationId = "consultationId")
        then(thematiqueRepository).should(only()).getThematique(thematiqueId = "thematiqueId")
        then(consultationResponseRepository).should(only())
            .hasAnsweredConsultation(consultationId = "consultationId", userId = "userId")
    }

}