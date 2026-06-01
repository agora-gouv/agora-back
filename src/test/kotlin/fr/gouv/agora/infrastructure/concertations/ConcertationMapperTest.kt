package fr.gouv.agora.infrastructure.concertations

import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiMediaPicture
import fr.gouv.agora.infrastructure.common.StrapiMediaPictureFormatMedium
import fr.gouv.agora.infrastructure.common.StrapiMediaPictureFormats
import fr.gouv.agora.infrastructure.common.StrapiMetadata
import fr.gouv.agora.infrastructure.common.StrapiMetaPagination
import fr.gouv.agora.infrastructure.thematique.dto.ThematiqueStrapiDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
internal class ConcertationMapperTest {

    private lateinit var mapper: ConcertationMapper

    @BeforeEach
    fun setUp() {
        mapper = ConcertationMapper()
    }

    private val thematiqueDomain = Thematique(id = "thema-1", label = "Démocratie", picto = "🗳")
    private val thematiques = listOf(thematiqueDomain)

    @Nested
    inner class `toConcertations - thematique matching` {

        @Test
        fun `toConcertations - when thematique matches - should include concertation`() {
            // Given
            val dto = buildStrapiDTO(
                listOf(buildConcertationDTO(documentId = "concert-1", thematiqueDocumentId = "thema-1"))
            )

            // When
            val result = mapper.toConcertations(dto, thematiques)

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].id).isEqualTo("concert-1")
        }

        @Test
        fun `toConcertations - when thematique does not match - should exclude concertation`() {
            // Given
            val dto = buildStrapiDTO(
                listOf(buildConcertationDTO(thematiqueDocumentId = "thema-unknown"))
            )

            // When
            val result = mapper.toConcertations(dto, thematiques)

            // Then
            assertThat(result).isEmpty()
        }

        @Test
        fun `toConcertations - when multiple concertations with one unmatched thematique - should only return matched ones`() {
            // Given
            val dto = buildStrapiDTO(
                listOf(
                    buildConcertationDTO(documentId = "concert-1", thematiqueDocumentId = "thema-1"),
                    buildConcertationDTO(documentId = "concert-2", thematiqueDocumentId = "thema-unknown"),
                )
            )

            // When
            val result = mapper.toConcertations(dto, thematiques)

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].id).isEqualTo("concert-1")
        }
    }

    @Nested
    inner class `toConcertations - image url` {

        @Test
        fun `toConcertations - when image is null - should use urlImageDeCouverture as fallback`() {
            // Given
            val dto = buildStrapiDTO(
                listOf(buildConcertationDTO(image = null, urlImageDeCouverture = "https://fallback.jpg"))
            )

            // When
            val result = mapper.toConcertations(dto, thematiques)

            // Then
            assertThat(result[0].imageUrl).isEqualTo("https://fallback.jpg")
        }

        @Test
        fun `toConcertations - when image has medium format - should use medium url`() {
            // Given
            val image = StrapiMediaPicture(
                formats = StrapiMediaPictureFormats(medium = StrapiMediaPictureFormatMedium(url = "https://medium.jpg")),
                pictureUrlNotOptimized = "https://original.jpg"
            )
            val dto = buildStrapiDTO(listOf(buildConcertationDTO(image = image)))

            // When
            val result = mapper.toConcertations(dto, thematiques)

            // Then
            assertThat(result[0].imageUrl).isEqualTo("https://medium.jpg")
        }

        @Test
        fun `toConcertations - when image has no medium format - should use raw picture url`() {
            // Given
            val image = StrapiMediaPicture(
                formats = StrapiMediaPictureFormats(medium = null),
                pictureUrlNotOptimized = "https://original.jpg"
            )
            val dto = buildStrapiDTO(listOf(buildConcertationDTO(image = image)))

            // When
            val result = mapper.toConcertations(dto, thematiques)

            // Then
            assertThat(result[0].imageUrl).isEqualTo("https://original.jpg")
        }
    }

    @Nested
    inner class `toConcertations - field mapping` {

        @Test
        fun `toConcertations - when valid dto - should map all fields correctly`() {
            // Given
            val publicationDate = LocalDateTime.of(2026, 5, 1, 10, 0)
            val dto = buildStrapiDTO(
                listOf(
                    buildConcertationDTO(
                        documentId = "concert-abc",
                        titre = "Ma concertation",
                        urlExterne = "https://example.com",
                        flammeLabel = "🔥 Nouveau",
                        dateDePublication = publicationDate,
                    )
                )
            )

            // When
            val result = mapper.toConcertations(dto, thematiques)

            // Then
            assertThat(result).hasSize(1)
            val concertation = result[0]
            assertThat(concertation.id).isEqualTo("concert-abc")
            assertThat(concertation.title).isEqualTo("Ma concertation")
            assertThat(concertation.externalLink).isEqualTo("https://example.com")
            assertThat(concertation.updateLabel).isEqualTo("🔥 Nouveau")
            assertThat(concertation.updateDate).isEqualTo(publicationDate)
            assertThat(concertation.thematique).isEqualTo(thematiqueDomain)
        }

        @Test
        fun `toConcertations - when flammeLabel is null - should map updateLabel as null`() {
            // Given
            val dto = buildStrapiDTO(listOf(buildConcertationDTO(flammeLabel = null)))

            // When
            val result = mapper.toConcertations(dto, thematiques)

            // Then
            assertThat(result[0].updateLabel).isNull()
        }
    }

    private fun buildConcertationDTO(
        documentId: String = "concert-1",
        titre: String = "Concertation test",
        urlExterne: String = "https://example.com",
        urlImageDeCouverture: String = "https://default.jpg",
        dateDePublication: LocalDateTime = LocalDateTime.of(2026, 1, 1, 0, 0),
        thematiqueDocumentId: String = "thema-1",
        flammeLabel: String? = null,
        image: StrapiMediaPicture? = null,
    ) = ConcertationStrapiDTO(
        documentId = documentId,
        titre = titre,
        urlExterne = urlExterne,
        urlImageDeCouverture = urlImageDeCouverture,
        dateDePublication = dateDePublication,
        thematique = ThematiqueStrapiDTO(documentId = thematiqueDocumentId, label = "Démocratie", pictogramme = "🗳"),
        flammeLabel = flammeLabel,
        image = image,
    )

    private fun buildStrapiDTO(items: List<ConcertationStrapiDTO>): StrapiDTO<ConcertationStrapiDTO> {
        return StrapiDTO(
            data = items,
            meta = StrapiMetadata(StrapiMetaPagination(page = 1, pageSize = 100, pageCount = 1, total = items.size))
        )
    }
}
