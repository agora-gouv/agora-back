package fr.social.gouv.agora.infrastructure.reponseConsultation

import fr.social.gouv.agora.domain.ReponseConsultationInserting
import org.springframework.stereotype.Component

@Component
class ReponseConsultationJsonMapper {

    fun toDomain(json: ReponsesConsultationJson): List<ReponseConsultationInserting> {
        return json.responses.map { response ->
            ReponseConsultationInserting(
                questionId = response.questionId,
                choiceIds = response.choiceIds,
                responseText = response.responseText,
            )
        }
    }
}