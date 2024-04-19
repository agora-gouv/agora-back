package fr.gouv.agora.usecase.qagSelection

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import fr.gouv.agora.usecase.responseQag.repository.ResponseQagPreviewCacheRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class SelectMostPopularQagUseCaseTest {

    @InjectMocks
    private lateinit var useCase: SelectMostPopularQagUseCase

    @Mock
    private lateinit var featureFlagsRepository: FeatureFlagsRepository

    @Mock
    private lateinit var qagInfoRepository: QagInfoRepository

    @Mock
    private lateinit var randomQagSelector: RandomQagSelector

    @Mock
    private lateinit var cacheRepository: ResponseQagPreviewCacheRepository

    @BeforeEach
    fun setUp() {
        given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.QagSelect)).willReturn(true)
    }

    @Test
    fun `putMostPopularQagInSelectedStatus - when feature is disabled - should do nothing`() {
        // Given
        given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.QagSelect)).willReturn(false)

        // When
        useCase.putMostPopularQagInSelectedStatus()

        // Then
        then(featureFlagsRepository).should(only()).isFeatureEnabled(AgoraFeature.QagSelect)
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(randomQagSelector).shouldHaveNoMoreInteractions()
        then(cacheRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `putMostPopularQagInSelectedStatus - when has no qag - should do nothing`() {
        // Given
        given(qagInfoRepository.getMostPopularQags()).willReturn(emptyList())

        // When
        useCase.putMostPopularQagInSelectedStatus()

        // Then
        then(qagInfoRepository).should(only()).getMostPopularQags()
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(randomQagSelector).shouldHaveNoMoreInteractions()
        then(cacheRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `putMostPopularQagInSelectedStatus - when has only 1 QaG - should select it`() {
        // Given
        val qag = mock(QagInfoWithSupportCount::class.java).also {
            given(it.id).willReturn("qagId")
        }
        given(qagInfoRepository.getMostPopularQags()).willReturn(listOf(qag))

        // When
        useCase.putMostPopularQagInSelectedStatus()

        // Then
        then(qagInfoRepository).should().getMostPopularQags()
        then(qagInfoRepository).should().selectQagForResponse(qagId = "qagId")
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(randomQagSelector).shouldHaveNoMoreInteractions()
        then(cacheRepository).should(only()).evictResponseQagPreviewList()
    }

    @Test
    fun `putMostPopularQagInSelectedStatus - when has multiple qags - should take a random then update its status`() {
        // Given
        val qag1 = mock(QagInfoWithSupportCount::class.java)
        val qag2 = mock(QagInfoWithSupportCount::class.java).also {
            given(it.id).willReturn("qagId2")
        }
        given(qagInfoRepository.getMostPopularQags()).willReturn(listOf(qag1, qag2))
        given(randomQagSelector.chooseRandom(listOf(qag1, qag2))).willReturn(qag2)

        // When
        useCase.putMostPopularQagInSelectedStatus()

        // Then
        then(qagInfoRepository).should().getMostPopularQags()
        then(qagInfoRepository).should().selectQagForResponse(qagId = "qagId2")
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(randomQagSelector).should(only()).chooseRandom(listOf(qag1, qag2))
        then(cacheRepository).should(only()).evictResponseQagPreviewList()
    }

}