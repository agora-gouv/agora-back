package fr.gouv.agora.usecase.responseQag

import fr.gouv.agora.domain.IncomingResponsePreview
import fr.gouv.agora.domain.QagStatus
import fr.gouv.agora.domain.ResponseQagPreviewWithoutOrder
import fr.gouv.agora.domain.ResponseQagText
import fr.gouv.agora.domain.ResponseQagVideo
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.gouv.agora.usecase.qag.repository.QagInfo
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import java.time.LocalDate
import java.util.Date

class ResponseQagPreviewListMapperTest {

    private val mapper = ResponseQagPreviewListMapper()

    private val qagInfo = QagInfo(
        id = "qagId",
        thematiqueId = "thematiqueId",
        title = "title",
        description = "description",
        date = Date(0),
        status = QagStatus.SELECTED_FOR_RESPONSE,
        username = "username",
        userId = "userId",
    )

    @Nested
    inner class ToResponseQagPreviewWithoutOrder {

        @Test
        fun `toResponseQagPreviewWithoutOrder - when responseQag is ResponseQagText - should set responseText`() {
            // Given
            val thematique = mock(Thematique::class.java)
            val responseDate = Date(1000)
            val responseQag = ResponseQagText(
                author = "author",
                authorPortraitUrl = "portraitUrl",
                responseDate = responseDate,
                feedbackQuestion = "feedbackQuestion",
                qagId = "qagId",
                responseLabel = "label",
                responseText = "Le texte de la réponse",
            )

            // When
            val result = mapper.toResponseQagPreviewWithoutOrder(
                qagInfo = qagInfo,
                responseQag = responseQag,
                thematique = thematique,
            )

            // Then
            assertThat(result).isEqualTo(
                ResponseQagPreviewWithoutOrder(
                    qagId = "qagId",
                    thematique = thematique,
                    title = "title",
                    author = "author",
                    authorPortraitUrl = "portraitUrl",
                    responseDate = responseDate,
                    responseText = "Le texte de la réponse",
                    username = "username",
                )
            )
        }

        @Test
        fun `toResponseQagPreviewWithoutOrder - when responseQag is ResponseQagText with HTML tags - should strip HTML tags`() {
            // Given
            val thematique = mock(Thematique::class.java)
            val responseDate = Date(1000)
            val responseQag = ResponseQagText(
                author = "author",
                authorPortraitUrl = "portraitUrl",
                responseDate = responseDate,
                feedbackQuestion = "feedbackQuestion",
                qagId = "qagId",
                responseLabel = "label",
                responseText = "<p>Le texte de la <strong>réponse</strong></p>",
            )

            // When
            val result = mapper.toResponseQagPreviewWithoutOrder(
                qagInfo = qagInfo,
                responseQag = responseQag,
                thematique = thematique,
            )

            // Then
            assertThat(result.responseText).isEqualTo("Le texte de la réponse")
        }

        @Test
        fun `toResponseQagPreviewWithoutOrder - when responseQag is ResponseQagText with text shorter than 200 characters - should not add ellipsis`() {
            // Given
            val thematique = mock(Thematique::class.java)
            val responseDate = Date(1000)
            val shortText = "a".repeat(100)
            val responseQag = ResponseQagText(
                author = "author",
                authorPortraitUrl = "portraitUrl",
                responseDate = responseDate,
                feedbackQuestion = "feedbackQuestion",
                qagId = "qagId",
                responseLabel = "label",
                responseText = shortText,
            )

            // When
            val result = mapper.toResponseQagPreviewWithoutOrder(
                qagInfo = qagInfo,
                responseQag = responseQag,
                thematique = thematique,
            )

            // Then
            assertThat(result.responseText).isEqualTo("a".repeat(100))
        }

        @Test
        fun `toResponseQagPreviewWithoutOrder - when responseQag is ResponseQagText with text longer than 200 characters - should truncate to 200 characters and add ellipsis`() {
            // Given
            val thematique = mock(Thematique::class.java)
            val responseDate = Date(1000)
            val longText = "a".repeat(250)
            val responseQag = ResponseQagText(
                author = "author",
                authorPortraitUrl = "portraitUrl",
                responseDate = responseDate,
                feedbackQuestion = "feedbackQuestion",
                qagId = "qagId",
                responseLabel = "label",
                responseText = longText,
            )

            // When
            val result = mapper.toResponseQagPreviewWithoutOrder(
                qagInfo = qagInfo,
                responseQag = responseQag,
                thematique = thematique,
            )

            // Then
            assertThat(result.responseText).isEqualTo("a".repeat(200) + "...")
        }

        @Test
        fun `toResponseQagPreviewWithoutOrder - when responseQag is ResponseQagText with HTML tags and text longer than 200 characters - should strip HTML then truncate to 200 characters and add ellipsis`() {
            // Given
            val thematique = mock(Thematique::class.java)
            val responseDate = Date(1000)
            val longText = "b".repeat(250)
            val responseQag = ResponseQagText(
                author = "author",
                authorPortraitUrl = "portraitUrl",
                responseDate = responseDate,
                feedbackQuestion = "feedbackQuestion",
                qagId = "qagId",
                responseLabel = "label",
                responseText = "<p>$longText</p>",
            )

            // When
            val result = mapper.toResponseQagPreviewWithoutOrder(
                qagInfo = qagInfo,
                responseQag = responseQag,
                thematique = thematique,
            )

            // Then
            assertThat(result.responseText).isEqualTo("b".repeat(200) + "...")
        }

        @Test
        fun `toResponseQagPreviewWithoutOrder - when responseQag is ResponseQagVideo - should set responseText to null`() {
            // Given
            val thematique = mock(Thematique::class.java)
            val responseDate = Date(1000)
            val responseQag = ResponseQagVideo(
                author = "author",
                authorPortraitUrl = "portraitUrl",
                responseDate = responseDate,
                feedbackQuestion = "feedbackQuestion",
                qagId = "qagId",
                authorDescription = "description",
                videoUrl = "videoUrl",
                videoTitle = "videoTitle",
                videoWidth = 1280,
                videoHeight = 720,
                transcription = "transcription",
                additionalInfo = null,
            )

            // When
            val result = mapper.toResponseQagPreviewWithoutOrder(
                qagInfo = qagInfo,
                responseQag = responseQag,
                thematique = thematique,
            )

            // Then
            assertThat(result).isEqualTo(
                ResponseQagPreviewWithoutOrder(
                    qagId = "qagId",
                    thematique = thematique,
                    title = "title",
                    author = "author",
                    authorPortraitUrl = "portraitUrl",
                    responseDate = responseDate,
                    responseText = null,
                    username = "username",
                )
            )
        }
    }

