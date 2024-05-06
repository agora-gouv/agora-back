package fr.gouv.agora.infrastructure.question

import fr.gouv.agora.domain.*
import org.springframework.stereotype.Component

@Component
class QuestionJsonMapper {

    fun toJson(domain: Questions): QuestionsJson {
        val chapterList = domain.questions.filterIsInstance<QuestionChapter>()
        val questionsNumber = domain.questions.size - chapterList.size
        return QuestionsJson(
            questionCount = domain.questionCount,
            questionsUniqueChoice = buildQuestionUniqueChoiceJsonList(
                domain.questions.filterIsInstance<QuestionUniqueChoice>(),
                chapterList,
                questionsNumber,
            ),
            questionsMultipleChoices = buildQuestionMultipleChoicesJsonList(
                domain.questions.filterIsInstance<QuestionMultipleChoices>(),
                chapterList,
                questionsNumber,
            ),
            questionsOpened = buildQuestionOpenedJsonList(
                domain.questions.filterIsInstance<QuestionOpen>(),
                chapterList,
                questionsNumber,
            ),
            chapters = buildChapterJsonList(chapterList),
            questionsWithCondition = buildQuestionConditionalJsonList(
                domain.questions.filterIsInstance<QuestionConditional>(),
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
        val (questionProgress, progressDescription) = buildQuestionProgress(domain.order, chapterList, questionsNumber)
        QuestionUniqueChoiceJson(
            id = domain.id,
            title = domain.title,
            popupDescription = domain.popupDescription,
            order = domain.order,
            questionProgress = questionProgress,
            questionProgressDescription = progressDescription,
            nextQuestionId = domain.nextQuestionId,
            possibleChoices = domain.choixPossibleList.map { choixPossible ->
                ChoixPossibleJson(
                    id = choixPossible.id,
                    label = choixPossible.label,
                    order = choixPossible.ordre,
                    nextQuestionId = null,
                    hasOpenTextField = choixPossible.hasOpenTextField,
                )
            }
        )
    }

    private fun buildQuestionMultipleChoicesJsonList(
        questionMultipleList: List<QuestionMultipleChoices>,
        chapterList: List<QuestionChapter>,
        questionsNumber: Int,
    ) = questionMultipleList.map { domain ->
        val (questionProgress, progressDescription) = buildQuestionProgress(domain.order, chapterList, questionsNumber)
        QuestionMultipleChoicesJson(
            id = domain.id,
            title = domain.title,
            popupDescription = domain.popupDescription,
            order = domain.order,
            questionProgress = questionProgress,
            questionProgressDescription = progressDescription,
            maxChoices = domain.maxChoices,
            nextQuestionId = domain.nextQuestionId,
            possibleChoices = domain.choixPossibleList.map { choixPossible ->
                ChoixPossibleJson(
                    id = choixPossible.id,
                    label = choixPossible.label,
                    order = choixPossible.ordre,
                    nextQuestionId = null,
                    hasOpenTextField = choixPossible.hasOpenTextField,
                )
            }
        )
    }

    private fun buildQuestionOpenedJsonList(
        questionOpenList: List<QuestionOpen>,
        chapterList: List<QuestionChapter>,
        questionsNumber: Int,
    ) = questionOpenList.map { domain ->
        val (questionProgress, progressDescription) = buildQuestionProgress(domain.order, chapterList, questionsNumber)
        QuestionOpenedJson(
            id = domain.id,
            title = domain.title,
            popupDescription = domain.popupDescription,
            order = domain.order,
            questionProgress = questionProgress,
            questionProgressDescription = progressDescription,
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
        val (questionProgress, progressDescription) = buildQuestionProgress(domain.order, chapterList, questionsNumber)
        QuestionConditionalJson(
            id = domain.id,
            title = domain.title,
            popupDescription = domain.popupDescription,
            order = domain.order,
            questionProgress = questionProgress,
            questionProgressDescription = progressDescription,
            possibleChoices = domain.choixPossibleList.map { choixPossible ->
                ChoixPossibleJson(
                    id = choixPossible.id,
                    label = choixPossible.label,
                    order = choixPossible.ordre,
                    nextQuestionId = choixPossible.nextQuestionId,
                    hasOpenTextField = choixPossible.hasOpenTextField,
                )
            }
        )
    }

    private fun calculateProgress(
        order: Int, chapterList: List<QuestionChapter>
    ) = (order - chapterList.count { chapter -> chapter.order < order })

    private fun buildQuestionProgress(order: Int, chapterList: List<QuestionChapter>, questionsNumber: Int): Pair<String, String> {
        val progress = calculateProgress(order, chapterList)
        val questionProgress = "Question $progress/$questionsNumber"
        val contentDescription = "Question $progress sur $questionsNumber"
        return questionProgress to contentDescription
    }

}
