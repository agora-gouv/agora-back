package fr.social.gouv.agora.infrastructure.question

import fr.social.gouv.agora.domain.*
import org.springframework.stereotype.Component

@Component
class QuestionJsonMapper {

    fun toJson(domainList: List<Question>): QuestionsJson {
        val chapterList = domainList.filterIsInstance<QuestionChapter>()
        val questionsNumber = domainList.size - chapterList.size
        return QuestionsJson(
            questionsUniqueChoice = buildQuestionUniqueChoiceJsonList(
                domainList.filterIsInstance<QuestionUniqueChoice>(),
                chapterList,
                questionsNumber,
            ),
            questionsMultipleChoices = buildQuestionMultipleChoicesJsonList(
                domainList.filterIsInstance<QuestionMultipleChoices>(),
                chapterList,
                questionsNumber,
            ),
            questionsOpened = buildQuestionOpenedJsonList(
                domainList.filterIsInstance<QuestionOpen>(),
                chapterList,
                questionsNumber,
            ),
            chapters = buildChapterJsonList(chapterList),
            questionsWithCondition = buildQuestionConditionalJsonList(
                domainList.filterIsInstance<QuestionConditional>(),
                chapterList,
                questionsNumber,
            ),
        )
    }

    private fun buildQuestionUniqueChoiceJsonList(
        questionUniqueList: List<QuestionUniqueChoice>,
        chapterList: List<QuestionChapter>,
        questionsNumber: Int,
    ) = questionUniqueList.map { domain ->
        QuestionUniqueChoiceJson(
            id = domain.id,
            title = domain.title,
            popupDescription = domain.popupDescription,
            order = domain.order,
            questionProgress = buildQuestionProgress(domain.order, chapterList, questionsNumber),
            nextQuestionId = domain.nextQuestionId,
            possibleChoices = domain.choixPossibleList.map { choixPossible ->
                ChoixPossibleJson(
                    id = choixPossible.id,
                    label = choixPossible.label,
                    order = choixPossible.ordre,
                    nextQuestionId = null,
                )
            }
        )
    }

    private fun buildQuestionMultipleChoicesJsonList(
        questionMultipleList: List<QuestionMultipleChoices>,
        chapterList: List<QuestionChapter>,
        questionsNumber: Int,
    ) = questionMultipleList.map { domain ->
        QuestionMultipleChoicesJson(
            id = domain.id,
            title = domain.title,
            popupDescription = domain.popupDescription,
            order = domain.order,
            questionProgress = buildQuestionProgress(domain.order, chapterList, questionsNumber),
            maxChoices = domain.maxChoices,
            nextQuestionId = domain.nextQuestionId,
            possibleChoices = domain.choixPossibleList.map { choixPossible ->
                ChoixPossibleJson(
                    id = choixPossible.id,
                    label = choixPossible.label,
                    order = choixPossible.ordre,
                    nextQuestionId = null,
                )
            }
        )
    }

    private fun buildQuestionOpenedJsonList(
        questionOpenList: List<QuestionOpen>,
        chapterList: List<QuestionChapter>,
        questionsNumber: Int,
    ) = questionOpenList.map { domain ->
        QuestionOpenedJson(
            id = domain.id,
            title = domain.title,
            popupDescription = domain.popupDescription,
            order = domain.order,
            questionProgress = buildQuestionProgress(domain.order, chapterList, questionsNumber),
            nextQuestionId = domain.nextQuestionId,
        )
    }

    private fun buildChapterJsonList(chapterList: List<QuestionChapter>) = chapterList.map { domain ->
        QuestionChapterJson(
            id = domain.id,
            title = domain.title,
            popupDescription = domain.popupDescription,
            order = domain.order,
            description = domain.description,
            nextQuestionId = domain.nextQuestionId,
        )
    }

    private fun buildQuestionConditionalJsonList(
        questionUniqueList: List<QuestionConditional>,
        chapterList: List<QuestionChapter>,
        questionsNumber: Int,
    ) = questionUniqueList.map { domain ->
        QuestionConditionalJson(
            id = domain.id,
            title = domain.title,
            popupDescription = domain.popupDescription,
            order = domain.order,
            questionProgress = buildQuestionProgress(domain.order, chapterList, questionsNumber),
            possibleChoices = domain.choixPossibleList.map { choixPossible ->
                ChoixPossibleJson(
                    id = choixPossible.id,
                    label = choixPossible.label,
                    order = choixPossible.ordre,
                    nextQuestionId = choixPossible.nextQuestionId,
                )
            }
        )
    }

    private fun calculateProgress(
        order: Int, chapterList: List<QuestionChapter>
    ) = (order - chapterList.count { chapter -> chapter.order < order })

    private fun buildQuestionProgress(order: Int, chapterList: List<QuestionChapter>, questionsNumber: Int) =
        "Question " + calculateProgress(order, chapterList) + "/" + questionsNumber
}
