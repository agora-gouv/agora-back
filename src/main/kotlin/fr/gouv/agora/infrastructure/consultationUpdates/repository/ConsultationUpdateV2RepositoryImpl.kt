package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.ConsultationUpdateInfoV2
import fr.gouv.agora.infrastructure.consultation.repository.ConsultationStrapiRepository
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateV2Repository
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
@Suppress("unused")
class ConsultationUpdateV2RepositoryImpl(
    private val updateDatabaseRepository: ConsultationUpdateInfoV2DatabaseRepository,
    private val consultationStrapiRepository: ConsultationStrapiRepository,
    private val sectionDatabaseRepository: ConsultationUpdateSectionDatabaseRepository,
    private val clock: Clock,
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

        if (featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            val consultation = consultationStrapiRepository.getConsultationById(consultationId) ?: return null
            return mapper.toDomainUnanswered(consultation)
        }

        return null
    }

    override fun getLatestConsultationUpdate(consultationId: String): ConsultationUpdateInfoV2? {
        val consultationUUID = consultationId.toUuidOrNull()
        if (consultationUUID != null) {
            val updateDTO = updateDatabaseRepository.getLatestConsultationUpdate(consultationUUID)
            val sectionDTOs = sectionDatabaseRepository.getConsultationUpdateSections(updateDTO.id)
            return mapper.toDomain(updateDTO, sectionDTOs)
        }

        if (featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            val consultation = consultationStrapiRepository.getConsultationById(consultationId) ?: return null
            val latestOtherContent = consultation.attributes.consultationContenuAutres.data
                .filter { it.attributes.datetimePublication < LocalDateTime.now(clock) }
                .maxByOrNull { it.attributes.datetimePublication }

            return if (latestOtherContent != null) {
                mapper.toDomainContenuAutre(consultation, latestOtherContent)
            } else {
                mapper.toDomainAnswered(consultation, consultation.attributes.contenuApresReponseOuTerminee.data)
            }
        }

        return null
    }

    override fun getConsultationUpdate(
        consultationId: String,
        consultationUpdateId: String, // todo utiliser un id forgé ?
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
