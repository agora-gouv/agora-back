package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.domain.*
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.profile.repository.ProfileRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyList
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
internal class ConsultationPreviewUseCaseTest {

    private lateinit var useCase: ConsultationPreviewUseCase

    @Mock
    private lateinit var consultationInfoRepository: ConsultationInfoRepository

    @Mock
    private lateinit var authentificationHelper: AuthentificationHelper

    @Mock
    private lateinit var profileRepository: ProfileRepository

    @BeforeEach
    fun setUp() {
        useCase = ConsultationPreviewUseCase(
            consultationInfoRepository = consultationInfoRepository,
            authentificationHelper = authentificationHelper,
            profileRepository = profileRepository,
        )
    }

    @Nested
    inner class `getConsultationPreviewPage - when canViewUnpublishedConsultations is true` {

        @BeforeEach
        fun setUp() {
            given(authentificationHelper.canViewUnpublishedConsultations()).willReturn(true)
        }

        @Test
        fun `getConsultationPreviewPage - when canViewUnpublishedConsultations - should use WithUnpublished queries`() {
            // Given
            given(authentificationHelper.getUserId()).willReturn(null)
            given(consultationInfoRepository.getOngoingConsultationsWithUnpublished(anyList())).willReturn(emptyList())
            given(consultationInfoRepository.getFinishedConsultationsWithUnpublished(anyList())).willReturn(emptyList())

            // When
            useCase.getConsultationPreviewPage()

            // Then
            then(consultationInfoRepository).should().getOngoingConsultationsWithUnpublished(anyList())
            then(consultationInfoRepository).should().getFinishedConsultationsWithUnpublished(anyList())
            then(consultationInfoRepository).shouldHaveNoMoreInteractions()
        }

        @Test
        fun `getConsultationPreviewPage - when canViewUnpublishedConsultations - should NOT use Published-only queries`() {
            // Given
            given(authentificationHelper.getUserId()).willReturn(null)
            given(consultationInfoRepository.getOngoingConsultationsWithUnpublished(anyList())).willReturn(emptyList())
            given(consultationInfoRepository.getFinishedConsultationsWithUnpublished(anyList())).willReturn(emptyList())

            // When
            useCase.getConsultationPreviewPage()

            // Then
            then(consultationInfoRepository).should().getOngoingConsultationsWithUnpublished(anyList())
            then(consultationInfoRepository).should().getFinishedConsultationsWithUnpublished(anyList())
            then(consultationInfoRepository).shouldHaveNoMoreInteractions()
        }

        @Test
        fun `getConsultationPreviewPage - when canViewUnpublishedConsultations - should return all consultations including unpublished`() {
            // Given
            val ongoing1 = mockConsultationPreview(id = "ongoing1", endDate = LocalDateTime.now().plusDays(10))
            val ongoingDraft = mockConsultationPreview(id = "ongoingDraft", endDate = LocalDateTime.now().plusDays(5))
            val finished1 = mockConsultationPreviewFinished(id = "finished1")
            val finishedDraft = mockConsultationPreviewFinished(id = "finishedDraft")

            given(authentificationHelper.getUserId()).willReturn(null)
            given(consultationInfoRepository.getOngoingConsultationsWithUnpublished(anyList()))
                .willReturn(listOf(ongoing1, ongoingDraft))
            given(consultationInfoRepository.getFinishedConsultationsWithUnpublished(anyList()))
                .willReturn(listOf(finished1, finishedDraft))

            // When
            val result = useCase.getConsultationPreviewPage()

            // Then
            assertThat(result.ongoingList.map { it.id }).containsExactlyInAnyOrder("ongoing1", "ongoingDraft")
            assertThat(result.finishedList.map { it.id }).containsExactlyInAnyOrder("finished1", "finishedDraft")
        }

        @Test
        fun `getConsultationPreviewPage - when user connected and canViewUnpublishedConsultations - should remove answered from ongoing`() {
            // Given
            val userId = "user123"
            val answeredConsultation = mockConsultationPreviewFinished(id = "answered1")
            val ongoingNotAnswered = mockConsultationPreview(id = "ongoing1", endDate = LocalDateTime.now().plusDays(10))
            val ongoingAnswered = mockConsultationPreview(id = "answered1", endDate = LocalDateTime.now().plusDays(5))

            given(authentificationHelper.getUserId()).willReturn(userId)
            given(profileRepository.getProfile(userId)).willReturn(null)
            given(consultationInfoRepository.getAnsweredConsultations(userId)).willReturn(listOf(answeredConsultation))
            given(consultationInfoRepository.getOngoingConsultationsWithUnpublished(anyList()))
                .willReturn(listOf(ongoingNotAnswered, ongoingAnswered))
            given(consultationInfoRepository.getFinishedConsultationsWithUnpublished(anyList())).willReturn(emptyList())

            // When
            val result = useCase.getConsultationPreviewPage()

            // Then
            assertThat(result.ongoingList.map { it.id }).containsExactly("ongoing1")
            assertThat(result.answeredList.map { it.id }).containsExactly("answered1")
        }

        @Test
        fun `getConsultationPreviewPage - when canViewUnpublishedConsultations - should sort ongoing by endDate asc`() {
            // Given
            val now = LocalDateTime.now()
            val ongoing1 = mockConsultationPreview(id = "ongoing1", endDate = now.plusDays(10))
            val ongoing2 = mockConsultationPreview(id = "ongoing2", endDate = now.plusDays(5))
            val ongoing3 = mockConsultationPreview(id = "ongoing3", endDate = now.plusDays(7))

            given(authentificationHelper.getUserId()).willReturn(null)
            given(consultationInfoRepository.getOngoingConsultationsWithUnpublished(anyList()))
                .willReturn(listOf(ongoing1, ongoing2, ongoing3))
            given(consultationInfoRepository.getFinishedConsultationsWithUnpublished(anyList())).willReturn(emptyList())

            // When
            val result = useCase.getConsultationPreviewPage()

            // Then
            assertThat(result.ongoingList.map { it.id }).containsExactly("ongoing2", "ongoing3", "ongoing1")
        }
    }

