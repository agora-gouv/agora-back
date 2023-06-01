package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagPreview
import fr.social.gouv.agora.domain.ResponseQag
import fr.social.gouv.agora.domain.SupportQagInfo
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
internal class GetQagSupportedPreviewListUseCaseTest {

    @Autowired
    private lateinit var useCase: GetQagSupportedPreviewListUseCase

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

    private val thematique = mock(Thematique::class.java)

    @BeforeEach
    fun setUp() {
        given(thematiqueRepository.getThematique(thematiqueId = "thematiqueId")).willReturn(thematique)
    }

    @Test
    fun `getQagSupportedPreviewList - when has empty qagInfo list - should return emptyList`() {
        // Given
        given(qagInfoRepository.getAllQagInfo()).willReturn(emptyList())

        // When
        val result = useCase.getQagSupportedPreviewList(userId = "userId", thematiqueId = null)

        // Then
        assertThat(result).isEqualTo(emptyList<QagPreview>())
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(supportRepository).should(only()).getAllSupportQag()
        then(responseQagRepository).shouldHaveNoInteractions()
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQagSupportedPreviewList - when has qagInfo but wrong thematique - should return emptyList`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.thematiqueId).willReturn("badThematiqueId")
        }
        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))

        // When
        val result = useCase.getQagSupportedPreviewList(userId = "userId", thematiqueId = "thematiqueId")

        // Then
        assertThat(result).isEqualTo(emptyList<QagPreview>())
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(supportRepository).should(only()).getAllSupportQag()
        then(responseQagRepository).shouldHaveNoInteractions()
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQagSupportedPreviewList - when has qagInfo, but has no support from user - should return emptyList`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn("qagId")
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))
        val supportQagInfo = mock(SupportQagInfo::class.java).also {
            given(it.qagId).willReturn("qagId")
            given(it.userId).willReturn("anotherUserId")
        }
        given(supportRepository.getAllSupportQag()).willReturn(listOf(supportQagInfo))

        // When
        val result = useCase.getQagSupportedPreviewList(userId = "userId", thematiqueId = null)

        // Then
        assertThat(result).isEqualTo(emptyList<QagPreview>())
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(supportRepository).should(only()).getAllSupportQag()
        then(responseQagRepository).shouldHaveNoInteractions()
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQagSupportedPreviewList - when has qagInfo, support from user but has responseQag - should return emptyList`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn("qagId")
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))
        val supportQagInfo = mock(SupportQagInfo::class.java).also {
            given(it.qagId).willReturn("qagId")
            given(it.userId).willReturn("userId")
        }
        given(supportRepository.getAllSupportQag()).willReturn(listOf(supportQagInfo))
        given(responseQagRepository.getResponseQag(qagId = "qagId")).willReturn(mock(ResponseQag::class.java))

        // When
        val result = useCase.getQagSupportedPreviewList(userId = "userId", thematiqueId = null)

        // Then
        assertThat(result).isEqualTo(emptyList<QagPreview>())
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(supportRepository).should(only()).getAllSupportQag()
        then(responseQagRepository).should(only()).getResponseQag(qagId = "qagId")
        then(thematiqueRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQagSupportedPreviewList - when has qagInfo, no responseQag, but no thematique - should return emptyList`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn("qagId")
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))
        val supportQagInfo = mock(SupportQagInfo::class.java).also {
            given(it.qagId).willReturn("qagId")
            given(it.userId).willReturn("userId")
        }
        given(supportRepository.getAllSupportQag()).willReturn(listOf(supportQagInfo))
        given(responseQagRepository.getResponseQag(qagId = "qagId")).willReturn(null)
        given(thematiqueRepository.getThematique(thematiqueId = "thematiqueId")).willReturn(null)

        // When
        val result = useCase.getQagSupportedPreviewList(userId = "userId", thematiqueId = null)

        // Then
        assertThat(result).isEqualTo(emptyList<QagPreview>())
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(supportRepository).should(only()).getAllSupportQag()
        then(responseQagRepository).should(only()).getResponseQag(qagId = "qagId")
        then(thematiqueRepository).should(only()).getThematique(thematiqueId = "thematiqueId")
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQagSupportedPreviewList - when has qagInfo, no responseQag, and thematique - should return mapped qagPreview`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn("qagId")
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))
        val supportQagInfo = mock(SupportQagInfo::class.java).also {
            given(it.qagId).willReturn("qagId")
            given(it.userId).willReturn("userId")
        }
        given(supportRepository.getAllSupportQag()).willReturn(listOf(supportQagInfo))
        given(responseQagRepository.getResponseQag(qagId = "qagId")).willReturn(null)

        val thematique = mock(Thematique::class.java)
        given(thematiqueRepository.getThematique(thematiqueId = "thematiqueId")).willReturn(thematique)

        val qagPreview = mock(QagPreview::class.java)
        given(
            mapper.toPreview(
                qagInfo = qagInfo,
                thematique = thematique,
                supportQagInfoList = listOf(supportQagInfo),
                userId = "userId",
            )
        ).willReturn(qagPreview)

        // When
        val result = useCase.getQagSupportedPreviewList(userId = "userId", thematiqueId = null)

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
            userId = "userId",
        )
    }

    @Test
    fun `getQagSupportedPreviewList - when has more than 10 results - should return 10 most recently supported mapped qagPreview`() {
        // Given
        val mostRecentlySupportedSetupData = (10 downTo 1).map { index ->
            setupQagInfo(qagId = "qagId$index", supportDate = Date(index.toLong()), userId = "userId")
        }
        val leastRecentlySupportedSetupData = setupQagInfo(
            qagId = "qagId0",
            supportDate = Date(0),
            userId = "userId",
        )
        val allQagInfo = mostRecentlySupportedSetupData.map { it.qagInfo } + leastRecentlySupportedSetupData.qagInfo
        val allQagSupportInfo =
            mostRecentlySupportedSetupData.map { it.userSupportQagInfo } + leastRecentlySupportedSetupData.userSupportQagInfo
        given(qagInfoRepository.getAllQagInfo()).willReturn(allQagInfo)
        given(supportRepository.getAllSupportQag()).willReturn(allQagSupportInfo)

        // When
        val result = useCase.getQagSupportedPreviewList(userId = "userId", thematiqueId = null)

        // Then
        assertThat(result).isEqualTo(mostRecentlySupportedSetupData.map { it.qagPreview })
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(supportRepository).should(only()).getAllSupportQag()
        (10 downTo 1).map { index ->
            then(responseQagRepository).should().getResponseQag(qagId = "qagId$index")
        }
        then(responseQagRepository).shouldHaveNoMoreInteractions()
        then(thematiqueRepository).should(only()).getThematique(thematiqueId = "thematiqueId")
        (0 until 10).map { index ->
            then(mapper).should().toPreview(
                qagInfo = mostRecentlySupportedSetupData[index].qagInfo,
                thematique = thematique,
                supportQagInfoList = listOf(mostRecentlySupportedSetupData[index].userSupportQagInfo),
                userId = "userId",
            )
        }
    }

    @Suppress("SameParameterValue")
    private fun setupQagInfo(
        qagId: String,
        supportDate: Date,
        userId: String,
    ): SetupData {
        val userSupportQagInfo = mock(SupportQagInfo::class.java).also {
            given(it.qagId).willReturn(qagId)
            given(it.supportDate).willReturn(supportDate)
            given(it.userId).willReturn(userId)
        }

        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn(qagId)
            given(it.thematiqueId).willReturn("thematiqueId")
        }
        given(responseQagRepository.getResponseQag(qagId = qagId)).willReturn(null)

        val qagPreview = mock(QagPreview::class.java)
        given(mapper.toPreview(qagInfo, thematique, listOf(userSupportQagInfo), "userId")).willReturn(qagPreview)

        return SetupData(qagInfo, userSupportQagInfo, qagPreview)
    }

    private data class SetupData(
        val qagInfo: QagInfo,
        val userSupportQagInfo: SupportQagInfo,
        val qagPreview: QagPreview,
    )

}