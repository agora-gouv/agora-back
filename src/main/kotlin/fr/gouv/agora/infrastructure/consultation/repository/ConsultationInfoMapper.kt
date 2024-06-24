package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.domain.ConsultationPreviewOngoing
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationStrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationWithUpdateInfoDTO
import fr.gouv.agora.infrastructure.thematique.repository.ThematiqueMapper
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDate
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ConsultationInfoMapper {
    private val logger = LoggerFactory.getLogger(ConsultationInfoMapper::class.java)

    fun toDomain(dto: ConsultationDTO) = ConsultationInfo(
        id = dto.id.toString(),
        title = dto.title,
        coverUrl = dto.coverUrl,
        detailsCoverUrl = dto.detailsCoverUrl,
        startDate = dto.startDate.toLocalDateTime(),
        endDate = dto.endDate.toLocalDateTime(),
        questionCount = dto.questionCount,
        estimatedTime = dto.estimatedTime,
        participantCountGoal = dto.participantCountGoal,
        description = dto.description,
        tipsDescription = dto.tipsDescription,
        thematiqueId = dto.thematiqueId.toString(),
    )

    fun toDomain(consultations: List<ConsultationDTO>, thematiques: List<Thematique>): List<ConsultationPreviewOngoing> {
        return consultations.mapNotNull { consultation ->
            val thematique = thematiques.find { it.id == consultation.thematiqueId.toString() }

            if (thematique == null) {
                logger.error("ConsultationPreviewOngoing - Thematique id '${consultation.thematiqueId}' non trouvée")
                return@mapNotNull null
            }

            return@mapNotNull ConsultationPreviewOngoing(
                id = consultation.id.toString(),
                title = consultation.title,
                coverUrl = consultation.coverUrl,
                thematique = thematique,
                endDate = consultation.endDate.toLocalDate(),
            )
        }
    }

    fun toDomain(consultations: StrapiDTO<ConsultationStrapiDTO>, thematiques: List<Thematique>): List<ConsultationPreviewOngoing> {
        return consultations.data.mapNotNull { consultation ->
            val thematique = thematiques.find { it.id == consultation.attributes.thematique.attributes.databaseId }

            if (thematique == null) {
                logger.error("ConsultationPreviewOngoing - Thematique id '${consultation.attributes.thematique.attributes.databaseId}' non trouvée")
                return@mapNotNull null
            }

            ConsultationPreviewOngoing(
                id = consultation.id,
                title = consultation.attributes.titre,
                coverUrl = consultation.attributes.urlImageDeCouverture,
                thematique = thematique,
                endDate = consultation.attributes.dateDeFin,
            )
        }
    }

    fun toDomain(dto: ConsultationWithUpdateInfoDTO) = ConsultationWithUpdateInfo(
        id = dto.id.toString(),
        title = dto.title,
        coverUrl = dto.coverUrl,
        thematiqueId = dto.thematiqueId.toString(),
        endDate = dto.endDate,
        updateDate = dto.updateDate,
        updateLabel = dto.updateLabel,
    )
}
