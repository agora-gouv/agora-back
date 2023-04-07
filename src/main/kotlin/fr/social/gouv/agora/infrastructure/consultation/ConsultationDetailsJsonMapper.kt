package fr.social.gouv.agora.infrastructure.consultation

import fr.social.gouv.agora.domain.Consultation
import fr.social.gouv.agora.infrastructure.utils.Base64ImageEncoder
import fr.social.gouv.agora.infrastructure.utils.JsonMapper
import org.springframework.stereotype.Component

@Component
class ConsultationDetailsJsonMapper : JsonMapper<Consultation, ConsultationDetailsJson> {

    override fun toJson(domain: Consultation) = ConsultationDetailsJson(
        id = domain.id,
        id_thematique = domain.id_thematique,
        cover = Base64ImageEncoder.toBase64(domain.cover) ?: "",
        end_date = domain.end_date.toString(),
        question_count = domain.question_count,
        estimated_time = domain.estimated_time,
        participant_count = 0, // TODO Feat-16
        participant_count_goal = domain.participant_count_goal,
        description = domain.description,
        tips_description = domain.tips_description,
    )

}