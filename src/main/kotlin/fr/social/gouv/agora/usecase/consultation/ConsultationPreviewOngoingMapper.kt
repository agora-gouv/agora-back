package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.domain.ConsultationPreviewOngoing
import fr.social.gouv.agora.domain.ConsultationPreviewOngoingInfo
import fr.social.gouv.agora.domain.Thematique
import org.springframework.stereotype.Component
import java.time.Clock

@Component
class ConsultationPreviewOngoingMapper(private val clock: Clock) {

    fun toConsultationPreviewOngoing(
        consultationPreviewOngoingInfo: ConsultationPreviewOngoingInfo,
        thematique: Thematique,
        hasAnswered: Boolean,
    ): ConsultationPreviewOngoing {
        return TODO() //ConsultationPreviewOngoing()
    }

}