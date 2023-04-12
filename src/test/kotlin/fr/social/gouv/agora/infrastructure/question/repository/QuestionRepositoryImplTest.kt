package fr.social.gouv.agora.infrastructure.question.repository

import fr.social.gouv.agora.domain.ChoixPossible
import fr.social.gouv.agora.domain.Question
import fr.social.gouv.agora.infrastructure.choixpossible.dto.ChoixPossibleDTO
import fr.social.gouv.agora.infrastructure.choixpossible.repository.ChoixPossibleDatabaseRepository
import fr.social.gouv.agora.infrastructure.question.dto.QuestionDTO
import fr.social.gouv.agora.infrastructure.question.repository.QuestionRepositoryImpl.Companion.CHOIX_POSSIBLE_CACHE_NAME
import fr.social.gouv.agora.infrastructure.question.repository.QuestionRepositoryImpl.Companion.QUESTION_CACHE_NAME
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*


@ExtendWith(SpringExtension::class)
@SpringBootTest
@EnableCaching
@ImportAutoConfiguration(
    classes = [
        CacheAutoConfiguration::class,
        RedisAutoConfiguration::class,
    ]
)
internal class QuestionRepositoryImplTest {

    @Autowired
    private lateinit var questionRepository: QuestionRepositoryImpl

    @MockBean
    private lateinit var questionDatabaseRepository: QuestionDatabaseRepository

    @MockBean
    private lateinit var choixPossibleDatabaseRepository: ChoixPossibleDatabaseRepository

    @MockBean
    private lateinit var questionMapper: QuestionMapper

    @Autowired
    @Suppress("unused")
    private lateinit var cacheManager: CacheManager

    private val choixPossible = ChoixPossible(
        id = "1337",
        label = "domain-label",
        ordre = 1,
        id_question = "domain-id-question",
    )

    private val choixPossibleDTO = ChoixPossibleDTO(
        id = UUID.randomUUID(),
        label = "domain-label",
        ordre = 1,
        id_question = UUID.randomUUID(),
    )

    private val question = Question(
        id = "a29255f2-10ca-4be5-aab1-801ea1733310",
        label = "domain-label",
        ordre = 1,
        type = "domain-type",
        id_consultation = "domain-id-consultation",
        listechoix = listOf(choixPossible)
    )

    private val questionDTO = QuestionDTO(
        id = UUID.randomUUID(),
        label = "dto-label",
        ordre = 1,
        type = "dto-type",
        id_consultation = UUID.randomUUID(),
    )
    @BeforeEach
    fun setUp() {
        cacheManager.getCache(QUESTION_CACHE_NAME)?.clear()
        cacheManager.getCache(CHOIX_POSSIBLE_CACHE_NAME)?.clear()
    }

