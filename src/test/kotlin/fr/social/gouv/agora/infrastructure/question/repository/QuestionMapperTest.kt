package fr.social.gouv.agora.infrastructure.question.repository

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.infrastructure.question.dto.ChoixPossibleDTO
import fr.social.gouv.agora.infrastructure.question.dto.QuestionDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class QuestionMapperTest {

    @Autowired
    private lateinit var questionMapper: QuestionMapper

    @Test
    fun `toDomain when DTO has type unique should return QuestionChoixUnique`() {
        // Given
        val questionDTOUnique = QuestionDTO(
            id = UUID.randomUUID(),
            title = "dto-label",
            ordre = 1,
            type = "unique",
            description = null,
            maxChoices = null,
            consultationId = UUID.randomUUID(),
        )
        val questionChoixUnique = QuestionChoixUnique(
            id = questionDTOUnique.id.toString(),
            title = "dto-label",
            order = 1,
            consultationId = questionDTOUnique.consultationId.toString(),
            choixPossibleList = emptyList(),
        )

        // When
        val result = questionMapper.toDomain(questionDTOUnique, emptyList())

        // Then
        assertThat(result).isEqualTo(questionChoixUnique)
    }

    @Test
    fun `toDomain when DTO has type unique and list choixPossibleDTO should return QuestionChoixUnique with list choixPossible`() {
        // Given
        val questionDTOUnique = QuestionDTO(
            id = UUID.randomUUID(),
            title = "dto-label",
            ordre = 1,
            type = "unique",
            description = null,
            maxChoices = null,
            consultationId = UUID.randomUUID(),
        )
        val choixPossibleDTO = ChoixPossibleDTO(
            id = UUID.randomUUID(),
            label = "label-choix",
            ordre = 1,
            questionId = UUID.randomUUID(),
        )

        // When
        val result = questionMapper.toDomain(questionDTOUnique, listOf(choixPossibleDTO))

        // Then
        assertThat(result).isEqualTo(
            QuestionChoixUnique(
                id = questionDTOUnique.id.toString(),
                title = "dto-label",
                order = 1,
                consultationId = questionDTOUnique.consultationId.toString(),
                choixPossibleList = listOf(
                    ChoixPossible(
                        id = choixPossibleDTO.id.toString(),
                        label = "label-choix",
                        ordre = 1,
                        questionId = choixPossibleDTO.questionId.toString(),
                    )
                ),
            )
        )
    }

    @Test
    fun `toDomain when DTO has type multiple should return QuestionChoixMultiple`() {
        // Given
        val questionDTOMultiple = QuestionDTO(
            id = UUID.randomUUID(),
            title = "dto-label",
            ordre = 1,
            type = "multiple",
            description = null,
            maxChoices = 2,
            consultationId = UUID.randomUUID(),
        )
        val questionChoixMultiple = QuestionChoixMultiple(
            id = questionDTOMultiple.id.toString(),
            title = "dto-label",
            order = 1,
            consultationId = questionDTOMultiple.consultationId.toString(),
            choixPossibleList = emptyList(),
            maxChoices = 2
        )

        // When
        val result = questionMapper.toDomain(questionDTOMultiple, emptyList())

        // Then
        assertThat(result).isEqualTo(questionChoixMultiple)
    }

    @Test
    fun `toDomain when DTO has type multiple and list choixPossibleDTO should return QuestionChoixMultiple with list choixPossible`() {
        // Given
        val questionDTOMultiple = QuestionDTO(
            id = UUID.randomUUID(),
            title = "dto-label",
            ordre = 1,
            type = "multiple",
            description = null,
            maxChoices = 2,
            consultationId = UUID.randomUUID(),
        )
        val choixPossibleDTO = ChoixPossibleDTO(
            id = UUID.randomUUID(),
            label = "label-choix",
            ordre = 1,
            questionId = UUID.randomUUID(),
        )

        // When
        val result = questionMapper.toDomain(questionDTOMultiple, listOf(choixPossibleDTO))

        // Then
        assertThat(result).isEqualTo(
            QuestionChoixMultiple(
                id = questionDTOMultiple.id.toString(),
                title = "dto-label",
                order = 1,
                consultationId = questionDTOMultiple.consultationId.toString(),
                choixPossibleList = listOf(
                    ChoixPossible(
                        id = choixPossibleDTO.id.toString(),
                        label = "label-choix",
                        ordre = 1,
                        questionId = choixPossibleDTO.questionId.toString(),
                    )
                ),
                maxChoices = 2,
            )
        )
    }

    @Test
    fun `toDomain when DTO has type ouverte should return QuestionOpened`() {
        // Given
        val questionDTOOuverte = QuestionDTO(
            id = UUID.randomUUID(),
            title = "dto-label",
            ordre = 1,
            type = "ouverte",
            description = null,
            maxChoices = null,
            consultationId = UUID.randomUUID(),
        )
        val questionOuverte = QuestionOpened(
            id = questionDTOOuverte.id.toString(),
            title = "dto-label",
            order = 1,
            consultationId = questionDTOOuverte.consultationId.toString(),
        )

        // When
        val result = questionMapper.toDomain(questionDTOOuverte, emptyList())

        // Then
        assertThat(result).isEqualTo(questionOuverte)
    }

    @Test
    fun `toDomain when DTO has type chapter should return Chapter`() {
        // Given
        val questionDTOChapter = QuestionDTO(
            id = UUID.randomUUID(),
            title = "dto-label",
            ordre = 1,
            type = "chapter",
            description = "dto-description",
            maxChoices = null,
            consultationId = UUID.randomUUID(),
        )
        val questionChapter = Chapter(
            id = questionDTOChapter.id.toString(),
            title = "dto-label",
            order = 1,
            consultationId = questionDTOChapter.consultationId.toString(),
            description = "dto-description",
        )

        // When
        val result = questionMapper.toDomain(questionDTOChapter, emptyList())

        // Then
        assertThat(result).isEqualTo(questionChapter)
    }
}