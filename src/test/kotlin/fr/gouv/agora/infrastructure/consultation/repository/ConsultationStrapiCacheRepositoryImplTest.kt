package fr.gouv.agora.infrastructure.consultation.repository

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
internal class ConsultationStrapiCacheRepositoryImplTest {

    @InjectMocks
    private lateinit var repository: ConsultationStrapiCacheRepositoryImpl

    @Mock
    private lateinit var shortTermCacheManager: CacheManager

    @Mock
    private lateinit var objectMapper: ObjectMapper

    @Mock
    private lateinit var ongoingCache: Cache

    @Mock
    private lateinit var finishedCache: Cache

    @BeforeEach
    fun setup() {
        given(shortTermCacheManager.getCache(ConsultationStrapiCacheRepositoryImpl.ONGOING_CONSULTATIONS_CACHE_NAME))
            .willReturn(ongoingCache)
        given(shortTermCacheManager.getCache(ConsultationStrapiCacheRepositoryImpl.FINISHED_CONSULTATIONS_CACHE_NAME))
            .willReturn(finishedCache)
    }

    @Nested
    inner class `getOngoingConsultations - when cache is empty` {

        @Test
        fun `should return null`() {
            // Given
            given(ongoingCache.get("all", String::class.java)).willReturn(null)

            // When
            val result = repository.getOngoingConsultations(emptyList())

            // Then
            assertThat(result).isNull()
        }
    }

    @Nested
    inner class `getOngoingConsultations - when cache has data` {

        @Test
        fun `should return deserialized list`() {
            // Given
            val objectMapperReal = buildObjectMapper()
            val dto = buildConsultationStrapiDTO("id-1")
            val json = objectMapperReal.writeValueAsString(listOf(dto))

            given(ongoingCache.get("all", String::class.java)).willReturn(json)
            given(objectMapper.readValue(json, ConsultationStrapiCacheRepositoryImpl.LIST_TYPE_REF))
                .willReturn(listOf(dto))

            // When
            val result = repository.getOngoingConsultations(emptyList())

            // Then
            assertThat(result).isNotNull
            assertThat(result).hasSize(1)
            assertThat(result!![0].documentId).isEqualTo("id-1")
        }
    }

    @Nested
    inner class `getOngoingConsultations - when territories are provided` {

        @Test
        fun `should use sorted territories as cache key`() {
            // Given
            val territories = listOf<Territoire>(Territoire.Pays.FRANCE)
            given(ongoingCache.get("France", String::class.java)).willReturn(null)

            // When
            val result = repository.getOngoingConsultations(territories)

            // Then
            assertThat(result).isNull()
            then(ongoingCache).should().get("France", String::class.java)
        }
    }

    @Nested
    inner class `putOngoingConsultations - when called` {

        @Test
        fun `should serialize and store in cache`() {
            // Given
            val dto = buildConsultationStrapiDTO("id-1")
            val json = """[{"documentId":"id-1"}]"""
            given(objectMapper.writeValueAsString(listOf(dto))).willReturn(json)

            // When
            repository.putOngoingConsultations(emptyList(), listOf(dto))

            // Then
            then(ongoingCache).should().put("all", json)
        }
    }

    @Nested
    inner class `evictOngoingConsultations - when called` {

        @Test
        fun `should clear the ongoing cache`() {
            // When
            repository.evictOngoingConsultations()

            // Then
            then(ongoingCache).should().clear()
        }
    }

    @Nested
    inner class `getFinishedConsultations - when cache is empty` {

        @Test
        fun `should return null`() {
            // Given
            given(finishedCache.get("all", String::class.java)).willReturn(null)

            // When
            val result = repository.getFinishedConsultations(emptyList())

            // Then
            assertThat(result).isNull()
        }
    }

