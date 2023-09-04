package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagInserting
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.domain.SupportQagInserting
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInsertionResult
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagRepository
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
internal class InsertQagUseCaseTest {

    @Autowired
    private lateinit var useCase: InsertQagUseCase

    @MockBean
    private lateinit var contentSanitizer: ContentSanitizer

    @MockBean
    private lateinit var qagInfoRepository: QagInfoRepository

    @MockBean
    private lateinit var supportQagRepository: SupportQagRepository

    private val qagInserting = QagInserting(
        thematiqueId = "thematiqueId",
        title = "title",
        description = "description",
        date = Date(0),
        status = QagStatus.ARCHIVED,
        username = "username",
        userId = "userId",
    )

    @BeforeEach
    fun setUp() {
        given(contentSanitizer.isContentSaint("title", 200)).willReturn(true)
        given(contentSanitizer.isContentSaint("description", 400)).willReturn(true)
        given(contentSanitizer.isContentSaint("username", 50)).willReturn(true)
    }

    @Test
    fun `insertQag - when title is not saint - should return failure`() {
        // Given
        given(contentSanitizer.isContentSaint("title", 200)).willReturn(false)

        // When
        val result = useCase.insertQag(qagInserting)

        // Then
        assertThat(result).isEqualTo(QagInsertionResult.Failure)
        then(contentSanitizer).should(only()).isContentSaint("title", 200)
        then(qagInfoRepository).shouldHaveNoInteractions()
        then(supportQagRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `insertQag - when description is not saint - should return failure`() {
        // Given
        given(contentSanitizer.isContentSaint("title", 200)).willReturn(true)
        given(contentSanitizer.isContentSaint("description", 400)).willReturn(false)

        // When
        val result = useCase.insertQag(qagInserting)

        // Then
        assertThat(result).isEqualTo(QagInsertionResult.Failure)
        then(contentSanitizer).should().isContentSaint("title", 200)
        then(contentSanitizer).should().isContentSaint("description", 400)
        then(contentSanitizer).shouldHaveNoMoreInteractions()
        then(qagInfoRepository).shouldHaveNoInteractions()
        then(supportQagRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `insertQag - when username is not saint - should return failure`() {
        // Given
        given(contentSanitizer.isContentSaint("title", 200)).willReturn(true)
        given(contentSanitizer.isContentSaint("description", 400)).willReturn(true)
        given(contentSanitizer.isContentSaint("username", 50)).willReturn(false)

        // When
        val result = useCase.insertQag(qagInserting)

        // Then
        assertThat(result).isEqualTo(QagInsertionResult.Failure)
        then(contentSanitizer).should().isContentSaint("title", 200)
        then(contentSanitizer).should().isContentSaint("description", 400)
        then(contentSanitizer).should().isContentSaint("username", 50)
        then(contentSanitizer).shouldHaveNoMoreInteractions()
        then(qagInfoRepository).shouldHaveNoInteractions()
        then(supportQagRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `insertQag - when insert failed - should return failure`() {
        // Given
        given(qagInfoRepository.insertQagInfo(qagInserting)).willReturn(QagInsertionResult.Failure)

        // When
        val result = useCase.insertQag(qagInserting)

        // Then
        assertThat(result).isEqualTo(QagInsertionResult.Failure)
        then(contentSanitizer).should().isContentSaint("title", 200)
        then(contentSanitizer).should().isContentSaint("description", 400)
        then(contentSanitizer).should().isContentSaint("username", 50)
        then(contentSanitizer).shouldHaveNoMoreInteractions()
        then(qagInfoRepository).should(only()).insertQagInfo(qagInserting)
    }

    @Test
    fun `insertQag - when insert success - should add support then return success`() {
        // Given
        val qagId = UUID.randomUUID()
        given(qagInfoRepository.insertQagInfo(qagInserting)).willReturn(QagInsertionResult.Success(qagId = qagId))

        // When
        val result = useCase.insertQag(qagInserting)

        // Then
        assertThat(result).isEqualTo(QagInsertionResult.Success(qagId = qagId))
        then(contentSanitizer).should().isContentSaint("title", 200)
        then(contentSanitizer).should().isContentSaint("description", 400)
        then(contentSanitizer).should().isContentSaint("username", 50)
        then(contentSanitizer).shouldHaveNoMoreInteractions()
        then(qagInfoRepository).should(only()).insertQagInfo(qagInserting)
        then(supportQagRepository).should(only()).insertSupportQag(
            SupportQagInserting(
                qagId = qagId.toString(),
                userId = "userId",
            )
        )
    }

}