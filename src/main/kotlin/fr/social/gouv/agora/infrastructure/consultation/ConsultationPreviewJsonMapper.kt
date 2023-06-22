package fr.social.gouv.agora.infrastructure.consultation

import fr.social.gouv.agora.domain.ConsultationPreviewAnswered
import fr.social.gouv.agora.domain.ConsultationPreviewOngoing
import fr.social.gouv.agora.domain.ConsultationStatus.*
import fr.social.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import org.springframework.stereotype.Component

@Component
class ConsultationPreviewJsonMapper(private val thematiqueJsonMapper: ThematiqueJsonMapper) {
    fun toJson(
        domainOngoingList: List<ConsultationPreviewOngoing>,
        domainAnsweredList: List<ConsultationPreviewAnswered>,
    ): ConsultationPreviewJson {
        return ConsultationPreviewJson(
            ongoingList = domainOngoingList.map { domain ->
                ConsultationOngoingJson(
                    id = domain.id,
                    title = domain.title,
                    coverUrl = domain.coverUrl,
                    endDate = domain.endDate.toString(),
                    thematique = thematiqueJsonMapper.toNoIdJson(domain.thematique),
                    highlightLabel = domain.highlightLabel,
                    hasAnswered = domain.hasAnswered,
                )
            },
            finishedList = emptyList(),
            answeredList = domainAnsweredList.map { domain ->
                ConsultationAnsweredJson(
                    id = domain.id,
                    title = domain.title,
                    coverUrl = domain.coverUrl,
                    thematique = thematiqueJsonMapper.toNoIdJson(domain.thematique),
                    step = when (domain.step) {
                        COLLECTING_DATA -> 1
                        POLITICAL_COMMITMENT -> 2
                        EXECUTION -> 3
                    }
                )
            }
        )
    }
}