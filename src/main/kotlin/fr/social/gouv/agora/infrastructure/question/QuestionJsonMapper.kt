package fr.social.gouv.agora.infrastructure.question

import fr.social.gouv.agora.domain.Question
import org.springframework.stereotype.Component

@Component
class QuestionJsonMapper {

    fun toJson(domainList: List<Question>): QuestionsJson {
        return QuestionsJson(questions = domainList.map { domainObject ->
            QuestionJson(
                id = domainObject.id,
                label = domainObject.label,
                order = domainObject.ordre,
                type = domainObject.type,
                possibleChoices = domainObject.choixPossibleList.map { choixPossible ->
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