package fr.social.gouv.agora.infrastructure.question

import fr.social.gouv.agora.domain.Question
import org.springframework.stereotype.Component

@Component
class QuestionJsonMapper {

    fun toJson(domainList: List<Question>): QuestionsJson {
        return QuestionsJson(questions = domainList.map { domain ->
            QuestionJson(
                id = domain.id,
                label = domain.label,
                order = domain.ordre,
                type = domain.type,
                maxChoices = domain.maxChoices,
                possibleChoices = domain.choixPossibleList.map { choixPossible ->
                    ChoixPossibleJson(
                        id = choixPossible.id,
                        label = choixPossible.label,
                        order = choixPossible.ordre,
                    )
                }
            )
        })
    }
}