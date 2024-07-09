package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.ConsultationUpdateInfoV2
import fr.gouv.agora.infrastructure.consultation.repository.ConsultationStrapiRepository
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateV2Repository
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class ConsultationUpdateV2RepositoryImpl(
    private val updateDatabaseRepository: ConsultationUpdateInfoV2DatabaseRepository,
    private val consultationStrapiRepository: ConsultationStrapiRepository,
    private val sectionDatabaseRepository: ConsultationUpdateSectionDatabaseRepository,
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val mapper: ConsultationUpdateInfoV2Mapper,
) : ConsultationUpdateV2Repository {

    override fun getLatestConsultationUpdateLabel(consultationId: String): String? {
        val consultationUUID = consultationId.toUuidOrNull()
        if (consultationUUID != null) {
            return updateDatabaseRepository.getLatestConsultationUpdateLabel(consultationUUID)
        }

        if (featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            return consultationStrapiRepository.getLastUpdateLabelFromConsultation(consultationId)
        }

        return null
    }

    override fun getUnansweredUsersConsultationUpdate(consultationId: String): ConsultationUpdateInfoV2? {
        val consultationUUID = consultationId.toUuidOrNull()
        if (consultationUUID != null) {
            val updateDTO = updateDatabaseRepository.getUnansweredUsersConsultationUpdate(consultationUUID)
            val sectionDTOs = sectionDatabaseRepository.getConsultationUpdateSections(updateDTO.id)
            return mapper.toDomain(updateDTO, sectionDTOs)
        }

        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            return null
        }

        // TODO si c'est un uuid, database, sinon strapi ?
        // TODO getContenuAprèsRéponses ?
        // TODO SELECT * FROM consultation_updates_v2
        //            WHERE consultation_id = :consultationId
        //            AND CURRENT_TIMESTAMP > update_date
        //            AND is_visible_to_unanswered_users_only = 1
        //            ORDER BY update_date DESC
        //            LIMIT 1
        //
        // TODO SELECT * FROM consultation_update_sections
        //            WHERE consultation_update_id = :consultationUpdateId
        //            ORDER BY ordre

        TODO("Strapi's consultations are not implemented yet")
    }

    override fun getLatestConsultationUpdate(consultationId: String): ConsultationUpdateInfoV2? {
        val consultationUUID = consultationId.toUuidOrNull()
        if (consultationUUID != null) {
            val updateDTO = updateDatabaseRepository.getLatestConsultationUpdate(consultationUUID)
            val sectionDTOs = sectionDatabaseRepository.getConsultationUpdateSections(updateDTO.id)
            return mapper.toDomain(updateDTO, sectionDTOs)
        }

        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            // TODO si c'est un uuid, database, sinon strapi ?
            //  SELECT * FROM consultation_updates_v2
            //            WHERE consultation_id = :consultationId
            //            AND CURRENT_TIMESTAMP > update_date
            //            AND is_visible_to_unanswered_users_only = 0
            //            ORDER BY update_date DESC
            //            LIMIT 1
            //  SELECT * FROM consultation_update_sections
            //            WHERE consultation_update_id = :consultationUpdateId
            //            ORDER BY ordre
            return null
        }

        TODO("Strapi's consultations are not implemented yet")
    }

    override fun getConsultationUpdate(
        consultationId: String,
        consultationUpdateId: String,
    ): ConsultationUpdateInfoV2? {
        val consultationUUID = consultationId.toUuidOrNull()
        val consultationUpdateUUID = consultationUpdateId.toUuidOrNull()

        if (consultationUUID != null && consultationUpdateUUID != null) {
            return updateDatabaseRepository.getConsultationUpdate(
                consultationId = consultationUUID,
                consultationUpdateId = consultationUpdateUUID,
            )?.let { updateDTO ->
                mapper.toDomain(
                    updateDTO,
                    sectionDatabaseRepository.getConsultationUpdateSections(consultationUpdateUUID),
                )
            }
        }

        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            // TODO si c'est un uuid, database, sinon strapi ?
            //  Strapi.get
            //  SELECT * FROM consultation_updates_v2
            //            WHERE consultation_id = :consultationId
            //            AND id = :consultationUpdateId
            //            AND CURRENT_TIMESTAMP > update_date
            //            LIMIT 1
            //  SELECT * FROM consultation_update_sections
            //            WHERE consultation_update_id = :consultationUpdateId
            //            ORDER BY ordre
            return null
        }

        TODO("Strapi's consultations are not implemented yet")
    }
}
