package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.domain.Consultation
import fr.social.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import fr.social.gouv.agora.infrastructure.utils.Mapper
import org.springframework.stereotype.Component
import java.util.*

@Component
class ConsultationMapper : Mapper<Consultation, ConsultationDTO> {

    override fun toDomain(dto: ConsultationDTO) = Consultation(
        id = dto.id.toString(),
        title = dto.title,
        abstract = dto.abstract,
        start_date = dto.start_date,
        end_date = dto.end_date,
        cover_url = dto.cover_url,
        question_count = dto.question_count,
        estimated_time = dto.estimated_time,
        participant_count_goal = dto.participant_count_goal,
        description = dto.description,
        tips_description = dto.tips_description,
        id_thematique = dto.id_thematique.toString(),
    )

    override fun toDto(domain: Consultation) = ConsultationDTO(
        id = UUID.fromString(domain.id),
        title = domain.title,
        abstract = domain.abstract,
        start_date = domain.start_date,
        end_date = domain.end_date,
        cover_url = domain.cover_url,
        question_count = domain.question_count,
        estimated_time = domain.estimated_time,
        participant_count_goal = domain.participant_count_goal,
        description = domain.description,
        tips_description = domain.tips_description,
        id_thematique = UUID.fromString(domain.id_thematique),
    )
}