    @Test
    fun `toIncomingResponsePreview si la question gagne un jour hors lundi, renvoi le lundi precedent et le lundi suivant`() {
        // Given
        val thematique = mock(Thematique::class.java)
        val qagWithSupportCountAndOrder = QagWithSupportCountAndOrder(
            qagWithSupportCount = QagInfoWithSupportCount(
                id = "id",
                thematiqueId = "thematiqueId",
                title = "title",
                description = "description",
                date = LocalDate.of(2024, 6, 6).toDate(),
                status = QagStatus.SELECTED_FOR_RESPONSE,
                username = "username",
                userId = "userId",
                supportCount = 0,
            ),
            order = 0,
        )


        // When
        val result = ResponseQagPreviewListMapper().toIncomingResponsePreview(
            qagWithSupportCountAndOrder,
            thematique,
        )

        // Then
        assertThat(result).isEqualTo(
            IncomingResponsePreview(
                id = "id",
                thematique = thematique,
                title = "title",
                supportCount = 0,
                dateLundiPrecedent = LocalDate.of(2024, 6, 3),
                dateLundiSuivant = LocalDate.of(2024, 6, 10),
                order = 0,
            )
        )
    }

    @Test
    fun `toIncomingResponsePreview si la question gagne un lundi, renvoi le lundi precedent et le jour actuel`() {
        // Given
        val thematique = mock(Thematique::class.java)
        val qagWithSupportCountAndOrder = QagWithSupportCountAndOrder(
            qagWithSupportCount = QagInfoWithSupportCount(
                id = "id",
                thematiqueId = "thematiqueId",
                title = "title",
                description = "description",
                date = LocalDate.of(2024, 6, 10).toDate(),
                status = QagStatus.SELECTED_FOR_RESPONSE,
                username = "username",
                userId = "userId",
                supportCount = 0,
            ),
            order = 0,
        )


        // When
        val result = ResponseQagPreviewListMapper().toIncomingResponsePreview(
            qagWithSupportCountAndOrder,
            thematique,
        )

        // Then
        assertThat(result).isEqualTo(
            IncomingResponsePreview(
                id = "id",
                thematique = thematique,
                title = "title",
                supportCount = 0,
                dateLundiPrecedent = LocalDate.of(2024, 6, 10),
                dateLundiSuivant = LocalDate.of(2024, 6, 17),
                order = 0,
            )
        )
    }
}
