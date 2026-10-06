package fr.gouv.agora.infrastructure.consultation.repository

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import fr.gouv.agora.domain.Territoire
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationAVenir
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
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import java.time.LocalDateTime

/**
 * Tests d'intégration partielle pour ConsultationStrapiCacheRepositoryImpl.
 *
 * On utilise un vrai ConcurrentMapCacheManager (pas de Redis mock) et un vrai ObjectMapper,
 * ce qui permet de vérifier le cycle complet put → get avec la vraie sérialisation/désérialisation.
 *
 * Note : avec l'ancienne implémentation (`get(key, String::class.java)`), les tests
 * "cache hit" échoueraient systématiquement car le type demandé ne correspond pas.
 * Ce test valide donc la correction du bug.
 */
internal class ConsultationStrapiCacheRepositoryImplTest {

    private lateinit var repository: ConsultationStrapiCacheRepositoryImpl

    private val objectMapper = jacksonObjectMapper()
        .registerKotlinModule()
        .registerModule(JavaTimeModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @BeforeEach
    fun setUp() {
        val cacheManager = ConcurrentMapCacheManager(
            ConsultationStrapiCacheRepositoryImpl.ONGOING_CONSULTATIONS_CACHE_NAME,
            ConsultationStrapiCacheRepositoryImpl.FINISHED_CONSULTATIONS_CACHE_NAME,
        )
        repository = ConsultationStrapiCacheRepositoryImpl(
            shortTermCacheManager = cacheManager,
            objectMapper = objectMapper,
        )
    }

    // -------------------------------------------------------------------------
    // Fixture
    // -------------------------------------------------------------------------

    private val contenuAvantReponse = StrapiConsultationContenuAvantReponse(
        documentId = "avant-doc-1",
        slug = "avant-reponse",
        templatePartage = "template",
        historiqueTitre = "Titre historique",
        historiqueCallToAction = "CTA historique",
        commanditaire = emptyList(),
        objectif = emptyList(),
        axeGouvernemental = emptyList(),
        presentation = emptyList(),
        sections = emptyList(),
    )

    private val contenuApresReponse = StrapiConsultationContenuApresReponse(
        documentId = "apres-doc-1",
        slug = "apres-reponse",
        templatePartage = "template",
        feedbackMessage = "Merci !",
        historiqueTitre = "Titre apres",
        historiqueCallToAction = "CTA apres",
        sections = emptyList(),
    )

    private val consultationDto = ConsultationStrapiDTO(
        documentId = "consult-doc-1",
        titre = "Ma consultation de test",
        slug = "ma-consultation-de-test",
        dateDeDebut = LocalDateTime.of(2024, 1, 1, 0, 0),
        dateDeFin = LocalDateTime.of(2024, 12, 31, 23, 59),
        urlImageDeCouverture = "https://example.com/cover.jpg",
        urlImagePageDeContenu = "https://example.com/content.jpg",
        nombreDeQuestion = 5,
        estimationNombreDeQuestions = "5",
        estimationTemps = "5 minutes",
        nombreParticipantsCible = 1000,
        thematique = ThematiqueStrapiDTO(
            documentId = "thema-doc-1",
            label = "Démocratie",
            pictogramme = "🗳",
        ),
        questions = emptyList(),
        contenuAvantReponse = contenuAvantReponse,
        contenuApresReponseOuTerminee = contenuApresReponse,
        consultationContenuAnalyseDesReponses = null,
        consultationContenuReponseDuCommanditaire = null,
        consultationContenuAutres = emptyList(),
        consultationContenuAVenir = null,
        territoire = "national",
        titrePageWeb = "Ma consultation web",
        sousTitrePageWeb = "Sous-titre",
        imageDeCouverture = null,
        imagePageDeContenu = null,
    )

    private val consultationDtoAvecContenuRiche = consultationDto.copy(
        documentId = "consult-doc-2",
        consultationContenuAnalyseDesReponses = StrapiConsultationAnalyseDesReponses(
            documentId = "analyse-doc-1",
            lienTelechargementAnalyse = "https://example.com/analyse.pdf",
            slug = "analyse",
            templatePartage = "template",
            datetimePublication = LocalDateTime.of(2024, 6, 1, 12, 0),
            feedbackMessage = "Feedback analyse",
            historiqueTitre = "Titre analyse",
            historiqueCallToAction = "CTA analyse",
            flammeLabel = "🔥 Analyse disponible",
            sections = emptyList(),
            recapEmoji = "📊",
            recapLabel = "Résultats",
            analysePdf = null,
        ),
        consultationContenuReponseDuCommanditaire = StrapiConsultationReponseCommanditaire(
            documentId = "reponse-doc-1",
            slug = "reponse-commanditaire",
            templatePartage = "template",
            datetimePublication = LocalDateTime.of(2024, 7, 1, 12, 0),
            feedbackMessage = "Feedback réponse",
            historiqueTitre = "Titre réponse",
            historiqueCallToAction = "CTA réponse",
            flammeLabel = null,
            sections = emptyList(),
            recapEmoji = null,
            recapLabel = null,
        ),
        consultationContenuAutres = listOf(
            StrapiConsultationContenuAutre(
                documentId = "autre-doc-1",
                slug = "contenu-autre",
                templatePartage = "template",
                feedbackMessage = "Feedback",
                historiqueTitre = "Titre",
                historiqueCallToAction = "CTA",
                datetimePublication = LocalDateTime.of(2024, 5, 1, 9, 0),
                sections = emptyList(),
                flammeLabel = "🔥",
                recapEmoji = "💬",
                recapLabel = "Mise à jour",
            )
        ),
        consultationContenuAVenir = StrapiConsultationAVenir(titreHistorique = "Bientôt disponible"),
    )

    // -------------------------------------------------------------------------
    // Tests ongoing
    // -------------------------------------------------------------------------

    @Nested
    inner class `getOngoingConsultations` {

        @Test
        fun `getOngoingConsultations - when cache is empty - should return null`() {
            // Given : cache vide (initialisé dans setUp)

            // When
            val result = repository.getOngoingConsultations(emptyList())

            // Then
            assertThat(result).isNull()
        }

        @Test
        fun `getOngoingConsultations - when cache has data - should return the list`() {
            // Given
            val data = listOf(consultationDto)
            repository.putOngoingConsultations(emptyList(), data)

            // When
            val result = repository.getOngoingConsultations(emptyList())

            // Then
            assertThat(result).isNotNull
            assertThat(result).hasSize(1)
            assertThat(result!![0].documentId).isEqualTo("consult-doc-1")
            assertThat(result[0].titre).isEqualTo("Ma consultation de test")
        }

        @Test
        fun `getOngoingConsultations - when cache has data - should correctly deserialize LocalDateTime fields`() {
            // Given
            val data = listOf(consultationDto)
            repository.putOngoingConsultations(emptyList(), data)

            // When
            val result = repository.getOngoingConsultations(emptyList())

            // Then
            assertThat(result).isNotNull
            assertThat(result!![0].dateDeDebut).isEqualTo(LocalDateTime.of(2024, 1, 1, 0, 0))
            assertThat(result[0].dateDeFin).isEqualTo(LocalDateTime.of(2024, 12, 31, 23, 59))
        }

        @Test
        fun `getOngoingConsultations - when cache has data with nested objects - should correctly deserialize all nested fields`() {
            // Given
            val data = listOf(consultationDtoAvecContenuRiche)
            repository.putOngoingConsultations(emptyList(), data)

            // When
            val result = repository.getOngoingConsultations(emptyList())

            // Then
            assertThat(result).isNotNull
            val dto = result!![0]
            assertThat(dto.thematique.documentId).isEqualTo("thema-doc-1")
            assertThat(dto.thematique.label).isEqualTo("Démocratie")
            assertThat(dto.consultationContenuAnalyseDesReponses).isNotNull
            assertThat(dto.consultationContenuAnalyseDesReponses!!.documentId).isEqualTo("analyse-doc-1")
            assertThat(dto.consultationContenuAnalyseDesReponses!!.datetimePublication)
                .isEqualTo(LocalDateTime.of(2024, 6, 1, 12, 0))
            assertThat(dto.consultationContenuReponseDuCommanditaire).isNotNull
            assertThat(dto.consultationContenuAutres).hasSize(1)
            assertThat(dto.consultationContenuAVenir).isNotNull
            assertThat(dto.consultationContenuAVenir!!.titreHistorique).isEqualTo("Bientôt disponible")
        }

        @Test
        fun `getOngoingConsultations - when cache has data for multiple territories - should return correct data per territory key`() {
            // Given
            val dataFrance = listOf(consultationDto)
            val dataIdf = listOf(consultationDtoAvecContenuRiche)
            repository.putOngoingConsultations(emptyList(), dataFrance)
            repository.putOngoingConsultations(listOf(Territoire.Pays.FRANCE), dataIdf)

            // When
            val resultFrance = repository.getOngoingConsultations(emptyList())
            val resultIdf = repository.getOngoingConsultations(listOf(Territoire.Pays.FRANCE))

            // Then
            assertThat(resultFrance!!.map { it.documentId }).containsExactly("consult-doc-1")
            assertThat(resultIdf!!.map { it.documentId }).containsExactly("consult-doc-2")
        }

        @Test
        fun `getOngoingConsultations - when cache has multiple consultations - should return all of them`() {
            // Given
            val data = listOf(consultationDto, consultationDtoAvecContenuRiche)
            repository.putOngoingConsultations(emptyList(), data)

            // When
            val result = repository.getOngoingConsultations(emptyList())

            // Then
            assertThat(result).hasSize(2)
            assertThat(result!!.map { it.documentId }).containsExactlyInAnyOrder("consult-doc-1", "consult-doc-2")
        }
    }

    // -------------------------------------------------------------------------
    // Tests evict ongoing
    // -------------------------------------------------------------------------

    @Nested
    inner class `evictOngoingConsultations` {

        @Test
        fun `evictOngoingConsultations - when evict called after put - should return null on next get`() {
            // Given
            repository.putOngoingConsultations(emptyList(), listOf(consultationDto))
            assertThat(repository.getOngoingConsultations(emptyList())).isNotNull // vérification préalable

            // When
            repository.evictOngoingConsultations()

            // Then
            assertThat(repository.getOngoingConsultations(emptyList())).isNull()
        }

        @Test
        fun `evictOngoingConsultations - when evict called on empty cache - should not throw`() {
            // Given : cache vide

            // When / Then : pas d'exception
            repository.evictOngoingConsultations()
            assertThat(repository.getOngoingConsultations(emptyList())).isNull()
        }

        @Test
        fun `evictOngoingConsultations - when evict called - should evict all territory keys`() {
            // Given
            repository.putOngoingConsultations(emptyList(), listOf(consultationDto))
            repository.putOngoingConsultations(listOf(Territoire.Pays.FRANCE), listOf(consultationDtoAvecContenuRiche))

            // When
            repository.evictOngoingConsultations()

            // Then
            assertThat(repository.getOngoingConsultations(emptyList())).isNull()
            assertThat(repository.getOngoingConsultations(listOf(Territoire.Pays.FRANCE))).isNull()
        }
    }

    // -------------------------------------------------------------------------
    // Tests finished
    // -------------------------------------------------------------------------

    @Nested
    inner class `getFinishedConsultations` {

        @Test
        fun `getFinishedConsultations - when cache is empty - should return null`() {
            // Given : cache vide

            // When
            val result = repository.getFinishedConsultations(emptyList())

            // Then
            assertThat(result).isNull()
        }

        @Test
        fun `getFinishedConsultations - when cache has data - should return the list`() {
            // Given
            val data = listOf(consultationDto)
            repository.putFinishedConsultations(emptyList(), data)

            // When
            val result = repository.getFinishedConsultations(emptyList())

            // Then
            assertThat(result).isNotNull
            assertThat(result).hasSize(1)
            assertThat(result!![0].documentId).isEqualTo("consult-doc-1")
        }

        @Test
        fun `getFinishedConsultations - when cache has data with nested LocalDateTime - should correctly deserialize`() {
            // Given
            val data = listOf(consultationDtoAvecContenuRiche)
            repository.putFinishedConsultations(emptyList(), data)

            // When
            val result = repository.getFinishedConsultations(emptyList())

            // Then
            assertThat(result).isNotNull
            assertThat(result!![0].consultationContenuAutres[0].datetimePublication)
                .isEqualTo(LocalDateTime.of(2024, 5, 1, 9, 0))
        }

        @Test
        fun `getFinishedConsultations - when cache has data for multiple territories - should isolate keys`() {
            // Given
            val dataAll = listOf(consultationDto)
            val dataFrance = listOf(consultationDtoAvecContenuRiche)
            repository.putFinishedConsultations(emptyList(), dataAll)
            repository.putFinishedConsultations(listOf(Territoire.Pays.FRANCE), dataFrance)

            // When
            val resultAll = repository.getFinishedConsultations(emptyList())
            val resultFrance = repository.getFinishedConsultations(listOf(Territoire.Pays.FRANCE))

            // Then
            assertThat(resultAll!!.map { it.documentId }).containsExactly("consult-doc-1")
            assertThat(resultFrance!!.map { it.documentId }).containsExactly("consult-doc-2")
        }
    }

    // -------------------------------------------------------------------------
    // Tests evict finished
    // -------------------------------------------------------------------------

    @Nested
    inner class `evictFinishedConsultations` {

        @Test
        fun `evictFinishedConsultations - when evict called after put - should return null on next get`() {
            // Given
            repository.putFinishedConsultations(emptyList(), listOf(consultationDto))
            assertThat(repository.getFinishedConsultations(emptyList())).isNotNull

            // When
            repository.evictFinishedConsultations()

            // Then
            assertThat(repository.getFinishedConsultations(emptyList())).isNull()
        }

        @Test
        fun `evictFinishedConsultations - when evict called on empty cache - should not throw`() {
            // Given : cache vide

            // When / Then
            repository.evictFinishedConsultations()
            assertThat(repository.getFinishedConsultations(emptyList())).isNull()
        }
    }

    // -------------------------------------------------------------------------
    // Tests isolation ongoing vs finished
    // -------------------------------------------------------------------------

    @Nested
    inner class `ongoing et finished sont isolés` {

        @Test
        fun `putOngoingConsultations - should not affect finished cache`() {
            // Given
            repository.putOngoingConsultations(emptyList(), listOf(consultationDto))

            // When
            val finished = repository.getFinishedConsultations(emptyList())

            // Then
            assertThat(finished).isNull()
        }

        @Test
        fun `putFinishedConsultations - should not affect ongoing cache`() {
            // Given
            repository.putFinishedConsultations(emptyList(), listOf(consultationDto))

            // When
            val ongoing = repository.getOngoingConsultations(emptyList())

            // Then
            assertThat(ongoing).isNull()
        }

        @Test
        fun `evictOngoingConsultations - should not evict finished cache`() {
            // Given
            repository.putOngoingConsultations(emptyList(), listOf(consultationDto))
            repository.putFinishedConsultations(emptyList(), listOf(consultationDtoAvecContenuRiche))

            // When
            repository.evictOngoingConsultations()

            // Then
            assertThat(repository.getOngoingConsultations(emptyList())).isNull()
            assertThat(repository.getFinishedConsultations(emptyList())).isNotNull
            assertThat(repository.getFinishedConsultations(emptyList())!![0].documentId).isEqualTo("consult-doc-2")
        }

        @Test
        fun `evictFinishedConsultations - should not evict ongoing cache`() {
            // Given
            repository.putOngoingConsultations(emptyList(), listOf(consultationDto))
            repository.putFinishedConsultations(emptyList(), listOf(consultationDtoAvecContenuRiche))

            // When
            repository.evictFinishedConsultations()

            // Then
            assertThat(repository.getFinishedConsultations(emptyList())).isNull()
            assertThat(repository.getOngoingConsultations(emptyList())).isNotNull
            assertThat(repository.getOngoingConsultations(emptyList())!![0].documentId).isEqualTo("consult-doc-1")
        }
    }
}