    @Nested
    inner class `getFinishedConsultations - when cache has data` {

        @Test
        fun `should return deserialized list`() {
            // Given
            val dto = buildConsultationStrapiDTO("id-2")
            val json = """[{"documentId":"id-2"}]"""

            given(finishedCache.get("all", String::class.java)).willReturn(json)
            given(objectMapper.readValue(json, ConsultationStrapiCacheRepositoryImpl.LIST_TYPE_REF))
                .willReturn(listOf(dto))

            // When
            val result = repository.getFinishedConsultations(emptyList())

            // Then
            assertThat(result).isNotNull
            assertThat(result).hasSize(1)
            assertThat(result!![0].documentId).isEqualTo("id-2")
        }
    }

    @Nested
    inner class `putFinishedConsultations - when called` {

        @Test
        fun `should serialize and store in cache`() {
            // Given
            val dto = buildConsultationStrapiDTO("id-2")
            val json = """[{"documentId":"id-2"}]"""
            given(objectMapper.writeValueAsString(listOf(dto))).willReturn(json)

            // When
            repository.putFinishedConsultations(emptyList(), listOf(dto))

            // Then
            then(finishedCache).should().put("all", json)
        }
    }

    @Nested
    inner class `evictFinishedConsultations - when called` {

        @Test
        fun `should clear the finished cache`() {
            // When
            repository.evictFinishedConsultations()

            // Then
            then(finishedCache).should().clear()
        }
    }

    @Nested
    inner class `getOngoingConsultations - when objectMapper throws exception` {

        @Test
        fun `should return null without throwing`() {
            // Given
            val json = "invalid-json"
            given(ongoingCache.get("all", String::class.java)).willReturn(json)
            given(objectMapper.readValue(json, ConsultationStrapiCacheRepositoryImpl.LIST_TYPE_REF))
                .willThrow(RuntimeException("parse error"))

            // When
            val result = repository.getOngoingConsultations(emptyList())

            // Then
            assertThat(result).isNull()
        }
    }

    @Nested
    inner class `putOngoingConsultations - when objectMapper throws exception` {

        @Test
        fun `should not throw`() {
            // Given
            val dto = buildConsultationStrapiDTO("id-1")
            given(objectMapper.writeValueAsString(listOf(dto))).willThrow(RuntimeException("serialization error"))

            // When / Then : should not throw
            repository.putOngoingConsultations(emptyList(), listOf(dto))
            then(ongoingCache).shouldHaveNoMoreInteractions()
        }
    }

    private fun buildObjectMapper(): ObjectMapper {
        return jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    private fun buildConsultationStrapiDTO(documentId: String): ConsultationStrapiDTO {
        val thematique = mock(ThematiqueStrapiDTO::class.java)
        return ConsultationStrapiDTO(
            documentId = documentId,
            titre = "Titre consultation",
            slug = "slug-$documentId",
            dateDeDebut = LocalDateTime.of(2026, 1, 1, 0, 0),
            dateDeFin = LocalDateTime.of(2026, 12, 31, 23, 59),
            urlImageDeCouverture = "https://example.com/cover.jpg",
            urlImagePageDeContenu = "https://example.com/content.jpg",
            nombreDeQuestion = 5,
            estimationNombreDeQuestions = "5 questions",
            estimationTemps = "5 minutes",
            nombreParticipantsCible = 10000,
            thematique = thematique,
            questions = emptyList(),
            contenuAvantReponse = mock(StrapiConsultationContenuAvantReponse::class.java),
            contenuApresReponseOuTerminee = mock(StrapiConsultationContenuApresReponse::class.java),
            consultationContenuAnalyseDesReponses = null,
            consultationContenuReponseDuCommanditaire = null,
            consultationContenuAutres = emptyList(),
            consultationContenuAVenir = null,
            territoire = "France",
            titrePageWeb = "Titre web",
            sousTitrePageWeb = "Sous-titre web",
            imageDeCouverture = null,
            imagePageDeContenu = null,
        )
    }
}