    @Test
    fun `getQuestionConsultation - when invalid UUID - should return null`() {
        // When
        val result = questionRepository.getQuestionConsultation("1234")

        // Then
        assertThat(result).isEqualTo(null)
        then(questionDatabaseRepository).shouldHaveNoInteractions()
        then(choixPossibleDatabaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getQuestionConsultation - when questionDatabaseRepository return null and has no cache - should return emptyList`() {
        // Given
        val uuid = UUID.fromString("c29255f2-10ca-4be5-aab1-801ea173337c")
        given(questionDatabaseRepository.getQuestionConsultation(uuid)).willReturn(null) //

        // When
        val result = questionRepository.getQuestionConsultation(uuid.toString())

        // Then
        assertThat(result).isEqualTo(emptyList<Question>())
        then(questionDatabaseRepository).should(only()).getQuestionConsultation(uuid)
        then(choixPossibleDatabaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultation - when questionDatabaseRepository return null and has cache - should return emptyList`() {
        // Given
        val uuid = UUID.fromString("c29255f2-10ca-4be5-aab1-801ea173337c")
        given(questionDatabaseRepository.getQuestionConsultation(uuid)).willReturn(null)

        // When
        questionRepository.getQuestionConsultation(uuid.toString())
        val result = questionRepository.getQuestionConsultation(uuid.toString())
        // Then
        assertThat(result).isEqualTo(emptyList<Question>())
        then(questionDatabaseRepository).should(times(1)).getQuestionConsultation(uuid)
        then(choixPossibleDatabaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getQuestionConsultation - when questionDatabaseRepository return emptyList and has no cache - should return emptyList`() {
        // Given
        val uuid = UUID.fromString("c29255f2-10ca-4be5-aab1-801ea173337c")
        given(questionDatabaseRepository.getQuestionConsultation(uuid)).willReturn(emptyList<QuestionDTO>())

        // When
        val result = questionRepository.getQuestionConsultation(uuid.toString())

        // Then
        assertThat(result).isEqualTo(emptyList<Question>())
        then(questionDatabaseRepository).should(only()).getQuestionConsultation(uuid)
        then(choixPossibleDatabaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultation - when questionDatabaseRepository return emptyList and has cache - should return emptyList`() {
        // Given
        val uuid = UUID.fromString("c29255f2-10ca-4be5-aab1-801ea173337c")
        given(questionDatabaseRepository.getQuestionConsultation(uuid)).willReturn(emptyList<QuestionDTO>())

        // When
        questionRepository.getQuestionConsultation(uuid.toString())
        val result = questionRepository.getQuestionConsultation(uuid.toString())
        // Then
        assertThat(result).isEqualTo(emptyList<Question>())
        then(questionDatabaseRepository).should(times(1)).getQuestionConsultation(uuid)
        then(choixPossibleDatabaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getQuestionConsultation - when found and has no cache - should return parsed dto from databaseRepository`() {
        // Given
        val uuid = UUID.fromString("c29255f2-10ac-4be5-aab1-801ea173337c")
        //val uuid_question = UUID.fromString("c29255f2-10ac-4be5-aab1-801ea173340c")
        given(questionDatabaseRepository.getQuestionConsultation(uuid)).willReturn(listOf(questionDTO))
        given(choixPossibleDatabaseRepository.getChoixPossibleQuestion(questionDTO.id)).willReturn(listOf(choixPossibleDTO))
        given(questionMapper.toDomain(questionDTO, listOf(choixPossibleDTO))).willReturn(question)
            /*
        given(choixPossibleDatabaseRepository.getChoixPossibleQuestion(uuid_question).willReturn(listOf(choixPossibleDTO))
        given(questionMapper.toDomain(questionDTO)).willReturn(question)*/

        // When
        val result = questionRepository.getQuestionConsultation(uuid.toString())

        // Then
        assertThat(result).isEqualTo(listOf(question))
        then(questionDatabaseRepository).should(only()).getQuestionConsultation(uuid)
        then(choixPossibleDatabaseRepository).should(only()).getChoixPossibleQuestion(questionDTO.id)
    }

    @Test
    fun `getQuestionConsultation - when found and has cache - should return parsed entity from cache`() {
        // Given
        val uuid = UUID.fromString("c29255f2-10ac-4be5-aab1-801ea173337c")
        //val uuid_question = UUID.fromString("c29255f2-10ac-4be5-aab1-801ea173340c")
        given(questionDatabaseRepository.getQuestionConsultation(uuid)).willReturn(listOf(questionDTO))
        given(choixPossibleDatabaseRepository.getChoixPossibleQuestion(questionDTO.id)).willReturn(listOf(choixPossibleDTO))
        given(questionMapper.toDomain(questionDTO, listOf(choixPossibleDTO))).willReturn(question)

        // When
        questionRepository.getQuestionConsultation(uuid.toString())
        val result = questionRepository.getQuestionConsultation(uuid.toString())

        // Then
        assertThat(result).isEqualTo(listOf(question))
        then(questionDatabaseRepository).should(times(1)).getQuestionConsultation(uuid)
        then(choixPossibleDatabaseRepository).should(times(1)).getChoixPossibleQuestion(questionDTO.id)
    }



}