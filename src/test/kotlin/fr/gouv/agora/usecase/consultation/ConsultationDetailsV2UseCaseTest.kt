package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.TestUtils
import fr.gouv.agora.domain.ConsultationUpdateInfoV2
import fr.gouv.agora.usecase.consultation.repository.ConsultationDetailsV2CacheRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationUpdateCacheResult
import fr.gouv.agora.usecase.consultationResponse.repository.UserAnsweredConsultationRepository
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateHistoryRepository
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateV2Repository
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.feedbackConsultationUpdate.repository.FeedbackConsultationUpdateRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Clock
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class ConsultationDetailsV2UseCaseTest {
    lateinit var consultationDetailsV2UseCase: ConsultationDetailsV2UseCase

    val clock: Clock = mock(Clock::class.java)

    val featureFlagsRepository: FeatureFlagsRepository = mock(FeatureFlagsRepository::class.java)

    val infoRepository: ConsultationInfoRepository = mock(ConsultationInfoRepository::class.java)

    val updateRepository: ConsultationUpdateV2Repository = mock(ConsultationUpdateV2Repository::class.java)

    val userAnsweredRepository: UserAnsweredConsultationRepository =
        mock(UserAnsweredConsultationRepository::class.java)

    val feedbackRepository: FeedbackConsultationUpdateRepository =
        mock(FeedbackConsultationUpdateRepository::class.java)

    val historyRepository: ConsultationUpdateHistoryRepository = mock(ConsultationUpdateHistoryRepository::class.java)

    val cacheRepository: ConsultationDetailsV2CacheRepository = mock(ConsultationDetailsV2CacheRepository::class.java)

    val now = LocalDateTime.of(2024, 12, 12, 12, 0)

    @BeforeEach
    fun setUp() {
        consultationDetailsV2UseCase = ConsultationDetailsV2UseCase(
            TestUtils.getFixedClock(now),
            featureFlagsRepository,
            infoRepository,
            updateRepository,
            userAnsweredRepository,
            feedbackRepository,
            historyRepository,
            cacheRepository,
        )
    }

    @Test
    fun `when everything is ok`() {
        // Given
        val consultationId = "chapi-chapo"
        val consultationInfo = mock(ConsultationInfo::class.java).also {
            given(it.id).willReturn(consultationId)
            given(it.endDate).willReturn(now.minusDays(2))
        }
        val consultationUpdateInfo = mock(ConsultationUpdateInfoV2::class.java)

        given(infoRepository.getConsultation(consultationId)).willReturn(consultationInfo)
        given(cacheRepository.getLastConsultationDetails(consultationId)).willReturn(ConsultationUpdateCacheResult.CacheNotInitialized)
        given(updateRepository.getUnansweredUsersConsultationUpdate(consultationId)).willReturn(consultationUpdateInfo)
        given(updateRepository.getLatestConsultationUpdate(consultationId)).willReturn(consultationUpdateInfo)

        // When
        val actual = consultationDetailsV2UseCase.getConsultation(consultationId, "chapo")

        // Then
        assertEquals(consultationInfo, actual)
    }
}
