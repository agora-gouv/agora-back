package fr.gouv.agora.usecase.responseQag

import fr.gouv.agora.domain.IncomingResponsePreview
import fr.gouv.agora.domain.QagStatus
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import java.time.LocalDate

class ResponseQagPreviewListMapperTest {

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
                dateLundiPrecedent = LocalDate.of(2024, 6, 3),
                dateLundiSuivant = LocalDate.of(2024, 6, 10),
                order = 0,
            )
        )
    }
}
