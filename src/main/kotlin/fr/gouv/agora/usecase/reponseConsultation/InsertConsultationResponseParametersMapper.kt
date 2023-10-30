package fr.gouv.agora.usecase.reponseConsultation

import fr.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository.InsertParameters
import org.springframework.stereotype.Component
import java.util.*

@Component
class InsertConsultationResponseParametersMapper {

    fun toInsertParameters(consultationId: String, userId: String): InsertParameters {
        return InsertParameters(
            consultationId = consultationId,
            userId = userId,
            participationId = UUID.randomUUID().toString(),
        )
    }

}