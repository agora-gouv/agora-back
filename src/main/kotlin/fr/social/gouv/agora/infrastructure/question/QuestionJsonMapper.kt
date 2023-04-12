package fr.social.gouv.agora.infrastructure.question

import fr.social.gouv.agora.domain.Question
import fr.social.gouv.agora.infrastructure.utils.JsonMapper
import org.springframework.stereotype.Component

@Component
class QuestionJsonMapper : JsonMapper<List<Question>, QuestionsJson> {

    override fun toJson(domain: List<Question>): QuestionsJson {
        return QuestionsJson(questions = domain.map { domainObject ->
            QuestionJson(
                id = domainObject.id,
                label = domainObject.label,
                order = domainObject.ordre,
                type = domainObject.type,
                possible_choices = domainObject.listechoix.map { obj ->
                    ChoixPossibleJson(
                        id = obj.id,
                        label = obj.label,
                        order = obj.ordre,
                    )
                }
            )
        })
    }
}