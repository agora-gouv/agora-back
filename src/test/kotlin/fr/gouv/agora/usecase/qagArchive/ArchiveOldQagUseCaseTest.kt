package fr.gouv.agora.usecase.qagArchive

import fr.gouv.agora.TestUtils
import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.qag.repository.AskQagStatusCacheRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qagUpdates.repository.QagUpdatesRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.*
import java.util.*

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

    @BeforeEach
    fun setUp() {
        reset(featureFlagsRepository)
    }

    @Test
    fun `archiveOldQag - when feature disabled - should do nothing and return FAILURE`() {
        // Given
        given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.QagArchive)).willReturn(false)
        mockDate(LocalDateTime.of(2023, Month.JUNE, 22, 16, 0, 0))

        // When
        val result = useCase.archiveOldQag()

        // Then
        assertThat(result).isEqualTo(ArchiveQagListResult.FAILURE)
        then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.QagArchive)
        then(qagInfoRepository).shouldHaveNoInteractions()
        then(qagUpdatesRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `archiveOldQag - when feature enabled and today is after tuesday from this week - should call repository with resetDate as tuesday before today at 14h then return SUCCESS`() {
        // Given
        given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.QagArchive)).willReturn(true)
        // Thursday June 22th, 16h00 => Tuesday this week is Tuesday June 20th, 14h00
        val todayDate = LocalDateTime.of(2023, Month.JUNE, 22, 16, 0, 0)
        val resetDate = LocalDateTime.of(2023, Month.JUNE, 20, 14, 0, 0)
        mockDate(todayDate)

        // When
        val result = useCase.archiveOldQag()

        // Then
        assertThat(result).isEqualTo(ArchiveQagListResult.SUCCESS)
        then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.QagArchive)
        then(qagInfoRepository).should(only()).archiveOldQags(resetDate.toDate())
    }

    @Test
    fun `archiveOldQag - when feature enabled and today is before reset date - should call repository with resetDate as tuesday after today at 14h then return SUCCESS`() {
        // Given
        given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.QagArchive)).willReturn(true)
        // Monday June 19th, 19h00 => Tuesday this week is Tuesday June 20th, 14h00
        val todayDate = LocalDateTime.of(2023, Month.JUNE, 19, 19, 0, 0)
        val resetDate = LocalDateTime.of(2023, Month.JUNE, 20, 14, 0, 0)
        mockDate(todayDate)

        // When
        val result = useCase.archiveOldQag()

        // Then
        assertThat(result).isEqualTo(ArchiveQagListResult.SUCCESS)
        then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.QagArchive)
        then(qagInfoRepository).should(only()).archiveOldQags(resetDate.toDate())
        then(askQagStatusCacheRepository).should(only()).clear()
    }

    private fun mockDate(localDateTime: LocalDateTime) {
        useCase = ArchiveOldQagUseCase(
            featureFlagsRepository = featureFlagsRepository,
            qagInfoRepository = qagInfoRepository,
            askQagStatusCacheRepository = askQagStatusCacheRepository,
            clock = TestUtils.getFixedClock(localDateTime),
        )
    }

}