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

    override fun getConsultationUpdateId(consultationId: String, slug: String): String? {
        val updateIdFromDb = updateDatabaseRepository.getConsultationUpdateIdBySlug(slug)
        if (updateIdFromDb != null) return updateIdFromDb.toString()

        if (featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            return consultationStrapiRepository.getConsultationById(consultationId)?.attributes?.getContenuIdBySlugOrId(slug)
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
                mapper.toDomainAnswered(consultation)
            }
        }

        return null
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

        if (featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            val consultation = consultationStrapiRepository.getConsultationById(consultationId) ?: return null
            val contenuAutre =
                consultation.attributes.consultationContenuAutres.data.firstOrNull { it.id == consultationUpdateId }

            return if (consultation.attributes.contenuAvantReponse.data.id == consultationUpdateId) {
                mapper.toDomainUnanswered(consultation)
            } else if (consultation.attributes.contenuApresReponseOuTerminee.data.id == consultationUpdateId) {
                mapper.toDomainAnswered(consultation)
            } else if (contenuAutre != null) {
                mapper.toDomainContenuAutre(consultation, contenuAutre)
            } else {
                null
            }
        }

        return null
    }
}
