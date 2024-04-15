package fr.gouv.agora.usecase.qag

import fr.gouv.agora.domain.QagInserting
import fr.gouv.agora.domain.QagStatus
import fr.gouv.agora.domain.SupportQagInserting
import fr.gouv.agora.usecase.qag.repository.*
import fr.gouv.agora.usecase.supportQag.repository.SupportQagCacheRepository
import fr.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class InsertQagUseCaseTest {

    @InjectMocks
    private lateinit var useCase: InsertQagUseCase

    @Mock
    private lateinit var contentSanitizer: ContentSanitizer

    @Mock
    private lateinit var qagInfoRepository: QagInfoRepository

    @Mock
    private lateinit var qagPreviewCacheRepository: QagPreviewCacheRepository

    @Mock
    private lateinit var supportQagRepository: SupportQagRepository

    @Mock
    private lateinit var askQagStatusCacheRepository: AskQagStatusCacheRepository

    @Mock
    private lateinit var supportQagCacheRepository: SupportQagCacheRepository

    private val qagInserting = QagInserting(
        thematiqueId = "thematiqueId",
        title = "title",
        description = "description",
        date = Date(0),
        status = QagStatus.ARCHIVED,
        username = "username",
        userId = "userId",
    )

    private val sanitizedQagInserting = QagInserting(
        thematiqueId = "thematiqueId",
        title = "sanitizedTitle",
        description = "sanitizedDescription",
        date = Date(0),
        status = QagStatus.ARCHIVED,
        username = "sanitizedUsername",
        userId = "userId",
    )

    @BeforeEach
    fun setUp() {
        given(contentSanitizer.sanitize("title", 200)).willReturn("sanitizedTitle")
        given(contentSanitizer.sanitize("description", 400)).willReturn("sanitizedDescription")
        given(contentSanitizer.sanitize("username", 50)).willReturn("sanitizedUsername")
    }

    @Test
    fun `insertQag - when insert failed - should return failure`() {
        // Given
        given(qagInfoRepository.insertQagInfo(sanitizedQagInserting)).willReturn(QagInsertionResult.Failure)

        // When
        val result = useCase.insertQag(qagInserting)

        // Then
        assertThat(result).isEqualTo(QagInsertionResult.Failure)
        then(contentSanitizer).should().sanitize("title", 200)
        then(contentSanitizer).should().sanitize("description", 400)
        then(contentSanitizer).should().sanitize("username", 50)
        then(contentSanitizer).shouldHaveNoMoreInteractions()
        then(qagInfoRepository).should(only()).insertQagInfo(sanitizedQagInserting)
        then(qagPreviewCacheRepository).shouldHaveNoInteractions()
        then(askQagStatusCacheRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `insertQag - when insert success - should add support then return success`() {
        // Given
        val qagInfo = mock(QagInfo::class.java).also {
            given(it.id).willReturn("qagId")
        }
        given(qagInfoRepository.insertQagInfo(sanitizedQagInserting)).willReturn(QagInsertionResult.Success(qagInfo = qagInfo))

        // When
        val result = useCase.insertQag(qagInserting)

        // Then
        assertThat(result).isEqualTo(QagInsertionResult.Success(qagInfo = qagInfo))
        then(contentSanitizer).should().sanitize("title", 200)
        then(contentSanitizer).should().sanitize("description", 400)
        then(contentSanitizer).should().sanitize("username", 50)
        then(contentSanitizer).shouldHaveNoMoreInteractions()
        then(qagInfoRepository).should(only()).insertQagInfo(sanitizedQagInserting)
        then(supportQagRepository).should(only()).insertSupportQag(
            SupportQagInserting(
                qagId = "qagId",
                userId = "userId",
            )
        )
        then(qagPreviewCacheRepository).should().evictQagSupportedList(userId = "userId", thematiqueId = null)
        then(qagPreviewCacheRepository).should().evictQagSupportedList(userId = "userId", thematiqueId = "thematiqueId")
        then(qagPreviewCacheRepository).shouldHaveNoMoreInteractions()
        then(askQagStatusCacheRepository).should(only()).evictAskQagStatus(userId = "userId")
        then(supportQagCacheRepository).should(only()).addSupportedQagIds(userId = "userId", qagId = "qagId")
    }

}