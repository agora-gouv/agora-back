package fr.gouv.agora.usecase.qagArchive

import fr.gouv.agora.TestUtils
import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.only
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.time.Month

@ExtendWith(MockitoExtension::class)
internal class AnonymizeOldQagUseCaseTest {

    private lateinit var useCase: AnonymizeOldQagUseCase

    @Mock
    private lateinit var featureFlagsRepository: FeatureFlagsRepository

    @Mock
    private lateinit var qagInfoRepository: QagInfoRepository

    @Nested
    inner class `anonymizeOldQag - when feature disabled` {

        @Test
        fun `should do nothing and return FAILURE`() {
            // Given
            given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.QagAnonymization)).willReturn(false)
            mockDate(LocalDateTime.of(2024, Month.JANUARY, 4, 16, 0, 0))

            // When
            val result = useCase.anonymizeOldQag()

            // Then
            assertThat(result).isEqualTo(AnonymizeQagListResult.FAILURE)
            then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.QagAnonymization)
            then(qagInfoRepository).shouldHaveNoInteractions()
        }
    }

    @Nested
    inner class `anonymizeOldQag - when feature enabled` {

        @Test
        fun `should call repository with date 3 weeks ago from now and return SUCCESS`() {
            // Given
            given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.QagAnonymization)).willReturn(true)
            // Today: Thursday January 4th 2024, 16h00 => anonymize date: Thursday December 14th 2023, 16h00
            val todayDate = LocalDateTime.of(2024, Month.JANUARY, 4, 16, 0, 0)
            val expectedAnonymizeDate = LocalDateTime.of(2023, Month.DECEMBER, 14, 16, 0, 0)
            mockDate(todayDate)

            // When
            val result = useCase.anonymizeOldQag()

            // Then
            assertThat(result).isEqualTo(AnonymizeQagListResult.SUCCESS)
            then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.QagAnonymization)
            then(qagInfoRepository).should(only()).anonymizeOldQags(expectedAnonymizeDate.toDate())
        }

        @Test
        fun `should keep exact time when computing 3 weeks ago`() {
            // Given
            given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.QagAnonymization)).willReturn(true)
            // Today: Monday January 1st 2024, 10h30 => anonymize date: Monday December 11th 2023, 10h30
            val todayDate = LocalDateTime.of(2024, Month.JANUARY, 1, 10, 30, 0)
            val expectedAnonymizeDate = LocalDateTime.of(2023, Month.DECEMBER, 11, 10, 30, 0)
            mockDate(todayDate)

            // When
            val result = useCase.anonymizeOldQag()

            // Then
            assertThat(result).isEqualTo(AnonymizeQagListResult.SUCCESS)
            then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.QagAnonymization)
            then(qagInfoRepository).should(only()).anonymizeOldQags(expectedAnonymizeDate.toDate())
        }
    }

    private fun mockDate(localDateTime: LocalDateTime) {
        useCase = AnonymizeOldQagUseCase(
            featureFlagsRepository = featureFlagsRepository,
            qagInfoRepository = qagInfoRepository,
            clock = TestUtils.getFixedClock(localDateTime),
        )
    }
}
