package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationAnalyseDesReponses
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
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
internal class ConsultationUpdateInfoV2MapperTest {

    private lateinit var mapper: ConsultationUpdateInfoV2Mapper

    private val fixedNow = LocalDateTime.of(2026, 6, 1, 12, 0, 0)
    private val pastDate = fixedNow.minusDays(10)
    private val futureDate = fixedNow.plusDays(10)

    @BeforeEach
    fun setUp() {
        mapper = ConsultationUpdateInfoV2Mapper()
    }

    @Nested
    inner class `toDomainUnanswered` {

        @Test
        fun `toDomainUnanswered - should use contenuAvantReponse documentId as id`() {
            // Given
            val consultation = buildConsultationDTO(contenuAvantReponseDocumentId = "avant-rep-123")

            // When
            val result = mapper.toDomainUnanswered(consultation)

            // Then
            assertThat(result.id).isEqualTo("avant-rep-123")
        }

        @Test
        fun `toDomainUnanswered - should have hasQuestionsInfo true`() {
            // Given
            val consultation = buildConsultationDTO()

            // When
            val result = mapper.toDomainUnanswered(consultation)

            // Then
            assertThat(result.hasQuestionsInfo).isTrue()
        }

        @Test
        fun `toDomainUnanswered - should have hasParticipationInfo false`() {
            // Given
            val consultation = buildConsultationDTO()

            // When
            val result = mapper.toDomainUnanswered(consultation)

            // Then
            assertThat(result.hasParticipationInfo).isFalse()
        }

        @Test
        fun `toDomainUnanswered - should have no downloadAnalysisUrl`() {
            // Given
            val consultation = buildConsultationDTO()

            // When
            val result = mapper.toDomainUnanswered(consultation)

            // Then
            assertThat(result.downloadAnalysisUrl).isNull()
        }

    }

    @Nested
    inner class `toDomainAnsweredOrEnded` {

        @Test
        fun `toDomainAnsweredOrEnded - should use contenuApresReponse documentId as id`() {
            // Given
            val consultation = buildConsultationDTO(contenuApresReponseDocumentId = "apres-rep-456")

            // When
            val result = mapper.toDomainAnsweredOrEnded(consultation, fixedNow)

            // Then
            assertThat(result!!.id).isEqualTo("apres-rep-456")
        }

        @Test
        fun `toDomainAnsweredOrEnded - when consultation ended - should include responsesInfo with flamme emoji`() {
            // Given
            val consultation = buildConsultationDTO(dateDeFin = pastDate)

            // When
            val result = mapper.toDomainAnsweredOrEnded(consultation, fixedNow)

            // Then
            assertThat(result!!.responsesInfo).isNotNull
            assertThat(result.responsesInfo!!.picto).isEqualTo("🏁")
        }

        @Test
        fun `toDomainAnsweredOrEnded - when consultation not ended - should include responsesInfo with hands emoji`() {
            // Given
            val consultation = buildConsultationDTO(dateDeFin = futureDate)

            // When
            val result = mapper.toDomainAnsweredOrEnded(consultation, fixedNow)

            // Then
            assertThat(result!!.responsesInfo).isNotNull
            assertThat(result.responsesInfo!!.picto).isEqualTo("🙌")
        }

        @Test
        fun `toDomainAnsweredOrEnded - should have hasQuestionsInfo false`() {
            // Given
            val consultation = buildConsultationDTO()

            // When
            val result = mapper.toDomainAnsweredOrEnded(consultation, fixedNow)

            // Then
            assertThat(result!!.hasQuestionsInfo).isFalse()
        }
    }

