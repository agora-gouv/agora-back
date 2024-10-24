package fr.gouv.agora.usecase.content

import fr.gouv.agora.domain.PageQuestionAuGouvernementContent
import fr.gouv.agora.usecase.content.repository.ContentRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.springframework.stereotype.Service

@Service
class GetContentQuestionsAuGouvernementUseCase(
    private val contentRepository: ContentRepository,
    private val qagInfoRepository: QagInfoRepository,
) {
    fun execute(): PageQuestionAuGouvernementContent {
        val content = contentRepository.getPageQuestionsAuGouvernement()

        val nombreDeQuestions = qagInfoRepository.getQagsCount(null)
        val texteAvecNombreDeQuestions = content.texteTotalQuestions.replace("{}", nombreDeQuestions.toString())

        return PageQuestionAuGouvernementContent(content.informationBottomsheet, texteAvecNombreDeQuestions)
    }
}
