package fr.gouv.agora.config

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import fr.gouv.agora.infrastructure.common.StrapiRichText
import fr.gouv.agora.infrastructure.common.StrapiRichTextNode
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationContenuApresReponse
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationContenuAvantReponse
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationQuestion
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationQuestionOuverte
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationSection
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationSectionRichText
import fr.gouv.agora.infrastructure.responseQag.dto.StrapiResponseQag
import fr.gouv.agora.infrastructure.responseQag.dto.StrapiResponseQagText
import fr.gouv.agora.infrastructure.responseQag.dto.StrapiResponseQagType
import fr.gouv.agora.infrastructure.thematique.dto.ThematiqueStrapiDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Reproduit et verrouille la correction du bug de prod :
 * "CACHE READ ERROR - ... missing type id property '__component' (for POJO property 'questions')".
 *
 * On utilise le VRAI serializer Redis (GenericJackson2JsonRedisSerializer) avec le meme
 * ObjectMapper que CacheConfig, pour exercer la serialisation/deserialisation reelle
 * (contrairement au ConcurrentMapCacheManager qui ne serialise rien).
 */
class StrapiCacheSerializationTest {

    private fun cacheObjectMapper(): ObjectMapper {
        val jacksonObjectMapper = jacksonObjectMapper()
        return jacksonObjectMapper
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .activateDefaultTyping(jacksonObjectMapper.polymorphicTypeValidator, ObjectMapper.DefaultTyping.EVERYTHING)
    }