    @Nested
    inner class `toDomainAnalyseDesReponses` {

        @Test
        fun `toDomainAnalyseDesReponses - when analyse is null - should return null`() {
            // Given
            val consultation = buildConsultationDTO(analyseDesReponses = null)

            // When
            val result = mapper.toDomainAnalyseDesReponses(consultation)

            // Then
            assertThat(result).isNull()
        }

        @Test
        fun `toDomainAnalyseDesReponses - when analyse is present - should use its documentId as id`() {
            // Given
            val analyse = buildAnalyseDesReponses(documentId = "analyse-789")
            val consultation = buildConsultationDTO(analyseDesReponses = analyse)

            // When
            val result = mapper.toDomainAnalyseDesReponses(consultation)

            // Then
            assertThat(result!!.id).isEqualTo("analyse-789")
        }

        @Test
        fun `toDomainAnalyseDesReponses - when analyse has pdf - should set downloadAnalysisUrl from pdf`() {
            // Given
            val analyse = buildAnalyseDesReponses(pdfUrl = "https://analyse.pdf")
            val consultation = buildConsultationDTO(analyseDesReponses = analyse)

            // When
            val result = mapper.toDomainAnalyseDesReponses(consultation)

            // Then
            assertThat(result!!.downloadAnalysisUrl).isEqualTo("https://analyse.pdf")
        }

        @Test
        fun `toDomainAnalyseDesReponses - when analyse has no pdf - should fallback to lienTelechargementAnalyse`() {
            // Given
            val analyse = buildAnalyseDesReponses(pdfUrl = null, lienTelechargementAnalyse = "https://fallback.pdf")
            val consultation = buildConsultationDTO(analyseDesReponses = analyse)

            // When
            val result = mapper.toDomainAnalyseDesReponses(consultation)

            // Then
            assertThat(result!!.downloadAnalysisUrl).isEqualTo("https://fallback.pdf")
        }

        @Test
        fun `toDomainAnalyseDesReponses - when recapEmoji and recapLabel are present - should include infoHeader`() {
            // Given
            val analyse = buildAnalyseDesReponses(recapEmoji = "🔥", recapLabel = "Résultats disponibles")
            val consultation = buildConsultationDTO(analyseDesReponses = analyse)

            // When
            val result = mapper.toDomainAnalyseDesReponses(consultation)

            // Then
            assertThat(result!!.infoHeader).isNotNull
            assertThat(result.infoHeader!!.picto).isEqualTo("🔥")
            assertThat(result.infoHeader!!.description).isEqualTo("Résultats disponibles")
        }

        @Test
        fun `toDomainAnalyseDesReponses - when recapEmoji is null - should have null infoHeader`() {
            // Given
            val analyse = buildAnalyseDesReponses(recapEmoji = null, recapLabel = "Label")
            val consultation = buildConsultationDTO(analyseDesReponses = analyse)

            // When
            val result = mapper.toDomainAnalyseDesReponses(consultation)

            // Then
            assertThat(result!!.infoHeader).isNull()
        }
    }

    @Nested
    inner class `toDomainReponseDuCommanditaire` {

        @Test
        fun `toDomainReponseDuCommanditaire - when commanditaire is null - should return null`() {
            // Given
            val consultation = buildConsultationDTO(reponseDuCommanditaire = null)

            // When
            val result = mapper.toDomainReponseDuCommanditaire(consultation)

            // Then
            assertThat(result).isNull()
        }

        @Test
        fun `toDomainReponseDuCommanditaire - when commanditaire is present - should use its documentId as id`() {
            // Given
            val commanditaire = buildReponseDuCommanditaire(documentId = "commanditaire-101")
            val consultation = buildConsultationDTO(reponseDuCommanditaire = commanditaire)

            // When
            val result = mapper.toDomainReponseDuCommanditaire(consultation)

            // Then
            assertThat(result!!.id).isEqualTo("commanditaire-101")
        }

        @Test
        fun `toDomainReponseDuCommanditaire - should have feedbackQuestion`() {
            // Given
            val commanditaire = buildReponseDuCommanditaire()
            val consultation = buildConsultationDTO(reponseDuCommanditaire = commanditaire)

            // When
            val result = mapper.toDomainReponseDuCommanditaire(consultation)

            // Then
            assertThat(result!!.feedbackQuestion).isNotNull
        }
    }

    @Nested
    inner class `toDomainContenuAutre` {

        @Test
        fun `toDomainContenuAutre - should use contenuAutre documentId as id`() {
            // Given
            val consultation = buildConsultationDTO()
            val contenuAutre = buildContenuAutre(documentId = "autre-202")

            // When
            val result = mapper.toDomainContenuAutre(consultation, contenuAutre)

            // Then
            assertThat(result.id).isEqualTo("autre-202")
        }

        @Test
        fun `toDomainContenuAutre - should have feedbackQuestion`() {
            // Given
            val consultation = buildConsultationDTO()
            val contenuAutre = buildContenuAutre()

            // When
            val result = mapper.toDomainContenuAutre(consultation, contenuAutre)

            // Then
            assertThat(result.feedbackQuestion).isNotNull
        }

        @Test
        fun `toDomainContenuAutre - should have hasQuestionsInfo false`() {
            // Given
            val consultation = buildConsultationDTO()
            val contenuAutre = buildContenuAutre()

            // When
            val result = mapper.toDomainContenuAutre(consultation, contenuAutre)

            // Then
            assertThat(result.hasQuestionsInfo).isFalse()
        }
    }

