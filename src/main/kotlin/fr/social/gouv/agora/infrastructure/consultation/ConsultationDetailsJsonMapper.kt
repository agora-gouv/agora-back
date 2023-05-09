package fr.social.gouv.agora.infrastructure.consultation

import fr.social.gouv.agora.domain.Consultation
import org.springframework.stereotype.Component

@Component
class ConsultationDetailsJsonMapper {

    fun toJson(domain: Consultation, participantCount: Int): ConsultationDetailsJson {
        return ConsultationDetailsJson(
            id = domain.id,
            thematique = ThematiqueJson(
                label = domain.thematique.label,
                picto = domain.thematique.picto,
                color = domain.thematique.color,
            ),
            title = domain.title,
            coverUrl = domain.coverUrl,
            endDate = domain.endDate.toString(),
            questionCount = domain.questionCount,
            estimatedTime = domain.estimatedTime,
            participantCount = participantCount,
            participantCountGoal = domain.participantCountGoal,
            description = domain.description,
            tipsDescription = domain.tipsDescription,
        )
    }

}
