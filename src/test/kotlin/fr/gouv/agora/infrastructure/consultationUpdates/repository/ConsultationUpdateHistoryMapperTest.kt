package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.domain.ConsultationUpdateHistoryStatus
import fr.gouv.agora.domain.ConsultationUpdateHistoryType
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationAnalyseDesReponses
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationAVenir
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationContenuApresReponse
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationContenuAutre
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationContenuAvantReponse
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationReponseCommanditaire
import fr.gouv.agora.infrastructure.thematique.dto.ThematiqueStrapiDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneOffset

@ExtendWith(MockitoExtension::class)
internal class ConsultationUpdateHistoryMapperTest {

    private lateinit var mapper: ConsultationUpdateHistoryMapper

    // now = 2026-06-01T12:00:00
    private val fixedNow = LocalDateTime.of(2026, 6, 1, 12, 0, 0)
    private val fixedClock = Clock.fixed(fixedNow.toInstant(ZoneOffset.UTC), ZoneOffset.UTC)

    @BeforeEach
    fun setUp() {
        mapper = ConsultationUpdateHistoryMapper(fixedClock)
    }

    private val pastDate = fixedNow.minusDays(10)
    private val futureDate = fixedNow.plusDays(10)

    @Nested
    inner class `toDomain - contenu avant réponse` {

        @Test
        fun `toDomain - when only contenuAvantReponse - should include it with DONE status`() {
            // Given
            val consultation = buildConsultationDTO(contenuAVenir = null)

            // When
            val result = mapper.toDomain(consultation)

            // Then
            val avantReponse = result.find { it.consultationUpdateId == "avant-rep-1" }
            assertThat(avantReponse).isNotNull
            assertThat(avantReponse!!.status).isEqualTo(ConsultationUpdateHistoryStatus.DONE)
            assertThat(avantReponse.type).isEqualTo(ConsultationUpdateHistoryType.UPDATE)
        }
    }

    @Nested
    inner class `toDomain - contenu a venir` {

        @Test
        fun `toDomain - when contenuAVenir is present - should include it with INCOMING status`() {
            // Given
            val contenuAVenir = StrapiConsultationAVenir(titreHistorique = "Prochain contenu")
            val consultation = buildConsultationDTO(contenuAVenir = contenuAVenir)

            // When
            val result = mapper.toDomain(consultation)

            // Then
            val aVenir = result.find { it.status == ConsultationUpdateHistoryStatus.INCOMING }
            assertThat(aVenir).isNotNull
            assertThat(aVenir!!.title).isEqualTo("Prochain contenu")
            assertThat(aVenir.consultationUpdateId).isNull()
        }

        @Test
        fun `toDomain - when contenuAVenir is null - should not include INCOMING entry`() {
            // Given
            val consultation = buildConsultationDTO(contenuAVenir = null)

            // When
            val result = mapper.toDomain(consultation)

            // Then
            assertThat(result.none { it.status == ConsultationUpdateHistoryStatus.INCOMING }).isTrue()
        }
    }

    @Nested
    inner class `toDomain - contenu autres` {

        @Test
        fun `toDomain - when two contenuAutres in the past - should include the older one with DONE status`() {
            // Given
            val autreOlder = buildContenuAutre(documentId = "autre-older", datetimePublication = pastDate.minusDays(5))
            val autreNewer = buildContenuAutre(documentId = "autre-newer", datetimePublication = pastDate)
            val consultation = buildConsultationDTO(
                contenuAutres = listOf(autreOlder, autreNewer),
                contenuAVenir = null,
            )

            // When
            val result = mapper.toDomain(consultation)

            // Then
            val older = result.find { it.consultationUpdateId == "autre-older" }
            assertThat(older).isNotNull
            // older is not the dernierContenuId (newer is) so it is DONE
            assertThat(older!!.status).isEqualTo(ConsultationUpdateHistoryStatus.DONE)
        }

        @Test
        fun `toDomain - when single contenuAutre in the past - should include it with CURRENT status`() {
            // Given
            val contenuAutre = buildContenuAutre(documentId = "autre-1", datetimePublication = pastDate)
            val consultation = buildConsultationDTO(
                contenuAutres = listOf(contenuAutre),
                contenuAVenir = null,
            )

            // When
            val result = mapper.toDomain(consultation)

            // Then
            val autre = result.find { it.consultationUpdateId == "autre-1" }
            assertThat(autre).isNotNull
            // single past contenu autre is the dernierContenuId → CURRENT
            assertThat(autre!!.status).isEqualTo(ConsultationUpdateHistoryStatus.CURRENT)
        }

        @Test
        fun `toDomain - when contenuAutre in the future - should be filtered out`() {
            // Given
            val contenuAutre = buildContenuAutre(documentId = "autre-future", datetimePublication = futureDate)
            val consultation = buildConsultationDTO(
                contenuAutres = listOf(contenuAutre),
                contenuAVenir = null,
            )

            // When
            val result = mapper.toDomain(consultation)

            // Then
            assertThat(result.none { it.consultationUpdateId == "autre-future" }).isTrue()
        }
    }

