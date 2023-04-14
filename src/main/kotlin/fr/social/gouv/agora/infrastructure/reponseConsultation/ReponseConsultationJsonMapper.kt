package fr.social.gouv.agora.infrastructure.reponseConsultation

import fr.social.gouv.agora.domain.ReponseConsultationInserting
import org.springframework.stereotype.Component
import java.util.*

@Component
class ReponseConsultationJsonMapper {

    fun toDomain(json: ReponsesConsultationJson, participationId: UUID): List<ReponseConsultationInserting> {
        return json.responses.map { response ->
            ReponseConsultationInserting(
                consultationId = json.consultationId,
                questionId = response.questionId,
                choiceIds = response.choiceIds,
                responseText = response.responseText,
                participationId = participationId.toString(),
            )
        }
    }
}