package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiMetaPagination
import fr.gouv.agora.infrastructure.common.StrapiMetadata
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationAVenir
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationContenuApresReponse
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationContenuAvantReponse
import fr.gouv.agora.infrastructure.thematique.dto.ThematiqueStrapiDTO
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.thematique.repository.ThematiqueMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
internal class ConsultationInfoMapperTest {

    @InjectMocks
    private lateinit var mapper: ConsultationInfoMapper

    @Mock
    private lateinit var thematiqueMapper: ThematiqueMapper

    private val fixedNow = LocalDateTime.of(2026, 6, 1, 12, 0, 0)
    private val pastDate = fixedNow.minusDays(10)
    private val futureDate = fixedNow.plusDays(10)

    private val mockThematique = Thematique(id = "thema-1", label = "Démocratie", picto = "🗳")

    @BeforeEach
    fun setUp() {
        given(thematiqueMapper.toDomain(ThematiqueStrapiDTO(documentId = "thema-1", label = "Démocratie", pictogramme = "🗳")))
            .willReturn(mockThematique)
    }

    @Nested
    inner class `toConsultationPreview` {

        @Test
        fun `toConsultationPreview - should use numeric id as string`() {
            // Given
            val dto = buildStrapiDTO(numericId = 42)

            // When
            val result = mapper.toConsultationPreview(dto)

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].id).isEqualTo("42")
        }

        @Test
        fun `toConsultationPreview - should not use documentId`() {
            // Given
            val dto = buildStrapiDTO(numericId = 10, documentId = "abc-doc-id")

            // When
            val result = mapper.toConsultationPreview(dto)

            // Then
            assertThat(result[0].id).isNotEqualTo("abc-doc-id")
        }
    }

    @Nested
    inner class `toDomainFinished` {

        @Test
        fun `toDomainFinished - should use numeric id as string`() {
            // Given
            val dto = buildStrapiDTO(numericId = 99)

            // When
            val result = mapper.toDomainFinished(dto, fixedNow)

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].id).isEqualTo("99")
        }

        @Test
        fun `toDomainFinished - should not use documentId`() {
            // Given
            val dto = buildStrapiDTO(numericId = 7, documentId = "some-document-id")

            // When
            val result = mapper.toDomainFinished(dto, fixedNow)

            // Then
            assertThat(result[0].id).isNotEqualTo("some-document-id")
        }
    }

    @Nested
    inner class `toConsultationInfo` {

        @Test
        fun `toConsultationInfo - should use numeric id as string`() {
            // Given
            val consultation = buildConsultationStrapiDTO(numericId = 123)

            // When
            val result = mapper.toConsultationInfo(consultation)

            // Then
            assertThat(result.id).isEqualTo("123")
        }

        @Test
        fun `toConsultationInfo - should not use documentId`() {
            // Given
            val consultation = buildConsultationStrapiDTO(numericId = 5, documentId = "doc-xyz")

            // When
            val result = mapper.toConsultationInfo(consultation)

            // Then
            assertThat(result.id).isNotEqualTo("doc-xyz")
        }
    }

    @Nested
    inner class `toConsultationsWithUpdateInfo` {

        @Test
        fun `toConsultationsWithUpdateInfo - should use numeric id as string`() {
            // Given
            val dto = buildStrapiDTO(numericId = 55)

            // When
            val result = mapper.toConsultationsWithUpdateInfo(dto, fixedNow)

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].id).isEqualTo("55")
        }

        @Test
        fun `toConsultationsWithUpdateInfo - should not use documentId`() {
            // Given
            val dto = buildStrapiDTO(numericId = 3, documentId = "other-doc")

            // When
            val result = mapper.toConsultationsWithUpdateInfo(dto, fixedNow)

            // Then
            assertThat(result[0].id).isNotEqualTo("other-doc")
        }
    }

    // ---- Helpers ----

    private fun buildStrapiDTO(
        numericId: Int = 1,
        documentId: String = "consult-doc-1",
    ): StrapiDTO<ConsultationStrapiDTO> {
        return StrapiDTO(
            data = listOf(buildConsultationStrapiDTO(numericId = numericId, documentId = documentId)),
            meta = StrapiMetadata(StrapiMetaPagination(1, 100, 1, 1)),
        )
    }

    private fun buildConsultationStrapiDTO(
        numericId: Int = 1,
        documentId: String = "consult-doc-1",
    ): ConsultationStrapiDTO = ConsultationStrapiDTO(
        id = numericId,
        documentId = documentId,
        titre = "Consultation test",
        slug = "consultation-test",
        dateDeDebut = pastDate,
        dateDeFin = futureDate,
        publishedAt = pastDate,
        urlImageDeCouverture = "https://cover.jpg",
        urlImagePageDeContenu = "https://page.jpg",
        nombreDeQuestion = 3,
        estimationNombreDeQuestions = "3",
        estimationTemps = "5 minutes",
        nombreParticipantsCible = 1000,
        thematique = ThematiqueStrapiDTO(documentId = "thema-1", label = "Démocratie", pictogramme = "🗳"),
        questions = emptyList(),
        contenuAvantReponse = StrapiConsultationContenuAvantReponse(
            documentId = "avant-rep-1",
            slug = "avant-reponse",
            templatePartage = "Partage",
            historiqueTitre = "Avant réponse",
            historiqueCallToAction = "Participez",
            commanditaire = emptyList(),
            objectif = emptyList(),
            axeGouvernemental = emptyList(),
            presentation = emptyList(),
            sections = emptyList(),
        ),
        contenuApresReponseOuTerminee = StrapiConsultationContenuApresReponse(
            documentId = "apres-rep-1",
            slug = "apres-reponse",
            templatePartage = "Partage",
            feedbackMessage = "Votre avis",
            historiqueTitre = "Après réponse",
            historiqueCallToAction = "Voir les résultats",
            sections = emptyList(),
        ),
        consultationContenuAnalyseDesReponses = null,
        consultationContenuReponseDuCommanditaire = null,
        consultationContenuAutres = emptyList(),
        consultationContenuAVenir = StrapiConsultationAVenir(titreHistorique = "A venir"),
        territoire = "France",
        titrePageWeb = "Titre web",
        sousTitrePageWeb = "Sous-titre web",
        imageDeCouverture = null,
        imagePageDeContenu = null,
    )
}
