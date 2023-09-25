package fr.social.gouv.agora.usecase.reponseConsultation

import fr.social.gouv.agora.domain.ReponseConsultationInserting
import org.springframework.stereotype.Service

@Service
class CommunControlQuestionUseCase {
    companion object {
        private const val MAX_TEXT_LENGTH = 200
    }

    fun isChoiceIdOverOne(listChoices: List<String>) = listChoices.size > 1
    fun isChoiceAutreOverMaxLength(response: ReponseConsultationInserting) =
        response.responseText.length > MAX_TEXT_LENGTH
}


