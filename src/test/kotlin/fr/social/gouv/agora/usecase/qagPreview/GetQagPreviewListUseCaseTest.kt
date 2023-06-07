package fr.social.gouv.agora.usecase.qagPreview

import fr.social.gouv.agora.domain.QagPreview
import fr.social.gouv.agora.domain.SupportQagInfo
import fr.social.gouv.agora.usecase.qag.GetQagWithSupportAndThematiqueUseCase
import fr.social.gouv.agora.usecase.qag.QagFilters
import fr.social.gouv.agora.usecase.qag.QagInfoWithSupportAndThematique
import fr.social.gouv.agora.usecase.qag.QagPreviewMapper
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class GetQagPreviewListUseCaseTest {

    @Autowired
    private lateinit var useCase: GetQagPreviewListUseCase

    @MockBean
    private lateinit var getQagListUseCase: GetQagWithSupportAndThematiqueUseCase

    @MockBean
    private lateinit var filterGenerator: QagPreviewFilterGenerator

    @MockBean
    private lateinit var mapper: QagPreviewMapper

    @Test
    fun `getQagPreviewList - when getQagListUseCase returns emptyList - should return 3x emptyList`() {
        // Given
        val qagFilters = mock(QagFilters::class.java)
        given(filterGenerator.getPreviewQagFilters(userId = "userId", thematiqueId = "thematiqueId"))
            .willReturn(qagFilters)
        given(getQagListUseCase.getQagWithSupportAndThematique(qagFilters)).willReturn(emptyList())

        // When
        val result = useCase.getQagPreviewList(userId = "userId", thematiqueId = "thematiqueId")

        // Then
        assertThat(result).isEqualTo(
            QagPreviewList(
                popularPreviewList = emptyList(),
                latestPreviewList = emptyList(),
                supportedPreviewList = emptyList(),
            )
        )
        then(filterGenerator).should(only()).getPreviewQagFilters(userId = "userId", thematiqueId = "thematiqueId")
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQagPreviewList - when getQagListUseCase returns qags - should return popularPreviewList sorted by supportCount desc`() {
        // Given
        val qagFilters = mock(QagFilters::class.java)
        given(filterGenerator.getPreviewQagFilters(userId = "userId", thematiqueId = "thematiqueId"))
            .willReturn(qagFilters)

        val (qag6Supports, qagPreview6Supports) = mockQag(supportCount = 6)
        val (qag42Supports, qagPreview42Supports) = mockQag(supportCount = 42)
        val (qag11Supports, qagPreview11Supports) = mockQag(supportCount = 11)
        given(getQagListUseCase.getQagWithSupportAndThematique(qagFilters)).willReturn(
            listOf(qag6Supports, qag42Supports, qag11Supports)
        )

        // When
        val result = useCase.getQagPreviewList(userId = "userId", thematiqueId = "thematiqueId")

        // Then
        assertThat(result.popularPreviewList).isEqualTo(
            listOf(qagPreview42Supports, qagPreview11Supports, qagPreview6Supports)
        )
        then(filterGenerator).should(only()).getPreviewQagFilters(userId = "userId", thematiqueId = "thematiqueId")
        then(mapper).should(atLeastOnce()).toPreview(qag = qag42Supports, userId = "userId")
        then(mapper).should(atLeastOnce()).toPreview(qag = qag11Supports, userId = "userId")
        then(mapper).should(atLeastOnce()).toPreview(qag = qag6Supports, userId = "userId")
    }

    @Test
    fun `getQagPreviewList - when getQagListUseCase returns qags - should return latestPreviewList sorted by postDate desc`() {
        // Given
        val qagFilters = mock(QagFilters::class.java)
        given(filterGenerator.getPreviewQagFilters(userId = "userId", thematiqueId = "thematiqueId"))
            .willReturn(qagFilters)

        val (qagDate6, qagPreviewDate6) = mockQag(postDate = Date(6))
        val (qagDate42, qagPreviewDate42) = mockQag(postDate = Date(42))
        val (qagDate11, qagPreviewDate11) = mockQag(postDate = Date(11))
        given(getQagListUseCase.getQagWithSupportAndThematique(qagFilters)).willReturn(
            listOf(qagDate6, qagDate42, qagDate11)
        )

        // When
        val result = useCase.getQagPreviewList(userId = "userId", thematiqueId = "thematiqueId")

        // Then
        assertThat(result.latestPreviewList).isEqualTo(
            listOf(qagPreviewDate42, qagPreviewDate11, qagPreviewDate6)
        )
        then(filterGenerator).should(only()).getPreviewQagFilters(userId = "userId", thematiqueId = "thematiqueId")
        then(mapper).should(atLeastOnce()).toPreview(qag = qagDate42, userId = "userId")
        then(mapper).should(atLeastOnce()).toPreview(qag = qagDate11, userId = "userId")
        then(mapper).should(atLeastOnce()).toPreview(qag = qagDate6, userId = "userId")
    }

    @Test
    fun `getQagPreviewList - when getQagListUseCase returns qags - should return supportedPreviewList with supportedQag only sorted by supportDate desc`() {
        // Given
        val qagFilters = mock(QagFilters::class.java)
        given(filterGenerator.getPreviewQagFilters(userId = "userId", thematiqueId = "thematiqueId"))
            .willReturn(qagFilters)

        val (qagDate6, qagPreviewDate6) = mockQag(userSupportDate = Date(6))
        val (qagDate42, qagPreviewDate42) = mockQag(userSupportDate = Date(42))
        val (qagDate11, qagPreviewDate11) = mockQag(userSupportDate = Date(11))
        val (unsupportedQag, _) = mockQag(userSupportDate = null)
        given(getQagListUseCase.getQagWithSupportAndThematique(qagFilters)).willReturn(
            listOf(qagDate6, qagDate42, qagDate11, unsupportedQag)
        )

        // When
        val result = useCase.getQagPreviewList(userId = "userId", thematiqueId = "thematiqueId")

        // Then
        assertThat(result.supportedPreviewList).isEqualTo(
            listOf(qagPreviewDate42, qagPreviewDate11, qagPreviewDate6)
        )
        then(filterGenerator).should(only()).getPreviewQagFilters(userId = "userId", thematiqueId = "thematiqueId")
        then(mapper).should(atLeastOnce()).toPreview(qag = qagDate42, userId = "userId")
        then(mapper).should(atLeastOnce()).toPreview(qag = qagDate11, userId = "userId")
        then(mapper).should(atLeastOnce()).toPreview(qag = qagDate6, userId = "userId")
    }

    @Test
    fun `getQagPreviewList - when getQagListUseCase returns lots of qags - should return lists limited to 10`() {
        // Given
        val qagFilters = mock(QagFilters::class.java)
        given(filterGenerator.getPreviewQagFilters(userId = "userId", thematiqueId = "thematiqueId"))
            .willReturn(qagFilters)

        val allQags =
            (0 until 20).map { mockQag(userSupportDate = null) } + (0 until 15).map { mockQag(userSupportDate = Date(0)) }
        given(getQagListUseCase.getQagWithSupportAndThematique(qagFilters))
            .willReturn(allQags.map { it.first })

        // When
        val result = useCase.getQagPreviewList(userId = "userId", thematiqueId = "thematiqueId")

        // Then
        assertThat(result.popularPreviewList.size).isEqualTo(10)
        assertThat(result.latestPreviewList.size).isEqualTo(10)
        assertThat(result.supportedPreviewList.size).isEqualTo(10)
        then(filterGenerator).should(only()).getPreviewQagFilters(userId = "userId", thematiqueId = "thematiqueId")
    }

    private fun mockQag(
        supportCount: Int = 1,
        postDate: Date = Date(0),
        userSupportDate: Date? = null,
    ): Pair<QagInfoWithSupportAndThematique, QagPreview> {
        val totalSupportCount = supportCount - if (userSupportDate == null) 0 else 1
        val supportQagInfoList = (0 until totalSupportCount).map { index ->
            mock(SupportQagInfo::class.java).also {
                given(it.userId).willReturn("userId$index")
            }
        } + userSupportDate?.let { supportDate ->
            mock(SupportQagInfo::class.java).also {
                given(it.userId).willReturn("userId")
                given(it.supportDate).willReturn(supportDate)
            }
        }


        val qagInfo = mock(QagInfo::class.java).also {
            given(it.date).willReturn(postDate)
        }
        val qag = mock(QagInfoWithSupportAndThematique::class.java).also {
            given(it.supportQagInfoList).willReturn(supportQagInfoList.filterNotNull())
            given(it.qagInfo).willReturn(qagInfo)
        }

        val qagPreview = mock(QagPreview::class.java)
        given(mapper.toPreview(qag = qag, userId = "userId")).willReturn(qagPreview)

        return qag to qagPreview
    }

}