package fr.gouv.agora.infrastructure.ficheInventaire

import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiMediaPicture
import fr.gouv.agora.infrastructure.common.StrapiMediaPictureFormatMedium
import fr.gouv.agora.infrastructure.common.StrapiMediaPictureFormats
import fr.gouv.agora.infrastructure.common.StrapiMetadata
import fr.gouv.agora.infrastructure.common.StrapiMetaPagination
import fr.gouv.agora.infrastructure.common.StrapiRichText
import fr.gouv.agora.infrastructure.thematique.dto.ThematiqueStrapiDTO
import fr.gouv.agora.infrastructure.thematique.repository.ThematiqueMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
internal class FicheInventaireRepositoryImplTest {

    @InjectMocks
    private lateinit var repository: FicheInventaireRepositoryImpl

    @Mock
    private lateinit var ficheInventaireStrapiRepository: FicheInventaireStrapiRepository

    @Mock
    private lateinit var thematiqueMapper: ThematiqueMapper

    private val thematiqueStrapiDTO = ThematiqueStrapiDTO(
        documentId = "thema-1",
        label = "Démocratie",
        pictogramme = "🗳",
    )
    private val thematiqueDomain = Thematique(id = "thema-1", label = "Démocratie", picto = "🗳")

    @BeforeEach
    fun setUp() {
        given(thematiqueMapper.toDomain(thematiqueStrapiDTO)).willReturn(thematiqueDomain)
    }

    @Nested
    inner class `getAll` {

        @Test
        fun `getAll - when strapi returns empty list - should return empty list`() {
            // Given
            val filters = FicheInventaireFilters()
            val emptyStrapiDTO = StrapiDTO<FicheInventaireStrapiDTO>(
                data = emptyList(),
                meta = StrapiMetadata(StrapiMetaPagination(1, 100, 0, 0))
            )
            given(ficheInventaireStrapiRepository.getFichesInventaire(filters)).willReturn(emptyStrapiDTO)

            // When
            val result = repository.getAll(filters)

            // Then
            assertThat(result).isEmpty()
        }

        @Test
        fun `getAll - when strapi returns fiches - should return mapped domain objects`() {
            // Given
            val filters = FicheInventaireFilters()
            val fiche = buildFicheInventaireDTO(documentId = "fiche-1", titre = "Ma fiche")
            val strapiDTO = buildStrapiDTO(listOf(fiche))
            given(ficheInventaireStrapiRepository.getFichesInventaire(filters)).willReturn(strapiDTO)

            // When
            val result = repository.getAll(filters)

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].id).isEqualTo("fiche-1")
            assertThat(result[0].titre).isEqualTo("Ma fiche")
        }

        @Test
        fun `getAll - when strapi returns fiche - should call thematiqueMapper with the thematique dto`() {
            // Given
            val filters = FicheInventaireFilters()
            val fiche = buildFicheInventaireDTO()
            given(ficheInventaireStrapiRepository.getFichesInventaire(filters)).willReturn(buildStrapiDTO(listOf(fiche)))

            // When
            repository.getAll(filters)

            // Then
            then(thematiqueMapper).should().toDomain(thematiqueStrapiDTO)
        }

        @Test
        fun `getAll - when strapi returns fiche - should map thematique from mapper result`() {
            // Given
            val filters = FicheInventaireFilters()
            val fiche = buildFicheInventaireDTO()
            given(ficheInventaireStrapiRepository.getFichesInventaire(filters)).willReturn(buildStrapiDTO(listOf(fiche)))

            // When
            val result = repository.getAll(filters)

            // Then
            assertThat(result[0].thematique).isEqualTo(thematiqueDomain)
        }
    }

    @Nested
    inner class `get` {

        @Test
        fun `get - when strapi returns null - should return null`() {
            // Given
            given(ficheInventaireStrapiRepository.getFicheInventaire("fiche-unknown")).willReturn(null)

            // When
            val result = repository.get("fiche-unknown")

            // Then
            assertThat(result).isNull()
        }

        @Test
        fun `get - when strapi returns fiche - should return mapped domain object`() {
            // Given
            val fiche = buildFicheInventaireDTO(documentId = "fiche-42", titre = "Fiche inventaire")
            given(ficheInventaireStrapiRepository.getFicheInventaire("fiche-42")).willReturn(fiche)

            // When
            val result = repository.get("fiche-42")

            // Then
            assertThat(result).isNotNull
            assertThat(result!!.id).isEqualTo("fiche-42")
            assertThat(result.titre).isEqualTo("Fiche inventaire")
        }
    }

    @Nested
    inner class `toFicheInventaire - illustration` {

        @Test
        fun `get - when illustration has medium format - should use medium url`() {
            // Given
            val illustration = StrapiMediaPicture(
                formats = StrapiMediaPictureFormats(medium = StrapiMediaPictureFormatMedium(url = "https://medium.jpg")),
                pictureUrlNotOptimized = "https://original.jpg"
            )
            val fiche = buildFicheInventaireDTO(illustration = illustration)
            given(ficheInventaireStrapiRepository.getFicheInventaire("fiche-1")).willReturn(fiche)

            // When
            val result = repository.get("fiche-1")

            // Then
            assertThat(result!!.illustration).isEqualTo("https://medium.jpg")
        }

        @Test
        fun `get - when illustration has no medium format - should use raw url`() {
            // Given
            val illustration = StrapiMediaPicture(
                formats = StrapiMediaPictureFormats(medium = null),
                pictureUrlNotOptimized = "https://original.jpg"
            )
            val fiche = buildFicheInventaireDTO(illustration = illustration)
            given(ficheInventaireStrapiRepository.getFicheInventaire("fiche-1")).willReturn(fiche)

            // When
            val result = repository.get("fiche-1")

            // Then
            assertThat(result!!.illustration).isEqualTo("https://original.jpg")
        }
    }

    private fun buildFicheInventaireDTO(
        documentId: String = "fiche-1",
        titre: String = "Fiche test",
        illustration: StrapiMediaPicture = StrapiMediaPicture(
            formats = null,
            pictureUrlNotOptimized = "https://illustration.jpg"
        ),
    ) = FicheInventaireStrapiDTO(
        documentId = documentId,
        etapeLancement = emptyList<StrapiRichText>(),
        etapeAnalyse = emptyList<StrapiRichText>(),
        etapeSuivi = emptyList<StrapiRichText>(),
        titre = titre,
        debut = LocalDate.of(2024, 1, 1),
        fin = LocalDate.of(2024, 12, 31),
        porteur = "Ministère test",
        lienSite = "https://site.fr",
        conditionParticipation = "Être citoyen",
        modaliteParticipation = "En ligne",
        thematique = thematiqueStrapiDTO,
        illustration = illustration,
        etape = "En cours",
        anneeDeLancement = "2024",
        type = "Consultation",
    )

    private fun buildStrapiDTO(items: List<FicheInventaireStrapiDTO>): StrapiDTO<FicheInventaireStrapiDTO> {
        return StrapiDTO(
            data = items,
            meta = StrapiMetadata(StrapiMetaPagination(page = 1, pageSize = 100, pageCount = 1, total = items.size))
        )
    }
}
