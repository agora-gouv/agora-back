package fr.gouv.agora.infrastructure.question.repository

import fr.gouv.agora.domain.Question
import fr.gouv.agora.domain.QuestionMultipleChoices
import fr.gouv.agora.domain.QuestionUniqueChoice
import fr.gouv.agora.infrastructure.question.dto.ChoixPossibleDTO
import fr.gouv.agora.infrastructure.question.dto.QuestionDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class QuestionRepositoryImplTest {

    @InjectMocks
    private lateinit var repository: QuestionRepositoryImpl

    @Mock
    private lateinit var questionCacheRepository: QuestionCacheRepository

    @Mock
    private lateinit var questionDatabaseRepository: QuestionDatabaseRepository

    @Mock
    private lateinit var choixPossibleCacheRepository: ChoixPossibleCacheRepository

    @Mock
    private lateinit var choixPossibleDatabaseRepository: ChoixPossibleDatabaseRepository

    @Mock
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