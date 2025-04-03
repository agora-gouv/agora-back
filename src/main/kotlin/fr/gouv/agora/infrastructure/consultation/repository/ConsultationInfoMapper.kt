package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.domain.ConsultationPreview
import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.infrastructure.common.StrapiAttributes
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
            val thematique = thematiqueMapper.toDomain(consultation.attributes.thematique)

            ConsultationPreview(
                id = consultation.id,
                slug = consultation.attributes.slug,
                title = consultation.attributes.titre,
                coverUrl = consultation.attributes.getImageDeCouverture(),
                thematique = thematique,
                endDate = consultation.attributes.dateDeFin,
                isPublished = consultation.attributes.isPublished(),
                territory = consultation.attributes.territoire,
            )
        }
    }

    fun toDomainFinished(
        consultations: StrapiDTO<ConsultationStrapiDTO>,
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
                slug = consultation.attributes.slug,
                title = consultationFields.titre,
                coverUrl = consultationFields.getImageDeCouverture(),
                thematique = thematique,
                updateLabel = consultationFields.getFlammeLabel(now),
                lastUpdateDate = updateDate,
                endDate = consultationFields.dateDeFin,
                isPublished = consultation.attributes.isPublished(),
                territory = consultation.attributes.territoire,
            )
        }
    }

    fun toConsultationsWithUpdateInfo(consultations: StrapiDTO<ConsultationStrapiDTO>, now: LocalDateTime): List<ConsultationWithUpdateInfo> {
        return consultations.data.mapNotNull {
            val consultationAttributes = it.attributes

            val updateDate = consultationAttributes.getLatestUpdateDate(now)
            if (updateDate == null) {
                logger.error("toConsultationWithUpdateInfo - Impossible de générer une updateDate pour la consultation id '${it.id}'")
                return@mapNotNull null
            }

            return@mapNotNull ConsultationWithUpdateInfo(
                id = it.id,
                slug = consultationAttributes.slug,
                title = consultationAttributes.titre,
                coverUrl = consultationAttributes.getImageDeCouverture(),
                thematiqueId = consultationAttributes.thematique.data.id,
                endDate = consultationAttributes.dateDeFin,
                updateDate = updateDate,
                updateLabel = consultationAttributes.getFlammeLabel(now),
                territory = consultationAttributes.territoire,
            )
        }
    }

    fun toConsultationInfo(consultation: StrapiAttributes<ConsultationStrapiDTO>): ConsultationInfo {
        return ConsultationInfo(
            id = consultation.id,
            title = consultation.attributes.titre,
            slug = consultation.attributes.slug,
            coverUrl = consultation.attributes.getImageDeCouverture(),
            detailsCoverUrl = consultation.attributes.getImagePageDeContenu(),
            startDate = consultation.attributes.dateDeDebut,
            endDate = consultation.attributes.dateDeFin,
            questionCount = consultation.attributes.estimationNombreDeQuestions,
            estimatedTime = consultation.attributes.estimationTemps,
            participantCountGoal = consultation.attributes.nombreParticipantsCible,
            thematique = thematiqueMapper.toDomain(consultation.attributes.thematique),
            isPublished = consultation.attributes.isPublished(),
            territory = consultation.attributes.territoire,
            titreWeb = consultation.attributes.titrePageWeb,
            sousTitreWeb = consultation.attributes.sousTitrePageWeb,
        )
    }
}
