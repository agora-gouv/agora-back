package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.FeatureFlags
import fr.social.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.social.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.DayOfWeek
import java.time.LocalDate
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

    @Test
    fun `getGetQagErrorText - when feature disabled - should return DISABLED`() {
        // Given
        val featureFlags = mock(FeatureFlags::class.java).also {
            given(it.isAskQuestionEnabled).willReturn(false)
        }
        given(featureFlagsRepository.getFeatureFlags()).willReturn(featureFlags)

        // When
        val result = useCase.getAskQagStatus(userId)

        // Then
        assertThat(result).isEqualTo(AskQagStatus.FEATURE_DISABLED)
        then(qagInfoRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getGetQagErrorText - when feature enabled and user didn't have Qag - should return ENABLED`() {
        // Given
        val featureFlags = mock(FeatureFlags::class.java).also {
            given(it.isAskQuestionEnabled).willReturn(true)
        }
        given(featureFlagsRepository.getFeatureFlags()).willReturn(featureFlags)
        given(qagInfoRepository.getAllQagInfo()).willReturn(emptyList())

        // When
        val result = useCase.getAskQagStatus(userId)

        // Then
        assertThat(result).isEqualTo(AskQagStatus.ENABLED)
        then(qagInfoRepository).should(only()).getAllQagInfo()
    }

    @Test
    fun `getGetQagErrorText - when feature enabled and user have Qag within the week - should return WEEKLY_LIMIT_REACHED`() {
        // Given
        val featureFlags = mock(FeatureFlags::class.java).also {
            given(it.isAskQuestionEnabled).willReturn(true)
        }
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.date).willReturn(Calendar.getInstance().time)
            given(it.userId).willReturn(userId)
        }
        given(featureFlagsRepository.getFeatureFlags()).willReturn(featureFlags)
        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))

        // When
        val result = useCase.getAskQagStatus(userId)

        // Then
        assertThat(result).isEqualTo(AskQagStatus.WEEKLY_LIMIT_REACHED)
        then(qagInfoRepository).should(only()).getAllQagInfo()
    }

    @Test
    fun `getGetQagErrorText - when feature enabled and user have Qag before Monday of the current week - should return ENABLED`() {
        // Given
        val featureFlags = mock(FeatureFlags::class.java).also {
            given(it.isAskQuestionEnabled).willReturn(true)
        }
        val localDate = LocalDate.now().with(DayOfWeek.MONDAY).minusDays(1)
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.date).willReturn(localDate.toDate())
            given(it.userId).willReturn(userId)
        }
        given(featureFlagsRepository.getFeatureFlags()).willReturn(featureFlags)
        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))

        // When
        val result = useCase.getAskQagStatus(userId)

        // Then
        assertThat(result).isEqualTo(AskQagStatus.ENABLED)
        then(qagInfoRepository).should(only()).getAllQagInfo()
    }

    @Test
    fun `getGetQagErrorText - when feature enabled and user have Qag after Sunday of the current week - should return ENABLED`() {
        // Given
        val featureFlags = mock(FeatureFlags::class.java).also {
            given(it.isAskQuestionEnabled).willReturn(true)
        }
        val localDate = LocalDate.now().with(DayOfWeek.SUNDAY).plusDays(1)
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.date).willReturn(localDate.toDate())
            given(it.userId).willReturn(userId)
        }
        given(featureFlagsRepository.getFeatureFlags()).willReturn(featureFlags)
        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))

        // When
        val result = useCase.getAskQagStatus(userId)

        // Then
        assertThat(result).isEqualTo(AskQagStatus.ENABLED)
        then(qagInfoRepository).should(only()).getAllQagInfo()
    }
}