    private fun strapiObjectMapper(): ObjectMapper {
        return jacksonObjectMapper()
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    private fun richTextNode(text: String) = StrapiRichTextNode(
        text = text,
        bold = null,
        underline = null,
        italic = null,
        strikethrough = null,
        code = null,
    )

    private fun consultationDto() = ConsultationStrapiDTO(
        documentId = "consult-doc-1",
        titre = "Ma consultation de test",
        slug = "ma-consultation-de-test",
        dateDeDebut = LocalDateTime.of(2024, 1, 1, 0, 0),
        dateDeFin = LocalDateTime.of(2024, 12, 31, 23, 59),
        urlImageDeCouverture = "https://example.com/cover.jpg",
        urlImagePageDeContenu = "https://example.com/content.jpg",
        nombreDeQuestion = 1,
        estimationNombreDeQuestions = "1",
        estimationTemps = "1 minute",
        nombreParticipantsCible = 1000,
        thematique = ThematiqueStrapiDTO(documentId = "thema-doc-1", label = "Democratie", pictogramme = "x"),
        questions = listOf(
            StrapiConsultationQuestionOuverte(
                id = "q1",
                titre = "Question ouverte",
                numero = 1,
                popupExplication = listOf(richTextNode("explication")),
                numeroQuestionSuivante = null,
            )
        ),
        contenuAvantReponse = StrapiConsultationContenuAvantReponse(
            documentId = "avant-doc-1",
            slug = "avant-reponse",
            templatePartage = "template",
            historiqueTitre = "Titre historique",
            historiqueCallToAction = "CTA historique",
            commanditaire = emptyList(),
            objectif = emptyList(),
            axeGouvernemental = emptyList(),
            presentation = emptyList(),
            sections = listOf(
                StrapiConsultationSectionRichText(
                    id = "s1",
                    description = listOf(richTextNode("section riche")),
                )
            ),
        ),
        contenuApresReponseOuTerminee = StrapiConsultationContenuApresReponse(
            documentId = "apres-doc-1",
            slug = "apres-reponse",
            templatePartage = "template",
            feedbackMessage = "Merci !",
            historiqueTitre = "Titre apres",
            historiqueCallToAction = "CTA apres",
            sections = emptyList(),
        ),
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

    @Test
    fun `le serializer de cache relit les questions, sections et rich text polymorphes`() {
        // Given
        val serializer = GenericJackson2JsonRedisSerializer(cacheObjectMapper())
        val original = listOf(consultationDto())

        // When
        val bytes = serializer.serialize(original)
        val json = String(bytes)

        // Then : les type ids doivent etre presents (c'etait la cause du CACHE READ ERROR)
        assertThat(json).contains("__component")
        assertThat(json).contains("question-de-consultation.question-ouverte")
        assertThat(json).contains("consultation-section.section-texte-riche")

        // And : la relecture doit reconstruire les sous-types concrets, y compris imbriques
        @Suppress("UNCHECKED_CAST")
        val result = serializer.deserialize(bytes) as List<ConsultationStrapiDTO>
        assertThat(result).hasSize(1)

        val question = result[0].questions[0]
        assertThat(question).isInstanceOf(StrapiConsultationQuestionOuverte::class.java)
        assertThat((question as StrapiConsultationQuestionOuverte).titre).isEqualTo("Question ouverte")
        assertThat(question.popupExplication).isNotNull
        assertThat(question.popupExplication!![0]).isInstanceOf(StrapiRichTextNode::class.java)
        assertThat((question.popupExplication!![0] as StrapiRichTextNode).text).isEqualTo("explication")

        val section = result[0].contenuAvantReponse.sections[0]
        assertThat(section).isInstanceOf(StrapiConsultationSectionRichText::class.java)
        assertThat((section as StrapiConsultationSectionRichText).description[0]).isInstanceOf(StrapiRichTextNode::class.java)
        assertThat((section.description[0] as StrapiRichTextNode).text).isEqualTo("section riche")
    }

    @Test
    fun `le client Strapi parse toujours un payload contenant __component`() {
        // Given : un JSON identique a celui renvoye par Strapi (avec __component)
        val json = """
            [
              {
                "__component": "question-de-consultation.question-ouverte",
                "id": "q1",
                "titre": "Question ouverte",
                "numero": 1,
                "popup_explication": null,
                "question_suivante": null
              }
            ]
        """.trimIndent()
        val typeRef = object : TypeReference<List<StrapiConsultationQuestion>>() {}

        // When
        val parsed = strapiObjectMapper().readValue(json, typeRef)

        // Then
        assertThat(parsed).hasSize(1)
        assertThat(parsed[0]).isInstanceOf(StrapiConsultationQuestionOuverte::class.java)
        assertThat((parsed[0] as StrapiConsultationQuestionOuverte).titre).isEqualTo("Question ouverte")
    }

    @Test
    fun `le client Strapi parse toujours du rich text contenant type`() {
        // Given : un JSON de rich text Strapi (avec "type")
        val json = """
            [
              { "type": "paragraph", "children": [ { "type": "text", "text": "Bonjour", "bold": false, "underline": false, "italic": false, "strikethrough": false, "code": false } ] }
            ]
        """.trimIndent()
        val typeRef = object : TypeReference<List<StrapiRichText>>() {}

        // When
        val parsed = strapiObjectMapper().readValue(json, typeRef)

        // Then
        assertThat(parsed).hasSize(1)
        assertThat(parsed[0].toHtml()).isEqualTo("<p>Bonjour</p>")
    }

    @Test
    fun `le serializer de cache relit les reponses QaG polymorphes`() {
        // Given
        val serializer = GenericJackson2JsonRedisSerializer(cacheObjectMapper())
        val response = StrapiResponseQag(
            auteur = "Auteur",
            auteurPortraitUrl = "https://example.com/portrait.jpg",
            auteurFonction = null,
            reponseDate = LocalDate.of(2024, 1, 1),
            feedbackQuestion = "Feedback ?",
            questionId = "qid",
            reponseType = listOf(StrapiResponseQagText(label = "Texte", text = listOf(richTextNode("hello")))),
            auteurPortrait = null,
        )

        // When
        val bytes = serializer.serialize(listOf(response))
        @Suppress("UNCHECKED_CAST")
        val result = serializer.deserialize(bytes) as List<StrapiResponseQag>

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].reponseType[0]).isInstanceOf(StrapiResponseQagText::class.java)
        assertThat((result[0].reponseType[0] as StrapiResponseQagText).label).isEqualTo("Texte")
    }
}
