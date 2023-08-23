package fr.social.gouv.agora.infrastructure.consultation

import fr.social.gouv.agora.domain.ConsultationPreviewAnswered
import fr.social.gouv.agora.domain.ConsultationPreviewFinished
import fr.social.gouv.agora.domain.ConsultationPreviewOngoing
import fr.social.gouv.agora.domain.ConsultationStatus
import fr.social.gouv.agora.domain.ConsultationStatus.*
import fr.social.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import org.springframework.stereotype.Component

@Component
class ConsultationPreviewJsonMapper(private val thematiqueJsonMapper: ThematiqueJsonMapper) {
    fun toJson(
        domainOngoingList: List<ConsultationPreviewOngoing>,
        domainFinishedList: List<ConsultationPreviewFinished>,
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
                )
            },
            finishedList = domainFinishedList.map { domain ->
                ConsultationFinishedJson(
                    id = domain.id,
                    title = domain.title,
                    coverUrl = domain.coverUrl,
                    thematique = thematiqueJsonMapper.toNoIdJson(domain.thematique),
                    step = statusToJson(domain.step)
                )
            },
            answeredList = domainAnsweredList.map { domain ->
                ConsultationAnsweredJson(
                    id = domain.id,
                    title = domain.title,
                    coverUrl = domain.coverUrl,
                    thematique = thematiqueJsonMapper.toNoIdJson(domain.thematique),
                    step = statusToJson(domain.step)
                )
            }
        )
    }


    private fun statusToJson(step: ConsultationStatus) = when (step) {
        COLLECTING_DATA -> 1
        POLITICAL_COMMITMENT -> 2
        EXECUTION -> 3
    }
}