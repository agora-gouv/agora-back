package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
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
internal class GetQagPopularPreviewListUseCaseTest {

    @Autowired
    private lateinit var useCase: GetQagPopularPreviewListUseCase

    @MockBean
    private lateinit var responseQagRepository: ResponseQagRepository

    @MockBean
    private lateinit var thematiqueRepository: ThematiqueRepository

    @MockBean
    private lateinit var supportRepository: GetSupportQagRepository

    @MockBean
    private lateinit var qagInfoRepository: QagInfoRepository

    @MockBean
    private lateinit var mapper: QagPreviewMapper

    private val userId = "userId"
    private val thematique = mock(Thematique::class.java)

    @BeforeEach
    fun setUp() {
        given(thematiqueRepository.getThematique(thematiqueId = "thematiqueId")).willReturn(thematique)
    }

    @Test
    fun `getQagPopularPreviewList - when has empty qagInfo list - should return emptyList`() {
        // Given
        given(qagInfoRepository.getAllQagInfo()).willReturn(emptyList())

        // When
        val result = useCase.getQagPopularPreviewList(userId = userId, thematiqueId = null)

        // Then
        assertThat(result).isEqualTo(emptyList<QagPreview>())
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(supportRepository).should(only()).getAllSupportQag()
        then(responseQagRepository).shouldHaveNoInteractions()
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQagPopularPreviewList - when has qagInfo but wrong thematique - should return emptyList`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.thematiqueId).willReturn("badThematiqueId")
        }
        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))

        // When
        val result = useCase.getQagPopularPreviewList(userId = userId, thematiqueId = "thematiqueId")

        // Then
        assertThat(result).isEqualTo(emptyList<QagPreview>())
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(supportRepository).should(only()).getAllSupportQag()
        then(responseQagRepository).shouldHaveNoInteractions()
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQagPopularPreviewList - when has qagInfo, but has responseQag - should return emptyList`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn("qagId")
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))
        given(responseQagRepository.getResponseQag(qagId = "qagId")).willReturn(mock(ResponseQag::class.java))

        // When
        val result = useCase.getQagPopularPreviewList(userId = userId, thematiqueId = null)

        // Then
        assertThat(result).isEqualTo(emptyList<QagPreview>())
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(supportRepository).should(only()).getAllSupportQag()
        then(responseQagRepository).should(only()).getResponseQag(qagId = "qagId")
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQagPopularPreviewList - when has qagInfo, no responseQag, but no thematique - should return emptyList`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn("qagId")
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))
        given(responseQagRepository.getResponseQag(qagId = "qagId")).willReturn(null)
        given(thematiqueRepository.getThematique(thematiqueId = "thematiqueId")).willReturn(null)

        // When
        val result = useCase.getQagPopularPreviewList(userId = userId, thematiqueId = null)

        // Then
        assertThat(result).isEqualTo(emptyList<QagPreview>())
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(supportRepository).should(only()).getAllSupportQag()
        then(responseQagRepository).should(only()).getResponseQag(qagId = "qagId")
        then(thematiqueRepository).should(only()).getThematique(thematiqueId = "thematiqueId")
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQagPopularPreviewList - when has supportQag, qagInfo, no responseQag, thematique but no support - should return mapped result`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn("qagId")
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))
        given(responseQagRepository.getResponseQag(qagId = "qagId")).willReturn(null)

        val thematique = mock(Thematique::class.java)
        given(thematiqueRepository.getThematique(thematiqueId = "thematiqueId")).willReturn(thematique)

        given(supportRepository.getAllSupportQag()).willReturn(emptyList())

        val qagPreview = mock(QagPreview::class.java)
        given(
            mapper.toPreview(
                qagInfo = qagInfo,
                thematique = thematique,
                supportQagInfoList = emptyList(),
                userId = userId,
            )
        ).willReturn(qagPreview)

        // When
        val result = useCase.getQagPopularPreviewList(userId = userId, thematiqueId = null)

        // Then
        assertThat(result).isEqualTo(listOf(qagPreview))
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(supportRepository).should(only()).getAllSupportQag()
        then(responseQagRepository).should(only()).getResponseQag(qagId = "qagId")
        then(thematiqueRepository).should(only()).getThematique(thematiqueId = "thematiqueId")
        then(mapper).should(only()).toPreview(
            qagInfo = qagInfo,
            thematique = thematique,
            supportQagInfoList = emptyList(),
            userId = userId,
        )
    }

    @Test
    fun `getQagPopularPreviewList - when has supportQag, qagInfo, no responseQag, thematique and has support - should return mapped result`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn("qagId")
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))
        given(responseQagRepository.getResponseQag(qagId = "qagId")).willReturn(null)

        val thematique = mock(Thematique::class.java)
        given(thematiqueRepository.getThematique(thematiqueId = "thematiqueId")).willReturn(thematique)

        val supportQagInfo = mock(SupportQagInfo::class.java).also {
            given(it.qagId).willReturn("qagId")
        }
        given(supportRepository.getAllSupportQag()).willReturn(listOf(supportQagInfo))

        val qagPreview = mock(QagPreview::class.java)
        given(
            mapper.toPreview(
                qagInfo = qagInfo,
                thematique = thematique,
                supportQagInfoList = listOf(supportQagInfo),
                userId = userId,
            )
        ).willReturn(qagPreview)

        // When
        val result = useCase.getQagPopularPreviewList(userId = userId, thematiqueId = null)

        // Then
        assertThat(result).isEqualTo(listOf(qagPreview))
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(supportRepository).should(only()).getAllSupportQag()
        then(responseQagRepository).should(only()).getResponseQag(qagId = "qagId")
        then(thematiqueRepository).should(only()).getThematique(thematiqueId = "thematiqueId")
        then(mapper).should(only()).toPreview(
            qagInfo = qagInfo,
            thematique = thematique,
            supportQagInfoList = listOf(supportQagInfo),
            userId = userId,
        )
    }

    @Test
    fun `getQagPopularPreviewList - when has more than 10 results - should return 10 most supported mapped qagPreview`() {
        // Given
        val mostPopularSetupData = (11 downTo 2).map { index ->
            setupQagInfo(qagId = "qagId$index", supportCount = index)
        }
        val allQagInfo = mostPopularSetupData.map { it.qagInfo }
        val allQagSupportInfo = mostPopularSetupData.flatMap { it.supportQagInfoList }
        given(qagInfoRepository.getAllQagInfo()).willReturn(allQagInfo)
        given(supportRepository.getAllSupportQag()).willReturn(allQagSupportInfo)

        // When
        val result = useCase.getQagPopularPreviewList(userId = userId, thematiqueId = null)

        // Then
        assertThat(result).isEqualTo(mostPopularSetupData.map { it.qagPreview })
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(supportRepository).should(only()).getAllSupportQag()
        (11 downTo 2).map { index ->
            then(responseQagRepository).should().getResponseQag(qagId = "qagId$index")
        }
        then(responseQagRepository).shouldHaveNoMoreInteractions()
        then(thematiqueRepository).should(only()).getThematique(thematiqueId = "thematiqueId")
        (0 until 10).map { index ->
            then(mapper).should().toPreview(
                qagInfo = mostPopularSetupData[index].qagInfo,
                thematique = thematique,
                supportQagInfoList = mostPopularSetupData[index].supportQagInfoList,
                userId = userId,
            )
        }
    }

    private fun setupQagInfo(
        qagId: String,
        supportCount: Int,
    ): SetupData {
        val supportQagInfoList = (0 until supportCount).map {
            mock(SupportQagInfo::class.java).also {
                given(it.qagId).willReturn(qagId)
            }
        }

        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn(qagId)
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(responseQagRepository.getResponseQag(qagId = qagId)).willReturn(null)

        val qagPreview = mock(QagPreview::class.java)
        given(mapper.toPreview(qagInfo, thematique, supportQagInfoList, userId)).willReturn(qagPreview)

        return SetupData(qagInfo, supportQagInfoList, qagPreview)
    }

    private data class SetupData(
        val qagInfo: QagInfo,
        val supportQagInfoList: List<SupportQagInfo>,
        val qagPreview: QagPreview,
    )

}