    // ---- Helpers ----

    private fun buildConsultationDTO(
        dateDeFin: LocalDateTime = futureDate,
        contenuAvantReponseDocumentId: String = "avant-rep-1",
        contenuApresReponseDocumentId: String = "apres-rep-1",
        analyseDesReponses: StrapiConsultationAnalyseDesReponses? = null,
        reponseDuCommanditaire: StrapiConsultationReponseCommanditaire? = null,
    ): ConsultationStrapiDTO = ConsultationStrapiDTO(
        documentId = "consult-1",
        titre = "Consultation test",
        slug = "consultation-test",
        dateDeDebut = pastDate,
        dateDeFin = dateDeFin,
        urlImageDeCouverture = "https://cover.jpg",
        urlImagePageDeContenu = "https://page.jpg",
        nombreDeQuestion = 5,
        estimationNombreDeQuestions = "5",
        estimationTemps = "5 minutes",
        nombreParticipantsCible = 1000,
        thematique = ThematiqueStrapiDTO(documentId = "thema-1", label = "Démocratie", pictogramme = "🗳"),
        questions = emptyList(),
        contenuAvantReponse = buildContenuAvantReponse(documentId = contenuAvantReponseDocumentId),
        contenuApresReponseOuTerminee = buildContenuApresReponse(documentId = contenuApresReponseDocumentId),
        consultationContenuAnalyseDesReponses = analyseDesReponses,
        consultationContenuReponseDuCommanditaire = reponseDuCommanditaire,
        consultationContenuAutres = emptyList(),
        consultationContenuAVenir = null,
        territoire = "France",
        titrePageWeb = "Titre web",
        sousTitrePageWeb = "Sous-titre web",
        imageDeCouverture = null,
        imagePageDeContenu = null,
    )

    private fun buildContenuAvantReponse(documentId: String = "avant-rep-1") =
        StrapiConsultationContenuAvantReponse(
            documentId = documentId,
            slug = "avant-reponse",
            templatePartage = "Partage",
            historiqueTitre = "Avant réponse",
            historiqueCallToAction = "Participez",
            commanditaire = emptyList(),
            objectif = emptyList(),
            axeGouvernemental = emptyList(),
            presentation = emptyList(),
            sections = emptyList(),
        )

    private fun buildContenuApresReponse(documentId: String = "apres-rep-1") =
        StrapiConsultationContenuApresReponse(
            documentId = documentId,
            slug = "apres-reponse",
            templatePartage = "Partage",
            feedbackMessage = "Votre avis",
            historiqueTitre = "Après réponse",
            historiqueCallToAction = "Voir les résultats",
            sections = emptyList(),
        )

    private fun buildContenuAutre(documentId: String = "autre-1") =
        StrapiConsultationContenuAutre(
            documentId = documentId,
            slug = "contenu-autre",
            templatePartage = "Partage",
            feedbackMessage = "Votre avis",
            historiqueTitre = "Autre contenu",
            historiqueCallToAction = "Voir",
            datetimePublication = pastDate,
            sections = emptyList(),
            flammeLabel = "🔥",
            recapEmoji = null,
            recapLabel = null,
        )

    private fun buildAnalyseDesReponses(
        documentId: String = "analyse-1",
        pdfUrl: String? = null,
        lienTelechargementAnalyse: String = "https://lien.pdf",
        recapEmoji: String? = null,
        recapLabel: String? = null,
    ) = StrapiConsultationAnalyseDesReponses(
        documentId = documentId,
        lienTelechargementAnalyse = lienTelechargementAnalyse,
        slug = "analyse",
        templatePartage = "Partage",
        datetimePublication = pastDate,
        feedbackMessage = "Votre avis",
        historiqueTitre = "Analyse",
        historiqueCallToAction = "Télécharger",
        flammeLabel = null,
        sections = emptyList(),
        recapEmoji = recapEmoji,
        recapLabel = recapLabel,
        analysePdf = pdfUrl?.let { fr.gouv.agora.infrastructure.common.StrapiMediaPdf(url = it) },
    )

    private fun buildReponseDuCommanditaire(documentId: String = "commanditaire-1") =
        StrapiConsultationReponseCommanditaire(
            documentId = documentId,
            slug = "reponse-commanditaire",
            templatePartage = "Partage",
            datetimePublication = pastDate,
            feedbackMessage = "Votre avis",
            historiqueTitre = "Réponse commanditaire",
            historiqueCallToAction = "Voir",
            flammeLabel = null,
            sections = emptyList(),
            recapEmoji = null,
            recapLabel = null,
        )
}
