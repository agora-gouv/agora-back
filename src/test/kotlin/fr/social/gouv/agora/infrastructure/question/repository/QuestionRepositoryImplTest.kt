package fr.social.gouv.agora.infrastructure.question.repository

import fr.social.gouv.agora.domain.Question
import fr.social.gouv.agora.domain.QuestionChoixUnique
import fr.social.gouv.agora.infrastructure.question.dto.ChoixPossibleDTO
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

    private val choixPossibleDTO = ChoixPossibleDTO(
        id = UUID.randomUUID(),
        label = "domain-label",
        ordre = 1,
        questionId = UUID.randomUUID(),
    )

    private val question = QuestionChoixUnique(
        id = "a29255f2-10ca-4be5-aab1-801ea1733310",
        title = "domain-label",
        order = 1,
        consultationId = "domain-id-consultation",
        choixPossibleList = emptyList(),
    )

    private val questionDTO = QuestionDTO(
        id = UUID.randomUUID(),
        title = "dto-label",
        ordre = 1,
        type = "dto-type",
        description = null,
        maxChoices = 2,
        consultationId = UUID.randomUUID(),
    )

    @BeforeEach
    fun setUp() {
        cacheManager.getCache(QUESTION_CACHE_NAME)?.clear()
        cacheManager.getCache(CHOIX_POSSIBLE_CACHE_NAME)?.clear()
    }

    @Test
    fun `getQuestionConsultation - when invalid UUID - should return emptyList`() {
        // When
        val result = questionRepository.getConsultationQuestionList("1234")

        // Then
        assertThat(result).isEqualTo(emptyList<Question>())
        then(questionDatabaseRepository).shouldHaveNoInteractions()
        then(choixPossibleDatabaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getQuestionConsultation - when questionDatabaseRepository return null and has no cache - should return emptyList`() {
        // Given
        val uuid = UUID.fromString("c29255f2-10ca-4be5-aab1-801ea173337c")
        given(questionDatabaseRepository.getQuestionConsultation(uuid)).willReturn(null)

        // When
        val result = questionRepository.getConsultationQuestionList(uuid.toString())

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
        questionRepository.getConsultationQuestionList(uuid.toString())
        val result = questionRepository.getConsultationQuestionList(uuid.toString())

        // Then
        assertThat(result).isEqualTo(emptyList<Question>())
        then(questionDatabaseRepository).should(times(1)).getQuestionConsultation(uuid)
        then(choixPossibleDatabaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getQuestionConsultation - when questionDatabaseRepository return emptyList and has no cache - should return emptyList`() {
        // Given
        val uuid = UUID.fromString("c29255f2-10ca-4be5-aab1-801ea173337c")
        given(questionDatabaseRepository.getQuestionConsultation(uuid)).willReturn(emptyList())

        // When
        val result = questionRepository.getConsultationQuestionList(uuid.toString())

        // Then
        assertThat(result).isEqualTo(emptyList<Question>())
        then(questionDatabaseRepository).should(only()).getQuestionConsultation(uuid)
        then(choixPossibleDatabaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultation - when questionDatabaseRepository return emptyList and has cache - should return emptyList`() {
        // Given
        val uuid = UUID.fromString("c29255f2-10ca-4be5-aab1-801ea173337c")
        given(questionDatabaseRepository.getQuestionConsultation(uuid)).willReturn(emptyList())

        // When
        questionRepository.getConsultationQuestionList(uuid.toString())
        val result = questionRepository.getConsultationQuestionList(uuid.toString())

        // Then
        assertThat(result).isEqualTo(emptyList<Question>())
        then(questionDatabaseRepository).should(times(1)).getQuestionConsultation(uuid)
        then(choixPossibleDatabaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getQuestionConsultation - when found and has no cache - should return parsed dto from databaseRepository`() {
        // Given
        val uuid = UUID.fromString("c29255f2-10ac-4be5-aab1-801ea173337c")
        given(questionDatabaseRepository.getQuestionConsultation(uuid)).willReturn(listOf(questionDTO))
        given(choixPossibleDatabaseRepository.getChoixPossibleQuestion(questionDTO.id)).willReturn(
            listOf(
                choixPossibleDTO
            )
        )
        given(questionMapper.toDomain(questionDTO, listOf(choixPossibleDTO))).willReturn(question)

        // When
        val result = questionRepository.getConsultationQuestionList(uuid.toString())

        // Then
        assertThat(result).isEqualTo(listOf(question))
        then(questionDatabaseRepository).should(only()).getQuestionConsultation(uuid)
        then(choixPossibleDatabaseRepository).should(only()).getChoixPossibleQuestion(questionDTO.id)
    }

    @Test
    fun `getQuestionConsultation - when found and has cache - should return parsed entity from cache`() {
        // Given
        val uuid = UUID.fromString("c29255f2-10ac-4be5-aab1-801ea173337c")
        given(questionDatabaseRepository.getQuestionConsultation(uuid)).willReturn(listOf(questionDTO))
        given(choixPossibleDatabaseRepository.getChoixPossibleQuestion(questionDTO.id)).willReturn(
            listOf(
                choixPossibleDTO
            )
        )
        given(questionMapper.toDomain(questionDTO, listOf(choixPossibleDTO))).willReturn(question)

        // When
        questionRepository.getConsultationQuestionList(uuid.toString())
        val result = questionRepository.getConsultationQuestionList(uuid.toString())

        // Then
        assertThat(result).isEqualTo(listOf(question))
        then(questionDatabaseRepository).should(times(1)).getQuestionConsultation(uuid)
        then(choixPossibleDatabaseRepository).should(times(1)).getChoixPossibleQuestion(questionDTO.id)
    }

}