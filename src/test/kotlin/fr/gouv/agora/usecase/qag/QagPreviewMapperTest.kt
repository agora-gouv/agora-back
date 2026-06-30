package fr.gouv.agora.usecase.qag

import fr.gouv.agora.domain.QagStatus
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Date

@ExtendWith(MockitoExtension::class)
internal class QagPreviewMapperTest {

    @InjectMocks
    private lateinit var mapper: QagPreviewMapper

    private val thematique = Thematique(id = "thematiqueId", label = "label", picto = "picto")

    private fun buildQag(status: QagStatus): QagInfoWithSupportCount {
        return QagInfoWithSupportCount(
            id = "qagId",
            thematiqueId = "thematiqueId",
            title = "title",
            description = "description",
            date = Date(),
            status = status,
            username = "username",
            userId = "userId",
            supportCount = 10,
        )
    }

    @Nested
    inner class CanShareTests {

        @Test
        fun `toPreview - when status is MODERATED_ACCEPTED - should set canShare to true`() {
            // Given
            val qag = buildQag(QagStatus.MODERATED_ACCEPTED)

            // When
            val result = mapper.toPreview(
                qag = qag,
                thematique = thematique,
                isSupportedByUser = false,
                isAuthor = false,
            )

            // Then
            assertThat(result.canShare).isTrue()
        }

        @Test
        fun `toPreview - when status is SELECTED_FOR_RESPONSE - should set canShare to true`() {
            // Given
            val qag = buildQag(QagStatus.SELECTED_FOR_RESPONSE)

            // When
            val result = mapper.toPreview(
                qag = qag,
                thematique = thematique,
                isSupportedByUser = false,
                isAuthor = false,
            )

            // Then
            assertThat(result.canShare).isTrue()
        }

        @Test
        fun `toPreview - when status is OPEN - should set canShare to false`() {
            // Given
            val qag = buildQag(QagStatus.OPEN)

            // When
            val result = mapper.toPreview(
                qag = qag,
                thematique = thematique,
                isSupportedByUser = false,
                isAuthor = false,
            )

            // Then
            assertThat(result.canShare).isFalse()
        }

        @Test
        fun `toPreview - when status is MODERATED_REJECTED - should set canShare to false`() {
            // Given
            val qag = buildQag(QagStatus.MODERATED_REJECTED)

            // When
            val result = mapper.toPreview(
                qag = qag,
                thematique = thematique,
                isSupportedByUser = false,
                isAuthor = false,
            )

            // Then
            assertThat(result.canShare).isFalse()
        }

        @Test
        fun `toPreview - when status is ARCHIVED - should set canShare to false`() {
            // Given
            val qag = buildQag(QagStatus.ARCHIVED)

            // When
            val result = mapper.toPreview(
                qag = qag,
                thematique = thematique,
                isSupportedByUser = false,
                isAuthor = false,
            )

            // Then
            assertThat(result.canShare).isFalse()
        }
    }
}
