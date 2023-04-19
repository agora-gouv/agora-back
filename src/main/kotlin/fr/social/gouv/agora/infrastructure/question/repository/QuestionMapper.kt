package fr.social.gouv.agora.infrastructure.question.repository

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.infrastructure.question.dto.ChoixPossibleDTO
import fr.social.gouv.agora.infrastructure.question.dto.QuestionDTO
import org.springframework.stereotype.Component

@Component
class QuestionMapper(private val choixPossibleMapper: ChoixPossibleMapper) {

    fun toDomain(dto: QuestionDTO, choixPossibleDtoList: List<ChoixPossibleDTO>): Question {
        return when (dto.type) {
            "unique" -> buildQuestionChoixUnique(dto, choixPossibleDtoList)

            "multiple" -> buildQuestionChoixMultiple(dto, choixPossibleDtoList)

            "ouverte" -> buildQuestionOuverte(dto)

            "chapter" -> buildChapitre(dto)

            else -> throw IllegalArgumentException("Invalid question type ${dto.type}")
        }
    }

    private fun buildChapitre(dto: QuestionDTO) = Chapitre(
        id = dto.id.toString(),
        title = dto.title,
        order = dto.ordre,
        consultationId = dto.consultationId.toString(),
        description = dto.description ?: "",
    )

    private fun buildQuestionOuverte(dto: QuestionDTO) = QuestionOuverte(
        id = dto.id.toString(),
        title = dto.title,
        order = dto.ordre,
        consultationId = dto.consultationId.toString(),
    )

    private fun buildQuestionChoixMultiple(
        dto: QuestionDTO, choixPossibleDtoList: List<ChoixPossibleDTO>
    ) = QuestionChoixMultiple(
        id = dto.id.toString(),
        title = dto.title,
        order = dto.ordre,
        consultationId = dto.consultationId.toString(),
        choixPossibleList = choixPossibleDtoList.map { choixPossibleDTO ->
            choixPossibleMapper.toDomain(
                choixPossibleDTO
            )
        },
        maxChoices = dto.maxChoices!!
    )

    private fun buildQuestionChoixUnique(
        dto: QuestionDTO, choixPossibleDtoList: List<ChoixPossibleDTO>
    ) = QuestionChoixUnique(id = dto.id.toString(),
        title = dto.title,
        order = dto.ordre,
        consultationId = dto.consultationId.toString(),
        choixPossibleList = choixPossibleDtoList.map { choixPossibleDTO ->
            choixPossibleMapper.toDomain(
                choixPossibleDTO
            )
        })

}
