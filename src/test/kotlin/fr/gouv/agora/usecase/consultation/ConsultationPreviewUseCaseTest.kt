package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.domain.*
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.profile.repository.ProfileRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.ArgumentMatchers.anyList
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
internal class ConsultationPreviewUseCaseTest {

    private lateinit var useCase: ConsultationPreviewUseCase

    private val consultationInfoRepository: ConsultationInfoRepository = mock(ConsultationInfoRepository::class.java)
    private val authentificationHelper: AuthentificationHelper = mock(AuthentificationHelper::class.java)
    private val profileRepository: ProfileRepository = mock(ProfileRepository::class.java)

    companion object {
        @JvmStatic
        fun getConsultationPreviewPageCases() = arrayOf(
            input(
                whenTestDescription = "utilisateur connecté, peut voir les non publiées",
                userId = "user123",
                canViewUnpublished = true,
                answeredConsultations = listOf(mockConsultationPreviewFinished(id = "answered1", isPublished = true)),
                ongoingConsultations = listOf(
                    mockConsultationPreview(id = "ongoing1", endDate = LocalDateTime.now().plusDays(10), isPublished = true),
                    mockConsultationPreview(id = "ongoing2", endDate = LocalDateTime.now().plusDays(5), isPublished = true),
                    mockConsultationPreview(id = "ongoingUnpublished", endDate = LocalDateTime.now().plusDays(7), isPublished = false),
                ),
                finishedConsultations = listOf(
                    mockConsultationPreviewFinished(id = "finished1", isPublished = true),
                    mockConsultationPreviewFinished(id = "finishedUnpublished", isPublished = false),
                ),
                expectedOngoingIds = listOf("ongoing2", "ongoingUnpublished", "ongoing1"),
                expectedFinishedIds = listOf("finished1", "finishedUnpublished"),
                expectedAnsweredIds = listOf("answered1"),
            ),
            input(
                whenTestDescription = "utilisateur connecté, ne peut voir que les publiées",
                userId = "user123",
                canViewUnpublished = false,
                answeredConsultations = listOf(mockConsultationPreviewFinished(id = "answered1", isPublished = true)),
                ongoingConsultations = listOf(
                    mockConsultationPreview(id = "ongoing1", endDate = LocalDateTime.now().plusDays(10), isPublished = true),
                    mockConsultationPreview(id = "ongoing2", endDate = LocalDateTime.now().plusDays(5), isPublished = true),
                    mockConsultationPreview(id = "ongoingUnpublished", endDate = LocalDateTime.now().plusDays(7), isPublished = false),
                ),
                finishedConsultations = listOf(
                    mockConsultationPreviewFinished(id = "finished1", isPublished = true),
                    mockConsultationPreviewFinished(id = "finishedUnpublished", isPublished = false),
                ),
                expectedOngoingIds = listOf("ongoing2", "ongoing1"),
                expectedFinishedIds = listOf("finished1"),
                expectedAnsweredIds = listOf("answered1"),
            ),
            input(
                whenTestDescription = "utilisateur non connecté - should return empty answeredList",
                userId = null,
                canViewUnpublished = false,
                answeredConsultations = emptyList(),
                ongoingConsultations = emptyList(),
                finishedConsultations = emptyList(),
                expectedOngoingIds = emptyList(),
                expectedFinishedIds = emptyList(),
                expectedAnsweredIds = emptyList(),
            ),
        )

        private fun input(
            whenTestDescription: String,
            userId: String?,
            canViewUnpublished: Boolean,
            answeredConsultations: List<ConsultationPreviewFinished>,
            ongoingConsultations: List<ConsultationPreview>,
            finishedConsultations: List<ConsultationPreviewFinished>,
            expectedOngoingIds: List<String>,
            expectedFinishedIds: List<String>,
            expectedAnsweredIds: List<String>,
        ) = arrayOf(
            whenTestDescription,
            userId,
            canViewUnpublished,
            answeredConsultations,
            ongoingConsultations,
            finishedConsultations,
            expectedOngoingIds,
            expectedFinishedIds,
            expectedAnsweredIds
        )

        private fun mockConsultationPreview(
            id: String,
            endDate: LocalDateTime = LocalDateTime.now(),
            isPublished: Boolean = true,
        ) = ConsultationPreview(
            id = id,
            slug = "slug-$id",
            title = "title-$id",
            coverUrl = "cover-$id",
            thematique = Thematique(id = "thematiqueId", label = "label", picto = "picto"),
            endDate = endDate,
            isPublished = isPublished,
            territory = "France",
        )

        private fun mockConsultationPreviewFinished(
            id: String,
            isPublished: Boolean = true,
        ) = ConsultationPreviewFinished(
            id = id,
            slug = "slug-$id",
            title = "title-$id",
            coverUrl = "cover-$id",
            thematique = Thematique(id = "thematiqueId", label = "label", picto = "picto"),
            updateLabel = "updateLabel",
            lastUpdateDate = LocalDateTime.now(),
            endDate = LocalDateTime.now().minusDays(1),
            isPublished = isPublished,
            territory = "France",
        )
    }

    @ParameterizedTest(name = "getConsultationPreviewPage - when {0} - should return expected page")
    @MethodSource("getConsultationPreviewPageCases")
    fun `getConsultationPreviewPage - should return expected page`(
        @Suppress("UNUSED_PARAMETER")
        whenTestDescription: String,
        userId: String?,
        canViewUnpublished: Boolean,
        answeredConsultations: List<ConsultationPreviewFinished>,
        ongoingConsultations: List<ConsultationPreview>,
        finishedConsultations: List<ConsultationPreviewFinished>,
        expectedOngoingIds: List<String>,
        expectedFinishedIds: List<String>,
        expectedAnsweredIds: List<String>,
    ) {
        // Given
        useCase = ConsultationPreviewUseCase(
            consultationInfoRepository = consultationInfoRepository,
            authentificationHelper = authentificationHelper,
            profileRepository = profileRepository,
        )
        given(authentificationHelper.getUserId()).willReturn(userId)
        given(authentificationHelper.canViewUnpublishedConsultations()).willReturn(canViewUnpublished)
        if (userId != null) {
            given(profileRepository.getProfile(userId)).willReturn(null)
            given(consultationInfoRepository.getAnsweredConsultations(userId)).willReturn(answeredConsultations)
        }
        given(consultationInfoRepository.getOngoingConsultationsWithUnpublished(anyList())).willReturn(ongoingConsultations)
        given(consultationInfoRepository.getFinishedConsultationsWithUnpublished(anyList())).willReturn(finishedConsultations)

        // When
        val result = useCase.getConsultationPreviewPage()

        // Then
        assertThat(result.ongoingList.map { it.id }).containsExactlyElementsOf(expectedOngoingIds)
        assertThat(result.finishedList.map { it.id }).containsExactlyElementsOf(expectedFinishedIds)
        assertThat(result.answeredList.map { it.id }).containsExactlyElementsOf(expectedAnsweredIds)
    }
}