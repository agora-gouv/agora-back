package fr.social.gouv.agora.infrastructure.question

import fr.social.gouv.agora.domain.*
import org.springframework.stereotype.Component

@Component
class QuestionJsonMapper {

    fun toJson(domainList: List<Question>): QuestionsJson {
        val chapterList = domainList.filterIsInstance<Chapter>()
        val chapterJsonList = chapterList.map { domain ->
            ChapterJson(
                id = domain.id,
                title = domain.title,
                order = domain.order,
                consultationId = domain.consultationId,
                description = domain.description,
            )
        }
        val questionUniqueList = domainList.filterIsInstance<QuestionChoixUnique>()
        val questionUniqueJsonList = questionUniqueList.map { domain ->
            QuestionUniqueChoiceJson(
                id = domain.id,
                title = domain.title,
                order = domain.order,
                consultationId = domain.consultationId,
                questionProgress = "Question "+ calculateProgress(domain.order, chapterList) +"/"+(domainList.size-chapterList.size),
                possibleChoices = domain.choixPossibleList.map { choixPossible ->
                    ChoixPossibleJson(
                        id = choixPossible.id,
                        label = choixPossible.label,
                        order = choixPossible.ordre,
                    )
                }
            )
        }
        val questionOpenedList = domainList.filterIsInstance<QuestionOpened>()
        val questionOpenedJsonList = questionOpenedList.map { domain ->
            QuestionOpenedJson(
                id = domain.id,
                title = domain.title,
                order = domain.order,
                consultationId = domain.consultationId,
                questionProgress = "Question "+ calculateProgress(domain.order, chapterList) +"/"+(domainList.size-chapterList.size),
            )
        }
        val questionMultipleList = domainList.filterIsInstance<QuestionChoixMultiple>()
        val questionMultipleJsonList = questionMultipleList.map { domain ->
            QuestionMultipleChoicesJson(
                id = domain.id,
                title = domain.title,
                order = domain.order,
                consultationId = domain.consultationId,
                questionProgress = "Question "+ calculateProgress(domain.order, chapterList) +"/"+(domainList.size-chapterList.size),
                maxChoices = domain.maxChoices,
                possibleChoices = domain.choixPossibleList.map { choixPossible ->
                    ChoixPossibleJson(
                        id = choixPossible.id,
                        label = choixPossible.label,
                        order = choixPossible.ordre,
                    )
                }
            )
        }
        return QuestionsJson(questionUniqueJsonList, questionOpenedJsonList, questionMultipleJsonList, chapterJsonList)
    }

    private fun calculateProgress(
        order: Int,
        chapterList: List<Chapter>
    ) = (order - chapterList.count { chapter -> chapter.order < order })
}
