package fr.social.gouv.agora.infrastructure.consultation

import fr.social.gouv.agora.domain.Consultation
import org.springframework.stereotype.Component

@Component
class ConsultationDetailsJsonMapper {

    fun toJson(domain: Consultation) = ConsultationDetailsJson(
        id = domain.id,
        thematiqueId = domain.thematiqueId,
        title = domain.title,
        coverUrl = domain.coverUrl,
        endDate = domain.endDate.toString(),
        questionCount = domain.questionCount,
        estimatedTime = domain.estimatedTime,
        participantCount = 15035, // TODO Feat-16
        participantCountGoal = domain.participantCountGoal,
        description = domain.description,
        tipsDescription = domain.tipsDescription,
    )

}