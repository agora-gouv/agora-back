package fr.gouv.agora.infrastructure.thematique.repository

import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiMetadata
import fr.gouv.agora.infrastructure.common.StrapiMetaPagination
import fr.gouv.agora.infrastructure.thematique.dto.ThematiqueStrapiDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class ThematiqueMapperTest {

    private lateinit var mapper: ThematiqueMapper

    @BeforeEach
    fun setUp() {
        mapper = ThematiqueMapper()
    }

    @Nested
    inner class `toDomain - single DTO` {

        @Test
        fun `toDomain - when valid dto - should map documentId as id`() {
            // Given
            val dto = buildThematiqueStrapiDTO(documentId = "thema-42")

            // When
            val result = mapper.toDomain(dto)

            // Then
            assertThat(result.id).isEqualTo("thema-42")
        }

        @Test
        fun `toDomain - when valid dto - should map label`() {
            // Given
            val dto = buildThematiqueStrapiDTO(label = "Démocratie")

            // When
            val result = mapper.toDomain(dto)

            // Then
            assertThat(result.label).isEqualTo("Démocratie")
        }

        @Test
        fun `toDomain - when valid dto - should map pictogramme as picto`() {
            // Given
            val dto = buildThematiqueStrapiDTO(pictogramme = "🗳")

            // When
            val result = mapper.toDomain(dto)

            // Then
            assertThat(result.picto).isEqualTo("🗳")
        }
    }

    @Nested
    inner class `toDomain - StrapiDTO list` {

        @Test
        fun `toDomain - when empty list - should return empty list`() {
            // Given
            val strapiDTO = buildStrapiDTO(items = emptyList())

            // When
            val result = mapper.toDomain(strapiDTO)

            // Then
            assertThat(result).isEmpty()
        }

        @Test
        fun `toDomain - when list with two items - should return two domain objects`() {
            // Given
            val strapiDTO = buildStrapiDTO(
                items = listOf(
                    buildThematiqueStrapiDTO(documentId = "thema-1", label = "Santé", pictogramme = "🏥"),
                    buildThematiqueStrapiDTO(documentId = "thema-2", label = "Éducation", pictogramme = "📚"),
                )
            )

            // When
            val result = mapper.toDomain(strapiDTO)

            // Then
            assertThat(result).hasSize(2)
            assertThat(result[0].id).isEqualTo("thema-1")
            assertThat(result[0].label).isEqualTo("Santé")
            assertThat(result[1].id).isEqualTo("thema-2")
            assertThat(result[1].label).isEqualTo("Éducation")
        }

        @Test
        fun `toDomain - when list with one item - should correctly map all fields`() {
            // Given
            val strapiDTO = buildStrapiDTO(
                items = listOf(
                    buildThematiqueStrapiDTO(documentId = "thema-abc", label = "Autonomie", pictogramme = "👵"),
                )
            )

            // When
            val result = mapper.toDomain(strapiDTO)

            // Then
            assertThat(result).hasSize(1)
            val thematique = result[0]
            assertThat(thematique.id).isEqualTo("thema-abc")
            assertThat(thematique.label).isEqualTo("Autonomie")
            assertThat(thematique.picto).isEqualTo("👵")
        }
    }

    private fun buildThematiqueStrapiDTO(
        documentId: String = "thema-1",
        label: String = "Démocratie",
        pictogramme: String = "🗳",
    ) = ThematiqueStrapiDTO(
        documentId = documentId,
        label = label,
        pictogramme = pictogramme,
    )

    private fun buildStrapiDTO(items: List<ThematiqueStrapiDTO>): StrapiDTO<ThematiqueStrapiDTO> {
        return StrapiDTO(
            data = items,
            meta = StrapiMetadata(StrapiMetaPagination(page = 1, pageSize = 100, pageCount = 1, total = items.size))
        )
    }
}
