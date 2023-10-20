package fr.social.gouv.agora.infrastructure.consultation

import fr.social.gouv.agora.domain.Consultation
import fr.social.gouv.agora.infrastructure.profile.repository.DateMapper
import fr.social.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import org.springframework.stereotype.Component

@Component
class ConsultationDetailsJsonMapper(
    private val thematiqueJsonMapper: ThematiqueJsonMapper,
    private val dateMapper: DateMapper,
) {

    fun toJson(domain: Consultation, participantCount: Int): ConsultationDetailsJson {
        return ConsultationDetailsJson(
            id = domain.id,
            thematique = thematiqueJsonMapper.toNoIdJson(domain.thematique),
            title = domain.title,
            coverUrl = domain.coverUrl,
            endDate = dateMapper.toFormattedDate(domain.endDate),
            questionCount = domain.questionCount,
            estimatedTime = domain.estimatedTime,
            participantCount = participantCount,
            participantCountGoal = domain.participantCountGoal,
            description = domain.description,
            tipsDescription = domain.tipsDescription,
            hasAnswered = domain.hasAnswered,
        )
    }

}
