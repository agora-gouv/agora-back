package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationPreviewOngoing
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationStrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationWithUpdateInfoDTO
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ConsultationInfoMapper {
    private val logger = LoggerFactory.getLogger(ConsultationInfoMapper::class.java)

    fun toDomainOngoing(dto: ConsultationDTO) = ConsultationInfo(
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

    fun toDomainOngoing(
        consultations: List<ConsultationDTO>,
        thematiques: List<Thematique>
    ): List<ConsultationPreviewOngoing> {
        return consultations.mapNotNull { consultation ->
            val thematique = thematiques.find { it.id == consultation.thematiqueId.toString() }

            if (thematique == null) {
                logger.error("ConsultationPreviewOngoing Database - Thematique id '${consultation.thematiqueId}' non trouvée")
                return@mapNotNull null
            }

            return@mapNotNull ConsultationPreviewOngoing(
                id = consultation.id.toString(),
                title = consultation.title,
                coverUrl = consultation.coverUrl,
                thematique = thematique,
                endDate = consultation.endDate.toLocalDateTime(),
            )
        }
    }

    fun toDomainFinished(
        consultations: List<ConsultationWithUpdateInfoDTO>,
        thematiques: List<Thematique>,
    ): List<ConsultationPreviewFinished> {
        return consultations.mapNotNull { consultation ->
            val thematique = thematiques.find { it.id == consultation.thematiqueId.toString() }

            if (thematique == null) {
                logger.error("ConsultationPreviewFinished Database - Thematique id '${consultation.thematiqueId}' non trouvée")
                return@mapNotNull null
            }

            return@mapNotNull ConsultationPreviewFinished(
                id = consultation.id.toString(),
                title = consultation.title,
                coverUrl = consultation.coverUrl,
                thematique = thematique,
                updateLabel = consultation.updateLabel,
                updateDate = consultation.updateDate.toLocalDateTime(),
                endDate = consultation.endDate.toLocalDateTime(),
            )
        }
    }

    fun toDomainOngoing(
        consultations: StrapiDTO<ConsultationStrapiDTO>,
        thematiques: List<Thematique>
    ): List<ConsultationPreviewOngoing> {
        return consultations.data.mapNotNull { consultation ->
            val thematique = thematiques.find { it.id == consultation.attributes.thematique.data.attributes.databaseId }

            if (thematique == null) {
                logger.error("ConsultationPreviewOngoing Strapi - Thematique id '${consultation.attributes.thematique.data.attributes.databaseId}' non trouvée")
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

    fun toDomainFinished(
        consultations: StrapiDTO<ConsultationStrapiDTO>,
        thematiques: List<Thematique>,
        updateDate: LocalDateTime,
    ): List<ConsultationPreviewFinished> {
        return consultations.data.mapNotNull { consultation ->
            val thematique = thematiques.find { it.id == consultation.attributes.thematique.data.attributes.databaseId }

            if (thematique == null) {
                logger.error("ConsultationPreviewFinished - Thematique id '${consultation.attributes.thematique.data.attributes.databaseId}' non trouvée")
                return@mapNotNull null
            }

            ConsultationPreviewFinished(
                id = consultation.id,
                title = consultation.attributes.titre,
                coverUrl = consultation.attributes.urlImageDeCouverture,
                thematique = thematique,
                updateLabel = consultation.attributes.flammeLabel,
                updateDate = updateDate,
                endDate = consultation.attributes.dateDeFin,
            )
        }
    }

    fun toDomainOngoing(dto: ConsultationWithUpdateInfoDTO) = ConsultationWithUpdateInfo(
        id = dto.id.toString(),
        title = dto.title,
        coverUrl = dto.coverUrl,
        thematiqueId = dto.thematiqueId.toString(),
        endDate = dto.endDate.toLocalDateTime(),
        updateDate = dto.updateDate.toLocalDateTime(),
        updateLabel = dto.updateLabel,
    )
}
