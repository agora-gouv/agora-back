package fr.social.gouv.agora.infrastructure.question.repository

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.infrastructure.question.dto.ChoixPossibleDTO
import fr.social.gouv.agora.infrastructure.question.dto.QuestionDTO
import org.springframework.stereotype.Component

@Component
class QuestionMapper(private val choixPossibleMapper: ChoixPossibleMapper) {

    fun toDomain(dto: QuestionDTO, choixPossibleDtoList: List<ChoixPossibleDTO>): Question {
        val question = when (dto.type) {
            "unique" -> QuestionChoixUnique(id = dto.id.toString(),
                title = dto.title,
                order = dto.ordre,
                consultationId = dto.consultationId.toString(),
                choixPossibleList = choixPossibleDtoList.map { choixPossibleDTO ->
                    choixPossibleMapper.toDomain(
                        choixPossibleDTO
                    )
                })

            "multiple" -> QuestionChoixMultiple(
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

            "ouverte" -> QuestionOpened(
                id = dto.id.toString(),
                title = dto.title,
                order = dto.ordre,
                consultationId = dto.consultationId.toString(),
            )

            "chapter" -> Chapter(
                id = dto.id.toString(),
                title = dto.title,
                order = dto.ordre,
                consultationId = dto.consultationId.toString(),
                description = dto.description ?: "",

                )

            else -> throw IllegalArgumentException("type de question erroné")
        }
        return question
    }
}
