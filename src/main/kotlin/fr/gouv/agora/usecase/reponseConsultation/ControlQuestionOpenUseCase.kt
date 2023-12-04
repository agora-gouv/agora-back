package fr.gouv.agora.usecase.reponseConsultation

import fr.gouv.agora.domain.ReponseConsultationInserting
import org.springframework.stereotype.Service

@Service
class ControlQuestionOpenUseCase {
    companion object {
        private const val MAX_TEXT_LENGTH = 400
    }

    fun isTextFieldOverMaxLength(response: ReponseConsultationInserting) =
        response.responseText.length > MAX_TEXT_LENGTH
}


