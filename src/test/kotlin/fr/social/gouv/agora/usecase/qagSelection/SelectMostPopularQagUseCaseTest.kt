package fr.social.gouv.agora.usecase.qagSelection

import fr.social.gouv.agora.domain.FeatureFlags
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.domain.SupportQagInfo
import fr.social.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.social.gouv.agora.usecase.qag.GetQagWithSupportAndThematiqueUseCase
import fr.social.gouv.agora.usecase.qag.QagFilters
import fr.social.gouv.agora.usecase.qag.QagInfoWithSupportAndThematique
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class SelectMostPopularQagUseCaseTest {

    @Autowired
    private lateinit var useCase: SelectMostPopularQagUseCase

    @MockBean
    private lateinit var featureFlagsRepository: FeatureFlagsRepository

    @MockBean
    private lateinit var qagListUseCase: GetQagWithSupportAndThematiqueUseCase

    @MockBean
    private lateinit var filterGenerator: MostPopularQagFilterGenerator

    @MockBean
    private lateinit var qagInfoRepository: QagInfoRepository

    @MockBean
    private lateinit var randomQagSelector: RandomQagSelector

    @BeforeEach
    fun setUp() {
        val featureFlags = mock(FeatureFlags::class.java).also {
            given(it.isQagSelectEnabled).willReturn(true)
        }
        given(featureFlagsRepository.getFeatureFlags()).willReturn(featureFlags)
    }

    @Test
    fun `putMostPopularQagInSelectedStatus - when feature is disabled - should do nothing`() {
        // Given
        val featureFlags = mock(FeatureFlags::class.java).also {
            given(it.isQagSelectEnabled).willReturn(false)
        }
        given(featureFlagsRepository.getFeatureFlags()).willReturn(featureFlags)

        // When
        useCase.putMostPopularQagInSelectedStatus()

        // Then
        then(featureFlagsRepository).should(only()).getFeatureFlags()
        then(filterGenerator).shouldHaveNoInteractions()
        then(qagListUseCase).shouldHaveNoInteractions()
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(randomQagSelector).shouldHaveNoMoreInteractions()
    }

    @Test
    fun `putMostPopularQagInSelectedStatus - when has no qag - should do nothing`() {
        // Given
        val qagFilters = mock(QagFilters::class.java)
        given(filterGenerator.generateFilter()).willReturn(qagFilters)
        given(qagListUseCase.getQagWithSupportAndThematique(qagFilters)).willReturn(emptyList())

        // When
        useCase.putMostPopularQagInSelectedStatus()

        // Then
        then(filterGenerator).should(only()).generateFilter()
        then(qagListUseCase).should(only()).getQagWithSupportAndThematique(qagFilters)
        then(qagInfoRepository).shouldHaveNoMoreInteractions()
        then(randomQagSelector).shouldHaveNoMoreInteractions()
    }

    @Test
    fun `putMostPopularQagInSelectedStatus - when has qags with different support count - should take most supported then update status`() {
        // Given
        val qagFilters = mock(QagFilters::class.java)
        given(filterGenerator.generateFilter()).willReturn(qagFilters)
        val qagWith5Supports = mockQag(qagId = "qag5", supportCount = 5)
        val qagWith13Supports = mockQag(qagId = "qag13", supportCount = 13)
        given(qagListUseCase.getQagWithSupportAndThematique(qagFilters)).willReturn(
            listOf(qagWith5Supports, qagWith13Supports)
        )

        // When
        useCase.putMostPopularQagInSelectedStatus()

        // Then
        then(filterGenerator).should(only()).generateFilter()
        then(qagListUseCase).should(only()).getQagWithSupportAndThematique(qagFilters)
        then(qagInfoRepository).should(only()).updateQagStatus(
            qagId = "qag13",
            newQagStatus = QagStatus.SELECTED_FOR_RESPONSE,
        )
        then(randomQagSelector).shouldHaveNoMoreInteractions()
    }

    @Test
    fun `putMostPopularQagInSelectedStatus - when has qags with same support count - should take a random then update its status`() {
        // Given
        val qagFilters = mock(QagFilters::class.java)
        given(filterGenerator.generateFilter()).willReturn(qagFilters)
        val qag1 = mockQag(qagId = "qag1", supportCount = 6)
        val qag2 = mockQag(qagId = "qag2", supportCount = 6)
        given(qagListUseCase.getQagWithSupportAndThematique(qagFilters)).willReturn(listOf(qag1, qag2))
        given(randomQagSelector.chooseRandom(listOf(qag1, qag2))).willReturn(qag2)

        // When
        useCase.putMostPopularQagInSelectedStatus()

        // Then
        then(filterGenerator).should(only()).generateFilter()
        then(qagListUseCase).should(only()).getQagWithSupportAndThematique(qagFilters)
        then(qagInfoRepository).should(only()).updateQagStatus(
            qagId = "qag2",
            newQagStatus = QagStatus.SELECTED_FOR_RESPONSE,
        )
        then(randomQagSelector).should(only()).chooseRandom(listOf(qag1, qag2))
    }

    private fun mockQag(qagId: String, supportCount: Int): QagInfoWithSupportAndThematique {
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn(qagId)
        }
        val supportQagInfoList = (0 until supportCount).map { mock(SupportQagInfo::class.java) }

        return mock(QagInfoWithSupportAndThematique::class.java).also {
            given(it.qagInfo).willReturn(qagInfo)
            given(it.supportQagInfoList).willReturn(supportQagInfoList)
        }
    }

}