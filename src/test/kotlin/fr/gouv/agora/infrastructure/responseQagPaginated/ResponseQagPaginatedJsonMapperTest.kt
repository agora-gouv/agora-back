package fr.gouv.agora.infrastructure.responseQagPaginated

import fr.gouv.agora.domain.ResponseQagPreviewWithoutOrder
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.common.DateMapper
import fr.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import fr.gouv.agora.infrastructure.thematique.ThematiqueNoIdJson
import fr.gouv.agora.usecase.responseQag.ResponseQagPaginatedList
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Date

@ExtendWith(MockitoExtension::class)
internal class ResponseQagPaginatedJsonMapperTest {

    @InjectMocks
    private lateinit var mapper: ResponseQagPaginatedJsonMapper

    @Mock
    private lateinit var thematiqueJsonMapper: ThematiqueJsonMapper

    @Mock
    private lateinit var dateMapper: DateMapper

    private val thematique = Thematique(id = "thema-1", label = "Santé", picto = "🏥")
    private val thematiqueNoIdJson = ThematiqueNoIdJson(label = "Santé", picto = "🏥")
    private val date = Date(0)
    private val formattedDate = "2025-01-01 00:00:00"

    @Nested
    inner class `toJson - responseTexte` {

        @Test
        fun `toJson - when responseText is not null - should map responseTexte with its value`() {
            // Given
            given(thematiqueJsonMapper.toNoIdJson(thematique)).willReturn(thematiqueNoIdJson)
            given(dateMapper.toFormattedDate(date)).willReturn(formattedDate)
            val domain = buildDomain(responseText = "Voici la réponse")
            val paginatedList = ResponseQagPaginatedList(maxPageNumber = 1, responsesQag = listOf(domain))

            // When
            val result = mapper.toJson(paginatedList)

            // Then
            assertThat(result.responses).hasSize(1)
            assertThat(result.responses[0].responseTexte).isEqualTo("Voici la réponse")
        }

        @Test
        fun `toJson - when responseText is null - should map responseTexte as empty string`() {
            // Given
            given(thematiqueJsonMapper.toNoIdJson(thematique)).willReturn(thematiqueNoIdJson)
            given(dateMapper.toFormattedDate(date)).willReturn(formattedDate)
            val domain = buildDomain(responseText = null)
            val paginatedList = ResponseQagPaginatedList(maxPageNumber = 1, responsesQag = listOf(domain))

            // When
            val result = mapper.toJson(paginatedList)

            // Then
            assertThat(result.responses).hasSize(1)
            assertThat(result.responses[0].responseTexte).isEqualTo("")
        }
    }

    @Nested
    inner class `toJson - other fields` {

        @Test
        fun `toJson - should map maxPageNumber`() {
            // Given
            val paginatedList = ResponseQagPaginatedList(maxPageNumber = 5, responsesQag = emptyList())

            // When
            val result = mapper.toJson(paginatedList)

            // Then
            assertThat(result.maxPageNumber).isEqualTo(5)
        }

        @Test
        fun `toJson - should map all response fields`() {
            // Given
            given(thematiqueJsonMapper.toNoIdJson(thematique)).willReturn(thematiqueNoIdJson)
            given(dateMapper.toFormattedDate(date)).willReturn(formattedDate)
            val domain = buildDomain(responseText = "Réponse")
            val paginatedList = ResponseQagPaginatedList(maxPageNumber = 1, responsesQag = listOf(domain))

            // When
            val result = mapper.toJson(paginatedList)

            // Then
            val json = result.responses[0]
            assertThat(json.qagId).isEqualTo("qag-1")
            assertThat(json.thematique).isEqualTo(thematiqueNoIdJson)
            assertThat(json.title).isEqualTo("Titre de la QAG")
            assertThat(json.author).isEqualTo("Auteur")
            assertThat(json.authorPortraitUrl).isEqualTo("https://portrait.url")
            assertThat(json.responseDate).isEqualTo(formattedDate)
            assertThat(json.username).isEqualTo("utilisateur")
        }
    }

    private fun buildDomain(responseText: String?) = ResponseQagPreviewWithoutOrder(
        qagId = "qag-1",
        thematique = thematique,
        title = "Titre de la QAG",
        author = "Auteur",
        authorPortraitUrl = "https://portrait.url",
        responseDate = date,
        responseText = responseText,
        username = "utilisateur",
    )
}