    @Nested
    inner class `getConsultationPreviewPage - when canViewUnpublishedConsultations is false` {

        @BeforeEach
        fun setUp() {
            given(authentificationHelper.canViewUnpublishedConsultations()).willReturn(false)
        }

        @Test
        fun `getConsultationPreviewPage - when cannot view unpublished - should use Published-only queries`() {
            // Given
            given(authentificationHelper.getUserId()).willReturn(null)
            given(consultationInfoRepository.getOngoingConsultations(anyList())).willReturn(emptyList())
            given(consultationInfoRepository.getFinishedConsultations(anyList())).willReturn(emptyList())

            // When
            useCase.getConsultationPreviewPage()

            // Then
            then(consultationInfoRepository).should().getOngoingConsultations(anyList())
            then(consultationInfoRepository).should().getFinishedConsultations(anyList())
            then(consultationInfoRepository).shouldHaveNoMoreInteractions()
        }

        @Test
        fun `getConsultationPreviewPage - when cannot view unpublished - should NOT use WithUnpublished queries`() {
            // Given
            given(authentificationHelper.getUserId()).willReturn(null)
            given(consultationInfoRepository.getOngoingConsultations(anyList())).willReturn(emptyList())
            given(consultationInfoRepository.getFinishedConsultations(anyList())).willReturn(emptyList())

            // When
            useCase.getConsultationPreviewPage()

            // Then
            then(consultationInfoRepository).should().getOngoingConsultations(anyList())
            then(consultationInfoRepository).should().getFinishedConsultations(anyList())
            then(consultationInfoRepository).shouldHaveNoMoreInteractions()
        }

        @Test
        fun `getConsultationPreviewPage - when cannot view unpublished - should return only published consultations`() {
            // Given
            val publishedOngoing = mockConsultationPreview(id = "ongoing1", endDate = LocalDateTime.now().plusDays(10))
            val publishedFinished = mockConsultationPreviewFinished(id = "finished1")

            given(authentificationHelper.getUserId()).willReturn(null)
            given(consultationInfoRepository.getOngoingConsultations(anyList()))
                .willReturn(listOf(publishedOngoing))
            given(consultationInfoRepository.getFinishedConsultations(anyList()))
                .willReturn(listOf(publishedFinished))

            // When
            val result = useCase.getConsultationPreviewPage()

            // Then
            assertThat(result.ongoingList.map { it.id }).containsExactly("ongoing1")
            assertThat(result.finishedList.map { it.id }).containsExactly("finished1")
        }

        @Test
        fun `getConsultationPreviewPage - when user connected and cannot view unpublished - should remove answered from ongoing`() {
            // Given
            val userId = "user123"
            val answeredConsultation = mockConsultationPreviewFinished(id = "answered1")
            val ongoingNotAnswered = mockConsultationPreview(id = "ongoing1", endDate = LocalDateTime.now().plusDays(10))
            val ongoingAnswered = mockConsultationPreview(id = "answered1", endDate = LocalDateTime.now().plusDays(5))

            given(authentificationHelper.getUserId()).willReturn(userId)
            given(profileRepository.getProfile(userId)).willReturn(null)
            given(consultationInfoRepository.getAnsweredConsultations(userId)).willReturn(listOf(answeredConsultation))
            given(consultationInfoRepository.getOngoingConsultations(anyList()))
                .willReturn(listOf(ongoingNotAnswered, ongoingAnswered))
            given(consultationInfoRepository.getFinishedConsultations(anyList())).willReturn(emptyList())

            // When
            val result = useCase.getConsultationPreviewPage()

            // Then
            assertThat(result.ongoingList.map { it.id }).containsExactly("ongoing1")
            assertThat(result.answeredList.map { it.id }).containsExactly("answered1")
        }

        @Test
        fun `getConsultationPreviewPage - when cannot view unpublished - should sort ongoing by endDate asc`() {
            // Given
            val now = LocalDateTime.now()
            val ongoing1 = mockConsultationPreview(id = "ongoing1", endDate = now.plusDays(10))
            val ongoing2 = mockConsultationPreview(id = "ongoing2", endDate = now.plusDays(5))
            val ongoing3 = mockConsultationPreview(id = "ongoing3", endDate = now.plusDays(7))

            given(authentificationHelper.getUserId()).willReturn(null)
            given(consultationInfoRepository.getOngoingConsultations(anyList()))
                .willReturn(listOf(ongoing1, ongoing2, ongoing3))
            given(consultationInfoRepository.getFinishedConsultations(anyList())).willReturn(emptyList())

            // When
            val result = useCase.getConsultationPreviewPage()

            // Then
            assertThat(result.ongoingList.map { it.id }).containsExactly("ongoing2", "ongoing3", "ongoing1")
        }
    }

