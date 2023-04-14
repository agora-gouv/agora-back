package fr.social.gouv.agora.infrastructure.reponseConsultation

import fr.social.gouv.agora.domain.ReponseConsultation
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ReponseConsultationJsonMapper {

    fun toDomain(json: ReponsesConsultationJson): List<ReponseConsultation> {
        return json.responses.map { response ->
            ReponseConsultation(
                id = UUID.randomUUID().toString(),
                consultationId = json.consultationId,
                questionId = response.questionId,
                choiceIds = response.choiceIds,
                responseText = response.responseText,
            )
        }
    }
}