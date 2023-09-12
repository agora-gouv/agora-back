package fr.social.gouv.agora.infrastructure.question.repository

import fr.social.gouv.agora.domain.Question
import fr.social.gouv.agora.domain.QuestionMultipleChoices
import fr.social.gouv.agora.domain.QuestionUniqueChoice
import fr.social.gouv.agora.infrastructure.question.dto.ChoixPossibleDTO
import fr.social.gouv.agora.infrastructure.question.dto.QuestionDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class QuestionRepositoryImplTest {

    @Autowired
    private lateinit var repository: QuestionRepositoryImpl

    @MockBean
    private lateinit var questionCacheRepository: QuestionCacheRepository

    @MockBean
    private lateinit var questionDatabaseRepository: QuestionDatabaseRepository

    @MockBean
    private lateinit var choixPossibleCacheRepository: ChoixPossibleCacheRepository

    @MockBean
    private lateinit var choixPossibleDatabaseRepository: ChoixPossibleDatabaseRepository

    @MockBean
    private lateinit var mapper: QuestionMapper

    @Test
    fun `getQuestionConsultation - when invalid consultation UUID - should return emptyList`() {
        // When
        val result = repository.getConsultationQuestionList(consultationId = "Invalid consultationId UUID")

        // Then
        assertThat(result).isEqualTo(emptyList<Question>())
        then(questionDatabaseRepository).shouldHaveNoInteractions()
        then(choixPossibleDatabaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getQuestionConsultation - when questions & choixpossible cache are initialized - should return mapped dtos`() {
        // Given
        val consultationUUID = UUID.randomUUID()
        val questionUUID = UUID.randomUUID()

        val questionDTO = mock(QuestionDTO::class.java).also {
            given(it.id).willReturn(questionUUID)
        }
        given(questionCacheRepository.getQuestionList(consultationId = consultationUUID))
            .willReturn(QuestionCacheRepository.CacheResult.CachedQuestionList(listOf(questionDTO)))

        val choixPossibleDTO = mock(ChoixPossibleDTO::class.java)
        given(choixPossibleCacheRepository.getChoixPossibleList(questionId = questionUUID))
            .willReturn(ChoixPossibleCacheRepository.CacheResult.CachedChoixPossibleList(listOf(choixPossibleDTO)))

        val question = mock(QuestionUniqueChoice::class.java)
        given(
            mapper.toDomain(
                dto = questionDTO,
                questionDTOList = listOf(questionDTO),
                choixPossibleDTOList = listOf(choixPossibleDTO),
            )
        ).willReturn(question)

        // When
        val result = repository.getConsultationQuestionList(consultationId = consultationUUID.toString())

        // Then
        assertThat(result).isEqualTo(listOf(question))
        then(questionCacheRepository).should(only()).getQuestionList(consultationId = consultationUUID)
        then(questionDatabaseRepository).shouldHaveNoInteractions()
        then(choixPossibleCacheRepository).should(only()).getChoixPossibleList(questionId = questionUUID)
        then(choixPossibleDatabaseRepository).shouldHaveNoInteractions()
        then(mapper).should(only()).toDomain(
            dto = questionDTO,
            questionDTOList = listOf(questionDTO),
            choixPossibleDTOList = listOf(choixPossibleDTO),
        )
    }

    @Test
    fun `getQuestionConsultation - when questions & choixpossible cache not initialized - should insert result from database to cache then return mapped dtos`() {
        // Given
        val consultationUUID = UUID.randomUUID()
        val questionUUID = UUID.randomUUID()

        val questionDTO = mock(QuestionDTO::class.java).also {
            given(it.id).willReturn(questionUUID)
        }
        given(questionCacheRepository.getQuestionList(consultationId = consultationUUID))
            .willReturn(QuestionCacheRepository.CacheResult.CacheNotInitialized)
        given(questionDatabaseRepository.getQuestionConsultation(consultationId = consultationUUID))
            .willReturn(listOf(questionDTO))

        val choixPossibleDTO = mock(ChoixPossibleDTO::class.java)
        given(choixPossibleCacheRepository.getChoixPossibleList(questionId = questionUUID))
            .willReturn(ChoixPossibleCacheRepository.CacheResult.CacheNotInitialized)
        given(choixPossibleDatabaseRepository.getChoixPossibleQuestion(questionId = questionUUID))
            .willReturn(listOf(choixPossibleDTO))

        val question = mock(QuestionMultipleChoices::class.java)
        given(
            mapper.toDomain(
                dto = questionDTO,
                questionDTOList = listOf(questionDTO),
                choixPossibleDTOList = listOf(choixPossibleDTO),
            )
        ).willReturn(question)

        // When
        val result = repository.getConsultationQuestionList(consultationId = consultationUUID.toString())

        // Then
        assertThat(result).isEqualTo(listOf(question))
        then(questionCacheRepository).should().getQuestionList(consultationId = consultationUUID)
        then(questionCacheRepository).should().insertQuestionList(
            consultationId = consultationUUID,
            questionDTOList = listOf(questionDTO),
        )
        then(questionCacheRepository).shouldHaveNoMoreInteractions()
        then(questionDatabaseRepository).should(only()).getQuestionConsultation(consultationId = consultationUUID)
        then(choixPossibleCacheRepository).should().getChoixPossibleList(questionId = questionUUID)
        then(choixPossibleCacheRepository).should().insertChoixPossibleList(
            questionId = questionUUID,
            choixPossibleDTOList = listOf(choixPossibleDTO),
        )
        then(choixPossibleCacheRepository).shouldHaveNoMoreInteractions()
        then(choixPossibleDatabaseRepository).should(only()).getChoixPossibleQuestion(questionId = questionUUID)
        then(mapper).should(only()).toDomain(
            dto = questionDTO,
            questionDTOList = listOf(questionDTO),
            choixPossibleDTOList = listOf(choixPossibleDTO),
        )
    }

}