    @Nested
    inner class `toDomain - dernierContenuId` {

        @Test
        fun `toDomain - when contenuAutres is not empty - should mark latest as CURRENT`() {
            // Given
            val autreRecent = buildContenuAutre(documentId = "autre-recent", datetimePublication = pastDate)
            val consultation = buildConsultationDTO(
                contenuAutres = listOf(autreRecent),
                contenuAVenir = null,
            )

            // When
            val result = mapper.toDomain(consultation)

            // Then
            val current = result.find { it.status == ConsultationUpdateHistoryStatus.CURRENT }
            assertThat(current).isNotNull
            assertThat(current!!.consultationUpdateId).isEqualTo("autre-recent")
        }

        @Test
        fun `toDomain - when no contenuAutres and no commanditaire and no analyse - should mark contenuApresReponse as CURRENT`() {
            // Given
            // dateDeFin must be in the past so historiqueApresReponse passes the date filter
            val consultation = buildConsultationDTO(
                contenuAutres = emptyList(),
                contenuAVenir = null,
                analyseDesReponses = null,
                reponseDuCommanditaire = null,
                dateDeFin = pastDate.minusDays(5),
            )

            // When
            val result = mapper.toDomain(consultation)

            // Then
            val current = result.find { it.status == ConsultationUpdateHistoryStatus.CURRENT }
            assertThat(current).isNotNull
            assertThat(current!!.consultationUpdateId).isEqualTo("apres-rep-1")
        }
    }

    @Nested
    inner class `toDomain - analyse des réponses` {

        @Test
        fun `toDomain - when analyseDesReponses is present - should include it`() {
            // Given
            val analyse = buildAnalyseDesReponses(documentId = "analyse-1", datetimePublication = pastDate)
            val consultation = buildConsultationDTO(analyseDesReponses = analyse, contenuAVenir = null)

            // When
            val result = mapper.toDomain(consultation)

            // Then
            assertThat(result.any { it.consultationUpdateId == "analyse-1" }).isTrue()
        }

        @Test
        fun `toDomain - when analyseDesReponses is null - should not include it`() {
            // Given
            val consultation = buildConsultationDTO(analyseDesReponses = null, contenuAVenir = null)

            // When
            val result = mapper.toDomain(consultation)

            // Then
            assertThat(result.none { it.consultationUpdateId == "analyse-1" }).isTrue()
        }
    }

    // ---- Helpers ----

    private fun buildConsultationDTO(
        contenuAutres: List<StrapiConsultationContenuAutre> = emptyList(),
        contenuAVenir: StrapiConsultationAVenir? = null,
        analyseDesReponses: StrapiConsultationAnalyseDesReponses? = null,
        reponseDuCommanditaire: StrapiConsultationReponseCommanditaire? = null,
        dateDeFin: LocalDateTime = pastDate.plusDays(30),
    ): ConsultationStrapiDTO = ConsultationStrapiDTO(
        documentId = "consult-1",
        titre = "Consultation test",
        slug = "consultation-test",
        dateDeDebut = pastDate,
        dateDeFin = dateDeFin,
        publishedAt = pastDate,
        urlImageDeCouverture = "https://cover.jpg",
        urlImagePageDeContenu = "https://page.jpg",
        nombreDeQuestion = 5,
        estimationNombreDeQuestions = "5",
        estimationTemps = "5 minutes",
        nombreParticipantsCible = 1000,
        thematique = ThematiqueStrapiDTO(documentId = "thema-1", label = "Démocratie", pictogramme = "🗳"),
        questions = emptyList(),
        contenuAvantReponse = buildContenuAvantReponse(),
        contenuApresReponseOuTerminee = buildContenuApresReponse(),
        consultationContenuAnalyseDesReponses = analyseDesReponses,
        consultationContenuReponseDuCommanditaire = reponseDuCommanditaire,
        consultationContenuAutres = contenuAutres,
        consultationContenuAVenir = contenuAVenir,
        territoire = "France",
        titrePageWeb = "Titre web",
        sousTitrePageWeb = "Sous-titre web",
        imageDeCouverture = null,
        imagePageDeContenu = null,
    )

    private fun buildContenuAvantReponse(
        documentId: String = "avant-rep-1",
        slug: String = "avant-reponse",
    ) = StrapiConsultationContenuAvantReponse(
        documentId = documentId,
        slug = slug,
        templatePartage = "Partage avant réponse",
        historiqueTitre = "Avant réponse",
        historiqueCallToAction = "Participez",
        commanditaire = emptyList(),
        objectif = emptyList(),
        axeGouvernemental = emptyList(),
        presentation = emptyList(),
        sections = emptyList(),
    )

    private fun buildContenuApresReponse(
        documentId: String = "apres-rep-1",
        slug: String = "apres-reponse",
    ) = StrapiConsultationContenuApresReponse(
        documentId = documentId,
        slug = slug,
        templatePartage = "Partage après réponse",
        feedbackMessage = "Votre avis",
        historiqueTitre = "Après réponse",
        historiqueCallToAction = "Voir les résultats",
        sections = emptyList(),
    )

    private fun buildContenuAutre(
        documentId: String = "autre-1",
        datetimePublication: LocalDateTime = pastDate,
    ) = StrapiConsultationContenuAutre(
        documentId = documentId,
        slug = "contenu-autre",
        templatePartage = "Partage",
        feedbackMessage = "Votre avis",
        historiqueTitre = "Autre contenu",
        historiqueCallToAction = "Voir",
        datetimePublication = datetimePublication,
        sections = emptyList(),
        flammeLabel = "🔥",
        recapEmoji = null,
        recapLabel = null,
    )

    private fun buildAnalyseDesReponses(
        documentId: String = "analyse-1",
        datetimePublication: LocalDateTime = pastDate,
    ) = StrapiConsultationAnalyseDesReponses(
        documentId = documentId,
        lienTelechargementAnalyse = "https://pdf.fr",
        slug = "analyse",
        templatePartage = "Partage analyse",
        datetimePublication = datetimePublication,
        feedbackMessage = "Votre avis",
        historiqueTitre = "Analyse",
        historiqueCallToAction = "Télécharger",
        flammeLabel = null,
        sections = emptyList(),
        recapEmoji = null,
        recapLabel = null,
        analysePdf = null,
    )
}
