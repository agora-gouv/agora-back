package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.domain.ConsultationPreview
import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.toHtml
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationStrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationWithUpdateInfoDTO
import fr.gouv.agora.infrastructure.thematique.repository.ThematiqueMapper
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ConsultationInfoMapper(
    private val thematiqueMapper: ThematiqueMapper,
) {
    private val logger = LoggerFactory.getLogger(ConsultationInfoMapper::class.java)

    fun toConsultationInfo(dto: ConsultationDTO) = ConsultationInfo(
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

    fun toConsultationInfo(
        dto: StrapiDTO<ConsultationStrapiDTO>,
    ): List<ConsultationInfo> {
        return dto.data.map { consultation ->
            ConsultationInfo(
                id = consultation.id,
                title = consultation.attributes.titre,
                coverUrl = consultation.attributes.urlImageDeCouverture,
                detailsCoverUrl = consultation.attributes.urlImagePageDeContenu,
                startDate = consultation.attributes.dateDeDebut,
                endDate = consultation.attributes.dateDeFin,
                questionCount = consultation.attributes.estimationNombreDeQuestions,
                estimatedTime = consultation.attributes.estimationTemps,
                participantCountGoal = consultation.attributes.nombreParticipantsCible,
                description = consultation.attributes.description.toHtml(),
                tipsDescription = consultation.attributes.objectifs.toHtml(),
                thematiqueId = consultation.attributes.thematique.data.attributes.databaseId,
            )
        }
    }

    fun toConsultationPreview(
        consultations: List<ConsultationDTO>,
        thematiques: List<Thematique>
    ): List<ConsultationPreview> {
        return consultations.mapNotNull { consultation ->
            val thematique = thematiques.find { it.id == consultation.thematiqueId.toString() }

            if (thematique == null) {
                logger.error("ConsultationPreviewOngoing Database - Thematique id '${consultation.thematiqueId}' non trouvée")
                return@mapNotNull null
            }

            return@mapNotNull ConsultationPreview(
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

    fun toConsultationPreview(
        consultations: StrapiDTO<ConsultationStrapiDTO>,
        thematiques: List<Thematique>
    ): List<ConsultationPreview> {
        return consultations.data.map { consultation ->
            val thematique = thematiqueMapper.toDomain(consultation.attributes.thematique)

            ConsultationPreview(
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
        now: LocalDateTime,
    ): List<ConsultationPreviewFinished> {
        return consultations.data.mapNotNull { consultation ->
            val consultationFields = consultation.attributes

            val thematique = thematiqueMapper.toDomain(consultation.attributes.thematique)

            val updateDate = consultationFields.getLatestUpdateDate(now)
            if (updateDate == null) {
                logger.error("ConsultationPreviewFinished - Impossible de générer une updateDate pour la consultation id '${consultation.id}'")
                return@mapNotNull null
            }

            ConsultationPreviewFinished(
                id = consultation.id,
                title = consultationFields.titre,
                coverUrl = consultationFields.urlImageDeCouverture,
                thematique = thematique,
                updateLabel = consultationFields.flammeLabel,
                updateDate = updateDate,
                endDate = consultationFields.dateDeFin,
            )
        }
    }

    fun toConsultationWithUpdateInfo(dto: ConsultationWithUpdateInfoDTO) = ConsultationWithUpdateInfo(
        id = dto.id.toString(),
        title = dto.title,
        coverUrl = dto.coverUrl,
        thematiqueId = dto.thematiqueId.toString(),
        endDate = dto.endDate.toLocalDateTime(),
        updateDate = dto.updateDate.toLocalDateTime(),
        updateLabel = dto.updateLabel,
    )
}
