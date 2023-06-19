package fr.social.gouv.agora.infrastructure.question

import fr.social.gouv.agora.domain.*
import org.springframework.stereotype.Component

@Component
class QuestionJsonMapper {

    fun toJson(domainList: List<Question>): QuestionsJson {
        val chapterList = domainList.filterIsInstance<Chapitre>()
        val questionsNumber = domainList.size - chapterList.size
        return QuestionsJson(
            questionsUniqueChoice = buildQuestionUniqueChoiceJsonList(
                domainList.filterIsInstance<QuestionChoixUnique>(),
                chapterList,
                questionsNumber
            ),
            questionsOpened = buildQuestionOpenedJsonList(
                domainList.filterIsInstance<QuestionOuverte>(),
                chapterList,
                questionsNumber
            ),
            questionsMultipleChoices = buildQuestionMultipleChoicesJsonList(
                domainList.filterIsInstance<QuestionChoixMultiple>(),
                chapterList,
                questionsNumber
            ),
            chapters = buildChapterJsonList(chapterList),
            questionsWithCondition = emptyList(),
        )
    }

    private fun buildQuestionUniqueChoiceJsonList(
        questionUniqueList: List<QuestionChoixUnique>,
        chapterList: List<Chapitre>,
        questionsNumber: Int
    ) = questionUniqueList.map { domain ->
        QuestionUniqueChoiceJson(id = domain.id,
            title = domain.title,
            order = domain.order,
            questionProgress = buildQuestionProgress(domain.order, chapterList, questionsNumber),
            possibleChoices = domain.choixPossibleList.map { choixPossible ->
                ChoixPossibleJson(
                    id = choixPossible.id,
                    label = choixPossible.label,
                    order = choixPossible.ordre,
                )
            })
    }

    private fun buildQuestionOpenedJsonList(
        questionOuverteList: List<QuestionOuverte>,
        chapterList: List<Chapitre>,
        questionsNumber: Int
    ) = questionOuverteList.map { domain ->
        QuestionOpenedJson(
            id = domain.id,
            title = domain.title,
            order = domain.order,
            questionProgress = buildQuestionProgress(domain.order, chapterList, questionsNumber),
        )
    }

    private fun buildQuestionMultipleChoicesJsonList(
        questionMultipleList: List<QuestionChoixMultiple>,
        chapterList: List<Chapitre>,
        questionsNumber: Int
    ) = questionMultipleList.map { domain ->
        QuestionMultipleChoicesJson(
            id = domain.id,
            title = domain.title,
            order = domain.order,
            questionProgress = buildQuestionProgress(domain.order, chapterList, questionsNumber),
            maxChoices = domain.maxChoices,
            possibleChoices = domain.choixPossibleList.map { choixPossible ->
                ChoixPossibleJson(
                    id = choixPossible.id,
                    label = choixPossible.label,
                    order = choixPossible.ordre,
                )
            })
    }

    private fun buildChapterJsonList(chapterList: List<Chapitre>) = chapterList.map { domain ->
        ChapterJson(
            id = domain.id,
            title = domain.title,
            order = domain.order,
            description = domain.description,
        )
    }

    private fun calculateProgress(
        order: Int, chapterList: List<Chapitre>
    ) = (order - chapterList.count { chapter -> chapter.order < order })

    private fun buildQuestionProgress(order: Int, chapterList: List<Chapitre>, questionsNumber: Int) =
        "Question " + calculateProgress(order, chapterList) + "/" + questionsNumber
}
