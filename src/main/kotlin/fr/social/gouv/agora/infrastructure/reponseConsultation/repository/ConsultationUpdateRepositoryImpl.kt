package fr.social.gouv.agora.infrastructure.reponseConsultation.repository

import fr.social.gouv.agora.domain.ConsultationUpdate
import fr.social.gouv.agora.usecase.reponseConsultation.repository.ConsultationUpdateRepository
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class ConsultationUpdateRepositoryImpl : ConsultationUpdateRepository {

    override fun getConsultationUpdate(consultationId: String): ConsultationUpdate? {
        // TODO Feat-23
        return ConsultationUpdate(
            step = 1,
            description = "<body>La description avec textes <b>en gras</b> et potentiellement des <a href=\"https://google.fr\">liens</a><br/><br/><ul><li>example1 <b>en gras</b></li><li>example2</li></ul></body>",
        )
    }

}