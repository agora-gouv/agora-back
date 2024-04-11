package fr.gouv.agora.usecase.suspiciousUser

import fr.gouv.agora.TestUtils
import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.SignupHistoryCount
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.login.repository.UserDataRepository
import fr.gouv.agora.usecase.suspiciousUser.repository.SignupCountRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

@SpringBootTest
class IsSuspiciousUserUseCaseTest {

    private lateinit var useCase: IsSuspiciousUserUseCase

    @MockBean
    private lateinit var featureFlagsRepository: FeatureFlagsRepository

    @MockBean
    private lateinit var signupCountRepository: SignupCountRepository

    @MockBean
    private lateinit var userDataRepository: UserDataRepository

    @BeforeEach
    fun setUp() {
        reset(featureFlagsRepository)
        useCase = IsSuspiciousUserUseCase(
            clock = TestUtils.getFixedClock(LocalDateTime.of(2024, Month.JANUARY, 1, 12, 30, 59)),
            featureFlagsRepository = featureFlagsRepository,
            signupCountRepository = signupCountRepository,
            userDataRepository = userDataRepository,
        )
        given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.SuspiciousUserDetection)).willReturn(true)
    }

    @Test
    fun `isSuspiciousUser - when feature is disabled - should return false`() {
        // Given
        given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.SuspiciousUserDetection)).willReturn(false)

        // When
        val result = useCase.isSuspiciousUser(ipAddressHash = "ipHash")

        // Then
        assertThat(result).isEqualTo(false)
        then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.SuspiciousUserDetection)
        then(signupCountRepository).shouldHaveNoInteractions()
        then(userDataRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `isSuspiciousUser - when getTodaySignupCount is lower than 10 - should return false`() {
        // Given
        given(signupCountRepository.getTodaySignupCount(ipAddressHash = "ipHash")).willReturn(8)

        // When
        val result = useCase.isSuspiciousUser(ipAddressHash = "ipHash")

        // Then
        assertThat(result).isEqualTo(false)
        then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.SuspiciousUserDetection)
        then(signupCountRepository).should(only()).getTodaySignupCount(ipAddressHash = "ipHash")
        then(userDataRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `isSuspiciousUser - when todaySignupCount is greater or equal 10 - should return true`() {
        // Given
        given(signupCountRepository.getTodaySignupCount(ipAddressHash = "ipHash")).willReturn(10)

        // When
        val result = useCase.isSuspiciousUser(ipAddressHash = "ipHash")

        // Then
        assertThat(result).isEqualTo(true)
        then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.SuspiciousUserDetection)
        then(signupCountRepository).should(only()).getTodaySignupCount(ipAddressHash = "ipHash")
        then(userDataRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `isSuspiciousUser - when getTodaySignupCount is null but has no SignupHistoryCount - should init todaySignupCount to 0 and return false`() {
        // Given
        given(signupCountRepository.getTodaySignupCount(ipAddressHash = "ipHash")).willReturn(null)
        given(userDataRepository.getSignupHistory(ipAddressHash = "ipHash")).willReturn(emptyList())

        // When
        val result = useCase.isSuspiciousUser(ipAddressHash = "ipHash")

        // Then
        assertThat(result).isEqualTo(false)
        then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.SuspiciousUserDetection)
        then(signupCountRepository).should().getTodaySignupCount(ipAddressHash = "ipHash")
        then(signupCountRepository).should().initTodaySignupCount(ipAddressHash = "ipHash", todaySignupCount = 0)
        then(userDataRepository).should(only()).getSignupHistory(ipAddressHash = "ipHash")
    }

    @Test
    fun `isSuspiciousUser - when getTodaySignupCount is null and has a SignupHistoryCount with count greater or equal 10 - should init todaySignupCount to count and return true`() {
        // Given
        given(signupCountRepository.getTodaySignupCount(ipAddressHash = "ipHash")).willReturn(null)
        given(userDataRepository.getSignupHistory(ipAddressHash = "ipHash")).willReturn(
            listOf(
                SignupHistoryCount(
                    date = LocalDate.MIN,
                    signupCount = 14,
                )
            )
        )

        // When
        val result = useCase.isSuspiciousUser(ipAddressHash = "ipHash")

        // Then
        assertThat(result).isEqualTo(true)
        then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.SuspiciousUserDetection)
        then(signupCountRepository).should().getTodaySignupCount(ipAddressHash = "ipHash")
        then(signupCountRepository).should().initTodaySignupCount(ipAddressHash = "ipHash", todaySignupCount = 14)
        then(userDataRepository).should(only()).getSignupHistory(ipAddressHash = "ipHash")
    }

    @Test
    fun `isSuspiciousUser - when getTodaySignupCount is null and has no SignupHistoryCount with count greater or equal 10 but has an history entry for today - should init todaySignupCount to this entry and return false`() {
        // Given
        useCase = IsSuspiciousUserUseCase(
            clock = TestUtils.getFixedClock(LocalDateTime.of(2024, Month.JANUARY, 15, 12, 30, 59)),
            featureFlagsRepository = featureFlagsRepository,
            signupCountRepository = signupCountRepository,
            userDataRepository = userDataRepository,
        )
        given(signupCountRepository.getTodaySignupCount(ipAddressHash = "ipHash")).willReturn(null)
        given(userDataRepository.getSignupHistory(ipAddressHash = "ipHash")).willReturn(
            listOf(
                SignupHistoryCount(
                    date = LocalDate.of(2024, Month.JANUARY, 15),
                    signupCount = 8,
                )
            )
        )

        // When
        val result = useCase.isSuspiciousUser(ipAddressHash = "ipHash")

        // Then
        assertThat(result).isEqualTo(false)
        then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.SuspiciousUserDetection)
        then(signupCountRepository).should().getTodaySignupCount(ipAddressHash = "ipHash")
        then(signupCountRepository).should().initTodaySignupCount(ipAddressHash = "ipHash", todaySignupCount = 8)
        then(userDataRepository).should(only()).getSignupHistory(ipAddressHash = "ipHash")
    }

    @Test
    fun `isSuspiciousUser - when feature is disabled - should do nothing`() {
        // Given
        given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.SuspiciousUserDetection)).willReturn(false)

        // When
        useCase.notifySignup(ipAddressHash = "ipHash")

        // Then
        then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.SuspiciousUserDetection)
        then(signupCountRepository).shouldHaveNoInteractions()
        then(userDataRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `notifySignup - when getTodaySignupCount is null - should do nothing`() {
        // Given
        given(signupCountRepository.getTodaySignupCount(ipAddressHash = "ipHash")).willReturn(null)

        // When
        useCase.notifySignup(ipAddressHash = "ipHash")

        // Then
        then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.SuspiciousUserDetection)
        then(signupCountRepository).should(only()).getTodaySignupCount(ipAddressHash = "ipHash")
        then(userDataRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `notifySignup - when getTodaySignupCount is not null - should init with value + 1`() {
        // Given
        given(signupCountRepository.getTodaySignupCount(ipAddressHash = "ipHash")).willReturn(19)

        // When
        useCase.notifySignup(ipAddressHash = "ipHash")

        // Then
        then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.SuspiciousUserDetection)
        then(signupCountRepository).should().getTodaySignupCount(ipAddressHash = "ipHash")
        then(signupCountRepository).should().initTodaySignupCount(ipAddressHash = "ipHash", todaySignupCount = 20)
        then(userDataRepository).shouldHaveNoInteractions()
    }

}