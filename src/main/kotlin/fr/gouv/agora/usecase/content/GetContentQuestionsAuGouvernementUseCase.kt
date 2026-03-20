package fr.gouv.agora.usecase.content

import fr.gouv.agora.domain.PageQuestionAuGouvernementContent
import fr.gouv.agora.usecase.content.repository.ContentRepository
import fr.gouv.agora.usecase.qag.ContentSanitizer
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.springframework.stereotype.Service

@Service
class GetContentQuestionsAuGouvernementUseCase(
    private val contentRepository: ContentRepository,
    private val qagInfoRepository: QagInfoRepository,
    private val contentSanitizer: ContentSanitizer,
) {
    fun execute(): PageQuestionAuGouvernementContent {
        val content = contentRepository.getPageQuestionsAuGouvernement()

        val sanitizedText = contentSanitizer.sanitizeRichText(content.texteTotalQuestions, Int.MAX_VALUE)

        val nombreDeQuestions = qagInfoRepository.getQagsCount(null)
        val texteAvecNombreDeQuestions = sanitizedText.replace("{}", nombreDeQuestions.toString())

        return PageQuestionAuGouvernementContent(content.informationBottomsheet, texteAvecNombreDeQuestions)
    }
}
