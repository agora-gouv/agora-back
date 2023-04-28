package fr.social.gouv.agora.infrastructure.consultation

import fr.social.gouv.agora.domain.ConsultationPreviewOngoing
import fr.social.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import org.springframework.stereotype.Component

@Component
class ConsultationPreviewJsonMapper(
    private val mapperThematique: ThematiqueJsonMapper,
) {
    fun toJson(
        domainList: List<ConsultationPreviewOngoing>,
    ): ConsultationPreviewJson {
        return ConsultationPreviewJson(ongoingList = domainList.map { domain ->
            ConsultationOngoingJson(
                id = domain.id,
                title = domain.title,
                coverUrl = domain.coverUrl,
                endDate = domain.endDate.toString(),
                thematique = mapperThematique.toJson(domain.thematique),
                hasAnswered = domain.hasAnswered,
            )
        }, finishedList = emptyList(), answeredList = emptyList())
    }
}