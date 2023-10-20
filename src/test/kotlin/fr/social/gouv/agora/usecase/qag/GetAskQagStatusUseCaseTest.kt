package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.TestUtils
import fr.social.gouv.agora.domain.AgoraFeature
import fr.social.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.social.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime
import java.time.Month
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class GetAskQagStatusUseCaseTest {

    @Autowired
    private lateinit var useCase: GetAskQagStatusUseCase

    @MockBean
    private lateinit var qagInfoRepository: QagInfoRepository

    @MockBean
    private lateinit var featureFlagsRepository: FeatureFlagsRepository

    private val userId = "userId"

    companion object {

        @JvmStatic
        fun getAskQagStatusDateTestCases() = arrayOf(
            input(
                testWhenDescription = "qagPostDate < serverDate, qagPostDate is Monday week N and serverDate is Tuesday week N",
                qagPostDate = LocalDateTime.of(2023, Month.MAY, 1, 0, 0, 0),
                serverDate = LocalDateTime.of(2023, Month.MAY, 2, 12, 30, 0),
                expectedStatus = AskQagStatus.WEEKLY_LIMIT_REACHED,
            ),
            input(
                testWhenDescription = "qagPostDate < serverDate, qagPostDate is Thursday week N and serverDate is Friday week N",
                qagPostDate = LocalDateTime.of(2023, Month.MAY, 4, 0, 0, 0),
                serverDate = LocalDateTime.of(2023, Month.MAY, 5, 12, 30, 0),
                expectedStatus = AskQagStatus.WEEKLY_LIMIT_REACHED,
            ),
            input(
                testWhenDescription = "qagPostDate < serverDate, qagPostDate is Tuesday week N and serverDate is Thursday week N",
                qagPostDate = LocalDateTime.of(2023, Month.MAY, 2, 0, 0, 0),
                serverDate = LocalDateTime.of(2023, Month.MAY, 4, 12, 30, 0),
                expectedStatus = AskQagStatus.ENABLED,
            ),
            input(
                testWhenDescription = "qagPostDate < serverDate, qagPostDate is Friday week N and serverDate is Monday week N+1",
                qagPostDate = LocalDateTime.of(2023, Month.MAY, 5, 0, 0, 0),
                serverDate = LocalDateTime.of(2023, Month.MAY, 8, 12, 30, 0),
                expectedStatus = AskQagStatus.WEEKLY_LIMIT_REACHED,
            ),
            input(
                testWhenDescription = "qagPostDate < serverDate, qagPostDate is 1 second before weekly reset and serverDate is at weekly reset",
                qagPostDate = LocalDateTime.of(2023, Month.MAY, 2, 13, 59, 59),
                serverDate = LocalDateTime.of(2023, Month.MAY, 2, 14, 0, 0),
                expectedStatus = AskQagStatus.ENABLED,
            ),
            input(
                testWhenDescription = "qagPostDate = serverDate, qagPostDate at a random time",
                qagPostDate = LocalDateTime.of(2023, Month.MAY, 4, 12, 0, 0),
                serverDate = LocalDateTime.of(2023, Month.MAY, 4, 12, 0, 0),
                expectedStatus = AskQagStatus.WEEKLY_LIMIT_REACHED,
            ),
            input(
                testWhenDescription = "qagPostDate = serverDate, qagPostDate is at weekly reset and serverDate is at weekly reset",
                qagPostDate = LocalDateTime.of(2023, Month.MAY, 3, 14, 0, 0),
                serverDate = LocalDateTime.of(2023, Month.MAY, 3, 14, 0, 0),
                expectedStatus = AskQagStatus.WEEKLY_LIMIT_REACHED,
            ),
            input(
                testWhenDescription = "qagPostDate is week + 1 after serverDate",
                qagPostDate = LocalDateTime.of(2023, Month.MAY, 8, 14, 0, 0),
                serverDate = LocalDateTime.of(2023, Month.MAY, 1, 14, 0, 0),
                expectedStatus = AskQagStatus.ENABLED,
            ),
            input(
                testWhenDescription = "qagPostDate is after serverDate but same week",
                qagPostDate = LocalDateTime.of(2023, Month.MAY, 2, 12, 0, 0),
                serverDate = LocalDateTime.of(2023, Month.MAY, 1, 14, 0, 0),
                expectedStatus = AskQagStatus.WEEKLY_LIMIT_REACHED,
            ),
        )

        private fun input(
            testWhenDescription: String,
            qagPostDate: LocalDateTime,
            serverDate: LocalDateTime,
            expectedStatus: AskQagStatus,
        ) = arrayOf(testWhenDescription, qagPostDate, serverDate, expectedStatus)

    }

    @Test
    fun `getAskQagStatus - when feature disabled - should return DISABLED`() {
        // Given
        given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.AskQuestion)).willReturn(false)

        // When
        val result = useCase.getAskQagStatus(userId)

        // Then
        assertThat(result).isEqualTo(AskQagStatus.FEATURE_DISABLED)
        then(qagInfoRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getAskQagStatus - when feature enabled and user didn't have Qag - should return ENABLED`() {
        // Given
        given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.AskQuestion)).willReturn(true)
        given(qagInfoRepository.getUserQagInfoList(userId = userId, thematiqueId = null)).willReturn(emptyList())

        // When
        val result = useCase.getAskQagStatus(userId)

        // Then
        assertThat(result).isEqualTo(AskQagStatus.ENABLED)
        then(qagInfoRepository).should(only()).getUserQagInfoList(userId = userId, thematiqueId = null)
    }

    @ParameterizedTest(name = "getAskQagStatus - when {0} - should return {3}")
    @MethodSource("getAskQagStatusDateTestCases")
    fun `getAskQagStatus - should return expected`(
        testWhenDescription: String,
        qagPostDate: LocalDateTime,
        serverDate: LocalDateTime,
        expectedStatus: AskQagStatus,
    ) {
        useCase = GetAskQagStatusUseCase(
            qagInfoRepository = qagInfoRepository,
            featureFlagsRepository = featureFlagsRepository,
            clock = TestUtils.getFixedClock(serverDate),
        )

        given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.AskQuestion)).willReturn(true)

        val qagInfo = mock(QagInfo::class.java).also {
            given(it.date).willReturn(qagPostDate.toDate())
            given(it.userId).willReturn(userId)
        }
        given(qagInfoRepository.getUserQagInfoList(userId = userId, thematiqueId = null)).willReturn(listOf(qagInfo))

        // When
        val result = useCase.getAskQagStatus(userId)

        // Then
        assertThat(result).isEqualTo(expectedStatus)
        then(qagInfoRepository).should(only()).getUserQagInfoList(userId = userId, thematiqueId = null)
    }

}






