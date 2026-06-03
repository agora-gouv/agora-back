package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.domain.ConsultationPreview
import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import fr.gouv.agora.infrastructure.thematique.repository.ThematiqueMapper
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ConsultationInfoMapper(private val thematiqueMapper: ThematiqueMapper) {
    private val logger = LoggerFactory.getLogger(ConsultationInfoMapper::class.java)

    fun toConsultationPreview(
        consultations: StrapiDTO<ConsultationStrapiDTO>,
    ): List<ConsultationPreview> {
        return consultations.data.map { consultation ->
            val thematique = thematiqueMapper.toDomain(consultation.thematique)

            ConsultationPreview(
                id = consultation.documentId,
                slug = consultation.slug,
                title = consultation.titre,
                coverUrl = consultation.getImageCouverture(),
                thematique = thematique,
                endDate = consultation.dateDeFin,
                isPublished = consultation.isPublished(),
                territory = consultation.territoire,
            )
        }
    }

    fun toDomainFinished(
        consultations: StrapiDTO<ConsultationStrapiDTO>,
        now: LocalDateTime,
    ): List<ConsultationPreviewFinished> {
        return consultations.data.mapNotNull { consultation ->
            val thematique = thematiqueMapper.toDomain(consultation.thematique)

            val updateDate = consultation.getLatestUpdateDate(now)
            if (updateDate == null) {
                logger.error("ConsultationPreviewFinished - Impossible de générer une updateDate pour la consultation id '${consultation.documentId}'")
                return@mapNotNull null
            }

            ConsultationPreviewFinished(
                id = consultation.documentId,
                slug = consultation.slug,
                title = consultation.titre,
                coverUrl = consultation.getImageCouverture(),
                thematique = thematique,
                updateLabel = consultation.getFlammeLabel(now),
                lastUpdateDate = updateDate,
                endDate = consultation.dateDeFin,
                isPublished = consultation.isPublished(),
                territory = consultation.territoire,
            )
        }
    }

    fun toConsultationsWithUpdateInfo(consultations: StrapiDTO<ConsultationStrapiDTO>, now: LocalDateTime): List<ConsultationWithUpdateInfo> {
        return consultations.data.mapNotNull { consultation ->
            val updateDate = consultation.getLatestUpdateDate(now)
            if (updateDate == null) {
                logger.error("toConsultationWithUpdateInfo - Impossible de générer une updateDate pour la consultation id '${consultation.documentId}'")
                return@mapNotNull null
            }

            return@mapNotNull ConsultationWithUpdateInfo(
                id = consultation.documentId,
                slug = consultation.slug,
                title = consultation.titre,
                coverUrl = consultation.getImageCouverture(),
                thematiqueId = consultation.thematique.documentId,
                endDate = consultation.dateDeFin,
                updateDate = updateDate,
                updateLabel = consultation.getFlammeLabel(now),
                territory = consultation.territoire,
            )
        }
    }

    fun toConsultationInfo(consultation: ConsultationStrapiDTO): ConsultationInfo {
        return ConsultationInfo(
            id = consultation.documentId,
            title = consultation.titre,
            slug = consultation.slug,
            coverUrl = consultation.getImageCouverture(),
            detailsCoverUrl = consultation.getImagePageContenu(),
            startDate = consultation.dateDeDebut,
            endDate = consultation.dateDeFin,
            questionCount = consultation.estimationNombreDeQuestions,
            estimatedTime = consultation.estimationTemps,
            participantCountGoal = consultation.nombreParticipantsCible,
            thematique = thematiqueMapper.toDomain(consultation.thematique),
            isPublished = consultation.isPublished(),
            territory = consultation.territoire,
            titreWeb = consultation.titrePageWeb,
            sousTitreWeb = consultation.sousTitrePageWeb,
        )
    }
}
