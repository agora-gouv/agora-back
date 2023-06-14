package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagModeratingLockRepository
import fr.social.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class GetQagModeratingListUseCaseTest {

    @Autowired
    private lateinit var useCase: GetQagModeratingListUseCase

    @MockBean
    private lateinit var responseQagRepository: ResponseQagRepository

    @MockBean
    private lateinit var thematiqueRepository: ThematiqueRepository

    @MockBean
    private lateinit var supportRepository: GetSupportQagRepository

    @MockBean
    private lateinit var qagInfoRepository: QagInfoRepository

    @MockBean
    private lateinit var qagModeratingLockRepository: QagModeratingLockRepository

    @MockBean
    private lateinit var mapper: QagModeratingMapper

    @Nested
    inner class GetQagModeratingListTestCases {

        private val userId = "userId"
        private val thematique = mock(Thematique::class.java)

        @BeforeEach
        fun setUp() {
            given(thematiqueRepository.getThematique(thematiqueId = "thematiqueId")).willReturn(thematique)
        }

        @Test
        fun `getQagModeratingList - when has empty qagInfo list - should return emptyList`() {
            // Given
            given(qagInfoRepository.getAllQagInfo()).willReturn(emptyList())

            // When
            val result = useCase.getQagModeratingList(userId = userId)

            // Then
            assertThat(result).isEqualTo(emptyList<QagPreview>())
            then(qagInfoRepository).should(only()).getAllQagInfo()
            then(responseQagRepository).shouldHaveNoInteractions()
            then(thematiqueRepository).shouldHaveNoInteractions()
            then(supportRepository).shouldHaveNoInteractions()
            then(qagModeratingLockRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getQagModeratingList - when has qagInfo but status is not OPEN - should return emptyList`() {
            // Given
            val qagInfo = mock(QagInfo::class.java).also {
                given(it.status).willReturn(QagStatus.MODERATED_ACCEPTED)
            }
            given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))

            // When
            val result = useCase.getQagModeratingList(userId = userId)

            // Then
            assertThat(result).isEqualTo(emptyList<QagPreview>())
            then(qagInfoRepository).should(only()).getAllQagInfo()
            then(responseQagRepository).shouldHaveNoInteractions()
            then(thematiqueRepository).shouldHaveNoInteractions()
            then(supportRepository).shouldHaveNoInteractions()
            then(qagModeratingLockRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getQagModeratingList - when has not locked qagInfo but has existing responseQag - should return emptyList`() {
            // Given
            val qagInfo = mock(QagInfo::class.java).also {
                given(it.id).willReturn("qagId")
                given(it.status).willReturn(QagStatus.OPEN)
            }
            given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))
            given(responseQagRepository.getResponseQag(qagId = "qagId")).willReturn(mock(ResponseQag::class.java))
            given(qagModeratingLockRepository.isQagLocked(qagId = "qagId")).willReturn(false)

            // When
            val result = useCase.getQagModeratingList(userId = userId)

            // Then
            assertThat(result).isEqualTo(emptyList<QagPreview>())
            then(qagInfoRepository).should(only()).getAllQagInfo()
            then(responseQagRepository).should(only()).getResponseQag(qagId = "qagId")
            then(thematiqueRepository).shouldHaveNoInteractions()
            then(supportRepository).shouldHaveNoInteractions()
            then(qagModeratingLockRepository).should(only()).isQagLocked(qagId = "qagId")
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getQagModeratingList - when has not locked qagInfo, no responseQag but no corresponding thematique - should return emptyList`() {
            // Given
            val qagInfo = mock(QagInfo::class.java).also {
                given(it.id).willReturn("qagId")
                given(it.status).willReturn(QagStatus.OPEN)
                given(it.thematiqueId).willReturn("thematiqueId")
            }
            given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))
            given(responseQagRepository.getResponseQag(qagId = "qagId")).willReturn(null)
            given(thematiqueRepository.getThematique(thematiqueId = "thematiqueId")).willReturn(null)
            given(qagModeratingLockRepository.isQagLocked(qagId = "qagId")).willReturn(false)

            // When
            val result = useCase.getQagModeratingList(userId = userId)

            // Then
            assertThat(result).isEqualTo(emptyList<QagPreview>())
            then(qagInfoRepository).should(only()).getAllQagInfo()
            then(responseQagRepository).should(only()).getResponseQag(qagId = "qagId")
            then(thematiqueRepository).should(only()).getThematique(thematiqueId = "thematiqueId")
            then(supportRepository).shouldHaveNoInteractions()
            then(qagModeratingLockRepository).should(only()).isQagLocked(qagId = "qagId")
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getQagModeratingList - when has not locked qagInfo, no responseQag, thematique but no support - should return emptyList`() {
            // Given
            val qagInfo = mock(QagInfo::class.java).also {
                given(it.id).willReturn("qagId")
                given(it.status).willReturn(QagStatus.OPEN)
                given(it.thematiqueId).willReturn("thematiqueId")
            }
            given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(qagInfo))
            given(responseQagRepository.getResponseQag(qagId = "qagId")).willReturn(null)
            given(qagModeratingLockRepository.isQagLocked(qagId = "qagId")).willReturn(false)

            val thematique = mock(Thematique::class.java)
            given(thematiqueRepository.getThematique(thematiqueId = "thematiqueId")).willReturn(thematique)

            given(supportRepository.getSupportQag(qagId = "qagId", userId = userId)).willReturn(null)

            // When
            val result = useCase.getQagModeratingList(userId = userId)

            // Then
            assertThat(result).isEqualTo(emptyList<QagPreview>())
            then(qagInfoRepository).should(only()).getAllQagInfo()
            then(responseQagRepository).should(only()).getResponseQag(qagId = "qagId")
            then(thematiqueRepository).should(only()).getThematique(thematiqueId = "thematiqueId")
            then(supportRepository).should(only()).getSupportQag(qagId = "qagId", userId = userId)
            then(qagModeratingLockRepository).should(only()).isQagLocked(qagId = "qagId")
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getQagModeratingList - when has not locked qagInfo, no responseQag, thematique, support and thematique - should return mapped qagPreview`() {
            // Given
            val setupData = setupQagInfo(qagId = "qagId")
            given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(setupData.qagInfo))
            given(responseQagRepository.getResponseQag(qagId = "qagId")).willReturn(null)
            given(qagModeratingLockRepository.isQagLocked(qagId = "qagId")).willReturn(false)

            // When
            val result = useCase.getQagModeratingList(userId = userId)

            // Then
            assertThat(result).isEqualTo(listOf(setupData.qagModerating))
            then(qagInfoRepository).should(only()).getAllQagInfo()
            then(responseQagRepository).should(only()).getResponseQag(qagId = "qagId")
            then(thematiqueRepository).should(only()).getThematique(thematiqueId = "thematiqueId")
            then(supportRepository).should(only()).getSupportQag(qagId = "qagId", userId = userId)
            then(qagModeratingLockRepository).should(times(1)).isQagLocked(qagId = "qagId")
            then(qagModeratingLockRepository).should(times(1)).setQagLocked(setupData.qagModerating.id)
            then(mapper).should(only()).toQagModerating(setupData.qagInfo, thematique, setupData.supportQag)
        }

        @Test
        fun `getQagModeratingList - when has locked qagInfo, no responseQag, thematique, support and thematique - should return mapped emptyList`() {
            // Given
            val setupData = setupQagInfo(qagId = "qagId")
            given(qagInfoRepository.getAllQagInfo()).willReturn(listOf(setupData.qagInfo))
            given(responseQagRepository.getResponseQag(qagId = "qagId")).willReturn(null)
            given(qagModeratingLockRepository.isQagLocked(qagId = "qagId")).willReturn(true)

            // When
            val result = useCase.getQagModeratingList(userId = userId)

            // Then
            assertThat(result).isEqualTo(emptyList<QagPreview>())
            then(qagInfoRepository).should(only()).getAllQagInfo()
            then(responseQagRepository).shouldHaveNoInteractions()
            then(thematiqueRepository).shouldHaveNoInteractions()
            then(supportRepository).shouldHaveNoInteractions()
            then(qagModeratingLockRepository).should(only()).isQagLocked(qagId = "qagId")
            then(mapper).shouldHaveNoInteractions()
        }

        private fun setupQagInfo(
            qagId: String,
            postDate: Date = Date(0)
        ): SetupData {
            val qagInfo = mock(QagInfo::class.java).also {
                given(it.id).willReturn(qagId)
                given(it.status).willReturn(QagStatus.OPEN)
                given(it.thematiqueId).willReturn("thematiqueId")
                given(it.date).willReturn(postDate)
            }
            given(responseQagRepository.getResponseQag(qagId = qagId)).willReturn(null)

            val supportQag = mock(SupportQag::class.java)
            given(supportRepository.getSupportQag(qagId = qagId, userId = userId)).willReturn(supportQag)

            val qagPreview = mock(QagModerating::class.java)
            given(mapper.toQagModerating(qagInfo, thematique, supportQag)).willReturn(qagPreview)

            return SetupData(qagInfo, supportQag, qagPreview)
        }
    }

    companion object {
        @JvmStatic
        fun getModeratingQagCountTestCases() = arrayOf(
            input(
                qagIdAndStatusList = listOf("qagId1" to QagStatus.ARCHIVED),
                responseQagIdList = emptyList(),
                expectedModeratingQagCount = 0,
            ),
            input(
                qagIdAndStatusList = listOf("qagId1" to QagStatus.MODERATED_ACCEPTED),
                responseQagIdList = emptyList(),
                expectedModeratingQagCount = 0,
            ),
            input(
                qagIdAndStatusList = listOf("qagId1" to QagStatus.MODERATED_REJECTED),
                responseQagIdList = emptyList(),
                expectedModeratingQagCount = 0,
            ),
            input(
                qagIdAndStatusList = listOf("qagId1" to QagStatus.OPEN),
                responseQagIdList = emptyList(),
                expectedModeratingQagCount = 1,
            ),
            input(
                qagIdAndStatusList = listOf("qagId1" to QagStatus.OPEN),
                responseQagIdList = listOf("qagId1"),
                expectedModeratingQagCount = 0,
            ),
            input(
                qagIdAndStatusList = listOf(
                    "qagId1" to QagStatus.OPEN,
                    "qagId2" to QagStatus.ARCHIVED,
                    "qagId3" to QagStatus.MODERATED_ACCEPTED,
                    "qagId4" to QagStatus.MODERATED_REJECTED,
                    "qagId5" to QagStatus.OPEN,
                    "qagId6" to QagStatus.OPEN,
                ),
                responseQagIdList = listOf("qagId1"),
                expectedModeratingQagCount = 2,
            ),
        )

        private fun input(
            qagIdAndStatusList: List<Pair<String, QagStatus>>,
            responseQagIdList: List<String>,
            expectedModeratingQagCount: Int,
        ) = arrayOf(
            qagIdAndStatusList.map { (qagId, qagStatus) ->
                mock(QagInfo::class.java).also {
                    given(it.id).willReturn(qagId)
                    given(it.status).willReturn(qagStatus)
                }
            },
            responseQagIdList.map { qagId ->
                mock(ResponseQag::class.java).also {
                    given(it.qagId).willReturn(qagId)
                }
            },
            expectedModeratingQagCount
        )

    }

    @ParameterizedTest(name = "getModeratingQagCount - when has qags {0} and responses {1} - should return {2}")
    @MethodSource("getModeratingQagCountTestCases")
    fun `getModeratingQagCount - should return count of qag filtered by status OPEN and filter out qag with response`(
        qagInfoList: List<QagInfo>,
        responseQagList: List<ResponseQag>,
        expectedModeratingQagCount: Int,
    ) {
        // Given
        given(qagInfoRepository.getAllQagInfo()).willReturn(qagInfoList)
        given(responseQagRepository.getAllResponseQag()).willReturn(responseQagList)

        // When
        val result = useCase.getModeratingQagCount()

        // Then
        assertThat(result).isEqualTo(expectedModeratingQagCount)
        then(qagInfoRepository).should(only()).getAllQagInfo()
        then(responseQagRepository).should(only()).getAllResponseQag()
    }

    private data class SetupData(
        val qagInfo: QagInfo,
        val supportQag: SupportQag,
        val qagModerating: QagModerating,
    )
}