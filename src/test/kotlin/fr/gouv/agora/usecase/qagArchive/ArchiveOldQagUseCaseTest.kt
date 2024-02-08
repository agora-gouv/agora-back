package fr.gouv.agora.usecase.qagArchive

import fr.gouv.agora.TestUtils
import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.qag.repository.AskQagStatusCacheRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagPreviewCacheRepository
import fr.gouv.agora.usecase.qagUpdates.repository.QagUpdatesRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class ArchiveOldQagUseCaseTest {

    private lateinit var useCase: ArchiveOldQagUseCase

    @MockBean
    private lateinit var featureFlagsRepository: FeatureFlagsRepository

    @MockBean
    private lateinit var qagInfoRepository: QagInfoRepository

    @MockBean
    private lateinit var qagUpdatesRepository: QagUpdatesRepository

    @MockBean
    private lateinit var askQagStatusCacheRepository: AskQagStatusCacheRepository

    @MockBean
    private lateinit var qagPreviewCacheRepository: QagPreviewCacheRepository

    @BeforeEach
    fun setUp() {
        reset(featureFlagsRepository)
    }

    @Test
    fun `archiveOldQag - when feature disabled - should do nothing and return FAILURE`() {
        // Given
        given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.QagArchive)).willReturn(false)
        mockDate(LocalDateTime.of(2024, Month.JANUARY, 4, 16, 0, 0))

        // When
        val result = useCase.archiveOldQag()

        // Then
        assertThat(result).isEqualTo(ArchiveQagListResult.FAILURE)
        then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.QagArchive)
        then(qagInfoRepository).shouldHaveNoInteractions()
        then(qagUpdatesRepository).shouldHaveNoInteractions()
        then(qagPreviewCacheRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `archiveOldQag - when feature enabled and today is after monday from this week - should call repository with resetDate as monday before today at 14h then return SUCCESS`() {
        // Given
        given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.QagArchive)).willReturn(true)
        // Thursday, Thursday 4th 2024, 16h00 => resetDate is Monday, January 1st, 14h00
        val todayDate = LocalDateTime.of(2024, Month.JANUARY, 4, 16, 0, 0)
        val resetDate = LocalDateTime.of(2024, Month.JANUARY, 1, 14, 0, 0)
        mockDate(todayDate)

        // When
        val result = useCase.archiveOldQag()

        // Then
        assertThat(result).isEqualTo(ArchiveQagListResult.SUCCESS)
        then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.QagArchive)
        then(qagInfoRepository).should(only()).archiveOldQags(resetDate.toDate())
        then(qagPreviewCacheRepository).should(only()).clear()
    }

    @Test
    fun `archiveOldQag - when feature enabled and today is before reset date - should call repository with resetDate as monday after today at 14h then return SUCCESS`() {
        // Given
        given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.QagArchive)).willReturn(true)
        // Monday, January 1st 2024, 10h00 => resetDate is Monday, January 1st, 14h00
        val todayDate = LocalDateTime.of(2024, Month.JANUARY, 1, 10, 0, 0)
        val resetDate = LocalDateTime.of(2024, Month.JANUARY, 1, 14, 0, 0)
        mockDate(todayDate)

        // When
        val result = useCase.archiveOldQag()

        // Then
        assertThat(result).isEqualTo(ArchiveQagListResult.SUCCESS)
        then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.QagArchive)
        then(qagInfoRepository).should(only()).archiveOldQags(resetDate.toDate())
        then(askQagStatusCacheRepository).should(only()).clear()
        then(qagPreviewCacheRepository).should(only()).clear()
    }

    private fun mockDate(localDateTime: LocalDateTime) {
        useCase = ArchiveOldQagUseCase(
            featureFlagsRepository = featureFlagsRepository,
            qagInfoRepository = qagInfoRepository,
            askQagStatusCacheRepository = askQagStatusCacheRepository,
            qagPreviewCacheRepository = qagPreviewCacheRepository,
            clock = TestUtils.getFixedClock(localDateTime),
        )
    }

}