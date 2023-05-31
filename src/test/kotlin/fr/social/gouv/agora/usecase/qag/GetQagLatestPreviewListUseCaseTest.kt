package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagPreview
import fr.social.gouv.agora.domain.ResponseQag
import fr.social.gouv.agora.domain.SupportQag
import fr.social.gouv.agora.domain.Thematique
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
internal class GetQagLatestPreviewListUseCaseTest {

    @Autowired
    private lateinit var useCase: GetQagLatestPreviewListUseCase

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
    fun `getQagLatestPreviewList - when has empty qagInfo list - should return emptyList`() {
        // Given
        given(qagInfoRepository.getAllQagInfo()).willReturn(emptyList())

        // When
        val result = useCase.getQagLatestPreviewList(userId = userId, thematiqueId = null)

        // Then
        assertThat(result).isEqualTo(emptyList<QagPreview>())
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(responseQagRepository).shouldHaveNoInteractions()
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(supportRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQagLatestPreviewList - when has qagInfo but wrong thematique - should return emptyList`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn("qagId")
            given(it.thematiqueId).willReturn("badThematiqueId")
        }
        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))

        // When
        val result = useCase.getQagLatestPreviewList(userId = userId, thematiqueId = "thematiqueId")

        // Then
        assertThat(result).isEqualTo(emptyList<QagPreview>())
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(responseQagRepository).shouldHaveNoInteractions()
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(supportRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQagLatestPreviewList - when has qagInfo but has existing responseQag - should return emptyList`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn("qagId")
        }
        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))
        given(responseQagRepository.getResponseQag(qagId = "qagId")).willReturn(mock(ResponseQag::class.java))

        // When
        val result = useCase.getQagLatestPreviewList(userId = userId, thematiqueId = null)

        // Then
        assertThat(result).isEqualTo(emptyList<QagPreview>())
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(responseQagRepository).should(only()).getResponseQag(qagId = "qagId")
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(supportRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQagLatestPreviewList - when has qagInfo, no responseQag but no corresponding thematique - should return emptyList`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn("qagId")
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))
        given(responseQagRepository.getResponseQag(qagId = "qagId")).willReturn(null)
        given(thematiqueRepository.getThematique(thematiqueId = "thematiqueId")).willReturn(null)

        // When
        val result = useCase.getQagLatestPreviewList(userId = userId, thematiqueId = null)

        // Then
        assertThat(result).isEqualTo(emptyList<QagPreview>())
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(responseQagRepository).should(only()).getResponseQag(qagId = "qagId")
        then(thematiqueRepository).should(only()).getThematique(thematiqueId = "thematiqueId")
        then(supportRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQagLatestPreviewList - when has qagInfo, no responseQag, thematique but no support - should return emptyList`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn("qagId")
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))
        given(responseQagRepository.getResponseQag(qagId = "qagId")).willReturn(null)

        val thematique = mock(Thematique::class.java)
        given(thematiqueRepository.getThematique(thematiqueId = "thematiqueId")).willReturn(thematique)

        given(supportRepository.getSupportQag(qagId = "qagId", userId = userId)).willReturn(null)

        // When
        val result = useCase.getQagLatestPreviewList(userId = userId, thematiqueId = null)

        // Then
        assertThat(result).isEqualTo(emptyList<QagPreview>())
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(responseQagRepository).should(only()).getResponseQag(qagId = "qagId")
        then(thematiqueRepository).should(only()).getThematique(thematiqueId = "thematiqueId")
        then(supportRepository).should(only()).getSupportQag(qagId = "qagId", userId = userId)
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQagLatestPreviewList - when has qagInfo, no responseQag, thematique, support and thematique filter is null - should return mapped qagPreview`() {
        // Given
        val setupData = setupQagInfo(qagId = "qagId")
        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(setupData.qagInfo))
        given(responseQagRepository.getResponseQag(qagId = "qagId")).willReturn(null)

        // When
        val result = useCase.getQagLatestPreviewList(userId = userId, thematiqueId = null)

        // Then
        assertThat(result).isEqualTo(listOf(setupData.qagPreview))
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(responseQagRepository).should(only()).getResponseQag(qagId = "qagId")
        then(thematiqueRepository).should(only()).getThematique(thematiqueId = "thematiqueId")
        then(supportRepository).should(only()).getSupportQag(qagId = "qagId", userId = userId)
        then(mapper).should(only()).toPreview(setupData.qagInfo, thematique, setupData.supportQag)
    }

    @Test
    fun `getQagLatestPreviewList - when has more than 10 results - should return latest 10 mapped qagPreview`() {
        // Given
        val mostRecentSetupData = (10 downTo 1).map { index ->
            setupQagInfo(qagId = "qagId$index", postDate = Date(index.toLong()))
        }
        val oldSetupData = setupQagInfo(qagId = "qagId0", postDate = Date(0))
        val allQagInfo = mostRecentSetupData.map { it.qagInfo } + oldSetupData.qagInfo
        given(qagInfoRepository.getAllQagInfo()).willReturn(allQagInfo)

        // When
        val result = useCase.getQagLatestPreviewList(userId = userId, thematiqueId = null)

        // Then
        assertThat(result).isEqualTo(mostRecentSetupData.map { it.qagPreview })
        then(qagInfoRepository).should(only()).getAllQagInfo()
        (10 downTo 1).map { index ->
            then(responseQagRepository).should().getResponseQag(qagId = "qagId$index")
        }
        then(responseQagRepository).shouldHaveNoMoreInteractions()
        then(thematiqueRepository).should(only()).getThematique(thematiqueId = "thematiqueId")
        (10 downTo 1).map { index ->
            then(supportRepository).should().getSupportQag(qagId = "qagId$index", userId = userId)
        }
        then(supportRepository).shouldHaveNoMoreInteractions()
        (0 until 10).map { index ->
            then(mapper).should().toPreview(
                qagInfo = mostRecentSetupData[index].qagInfo,
                thematique = thematique,
                supportQag = mostRecentSetupData[index].supportQag,
            )
        }
        then(mapper).shouldHaveNoMoreInteractions()
    }

    private fun setupQagInfo(
        qagId: String,
        postDate: Date = Date(0)
    ): SetupData {
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn(qagId)
            given(it.thematiqueId).willReturn("thematiqueId")
            given(it.date).willReturn(postDate)
        }
        given(responseQagRepository.getResponseQag(qagId = qagId)).willReturn(null)

        val supportQag = mock(SupportQag::class.java)
        given(supportRepository.getSupportQag(qagId = qagId, userId = userId)).willReturn(supportQag)

        val qagPreview = mock(QagPreview::class.java)
        given(mapper.toPreview(qagInfo, thematique, supportQag)).willReturn(qagPreview)

        return SetupData(qagInfo, supportQag, qagPreview)
    }

    private data class SetupData(
        val qagInfo: QagInfo,
        val supportQag: SupportQag,
        val qagPreview: QagPreview,
    )

}