    @Nested
    inner class `getConsultationPreviewPage - user not connected` {

        @Test
        fun `getConsultationPreviewPage - when user not connected - should return empty answeredList`() {
            // Given
            given(authentificationHelper.getUserId()).willReturn(null)
            given(authentificationHelper.canViewUnpublishedConsultations()).willReturn(false)
            given(consultationInfoRepository.getOngoingConsultations(anyList())).willReturn(emptyList())
            given(consultationInfoRepository.getFinishedConsultations(anyList())).willReturn(emptyList())

            // When
            val result = useCase.getConsultationPreviewPage()

            // Then
            assertThat(result.answeredList).isEmpty()
            then(consultationInfoRepository).should().getOngoingConsultations(anyList())
            then(consultationInfoRepository).should().getFinishedConsultations(anyList())
            then(consultationInfoRepository).shouldHaveNoMoreInteractions()
        }
    }

    companion object {
        private fun mockConsultationPreview(
            id: String,
            endDate: LocalDateTime = LocalDateTime.now(),
        ) = ConsultationPreview(
            id = id,
            slug = "slug-$id",
            title = "title-$id",
            coverUrl = "cover-$id",
            thematique = Thematique(id = "thematiqueId", label = "label", picto = "picto"),
            endDate = endDate,
            territory = "France",
        )

        private fun mockConsultationPreviewFinished(
            id: String,
        ) = ConsultationPreviewFinished(
            id = id,
            slug = "slug-$id",
            title = "title-$id",
            coverUrl = "cover-$id",
            thematique = Thematique(id = "thematiqueId", label = "label", picto = "picto"),
            updateLabel = "updateLabel",
            lastUpdateDate = LocalDateTime.now(),
            endDate = LocalDateTime.now().minusDays(1),
            territory = "France",
        )
    }
}
