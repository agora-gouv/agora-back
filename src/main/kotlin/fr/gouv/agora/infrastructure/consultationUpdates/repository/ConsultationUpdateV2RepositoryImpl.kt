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
class ConsultationUpdateV2RepositoryImpl(
    private val updateDatabaseRepository: ConsultationUpdateInfoV2DatabaseRepository,
    private val consultationStrapiRepository: ConsultationStrapiRepository,
    private val sectionDatabaseRepository: ConsultationUpdateSectionDatabaseRepository,
    private val clock: Clock,
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val mapper: ConsultationUpdateInfoV2Mapper,
) : ConsultationUpdateV2Repository {

    override fun getUnansweredUsersConsultationUpdateWithUnpublished(consultationId: String): ConsultationUpdateInfoV2? {
        val consultationUUID = consultationId.toUuidOrNull()
        if (consultationUUID != null) {
            val updateDTO = updateDatabaseRepository.getUnansweredUsersConsultationUpdate(consultationUUID)
            val sectionDTOs = sectionDatabaseRepository.getConsultationUpdateSections(updateDTO.id)
            return mapper.toDomain(updateDTO, sectionDTOs)
        }

        if (featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            val consultation = consultationStrapiRepository.getConsultationByIdWithUnpublished(consultationId) ?: return null
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
            val now = LocalDateTime.now(clock)
            val consultation = consultationStrapiRepository.getConsultationByIdWithUnpublished(consultationId) ?: return null

            val latestOtherContent = consultation.attributes.consultationContenuAutres.data
                .filter { it.attributes.datetimePublication < now }
                .maxByOrNull { it.attributes.datetimePublication }
            val reponseDuCommanditaire = consultation.attributes.consultationContenuReponseDuCommanditaire.data
            val analyseDesReponses = consultation.attributes.consultationContenuAnalyseDesReponses.data

            return if (latestOtherContent != null) {
                mapper.toDomainContenuAutre(consultation, latestOtherContent)
            } else if (reponseDuCommanditaire != null && reponseDuCommanditaire.attributes.datetimePublication.isBefore(now)) {
                mapper.toDomainReponseDuCommanditaire(consultation)
            } else if (analyseDesReponses != null && analyseDesReponses.attributes.datetimePublication.isBefore(now)) {
                mapper.toDomainAnalyseDesReponses(consultation)
            } else {
                mapper.toDomainAnsweredOrEnded(consultation, now)
            }
        }

        return null
    }


    override fun getConsultationUpdateBySlugOrIdWithUnpublished(
        consultationId: String,
        consultationUpdateIdOrSlug: String,
    ): ConsultationUpdateInfoV2? {
        val updateFromDb = updateDatabaseRepository
            .getConsultationUpdateByIdOrSlug(consultationId, consultationUpdateIdOrSlug)
        if (updateFromDb != null) {
            val sections = sectionDatabaseRepository.getConsultationUpdateSections(updateFromDb.id)
            return mapper.toDomain(updateFromDb, sections)
        }

        if (featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            val consultationFromStrapi = consultationStrapiRepository.getConsultationByIdWithUnpublished(consultationId)
                ?: return null

            val consultationAttributes = consultationFromStrapi.attributes
            val contenuAvantReponse = consultationAttributes.contenuAvantReponse.data
            val contenuApresReponse = consultationAttributes.contenuApresReponseOuTerminee.data
            val contenuAnalyseReponses = consultationAttributes.consultationContenuAnalyseDesReponses.data
            val contenuReponseCommanditaire = consultationAttributes.consultationContenuReponseDuCommanditaire.data

            val foundContenuAvantReponse = contenuAvantReponse.id == consultationUpdateIdOrSlug || contenuAvantReponse.attributes.slug == consultationUpdateIdOrSlug
            val foundContenuApresReponse = contenuApresReponse.id == consultationUpdateIdOrSlug || contenuApresReponse.attributes.slug == consultationUpdateIdOrSlug
            val foundContenuAnalyseReponses = contenuAnalyseReponses?.id == consultationUpdateIdOrSlug || contenuAnalyseReponses?.attributes?.slug == consultationUpdateIdOrSlug
            val foundContenuReponseCommanditaire = contenuReponseCommanditaire?.id == consultationUpdateIdOrSlug || contenuReponseCommanditaire?.attributes?.slug == consultationUpdateIdOrSlug

            return if (foundContenuAvantReponse) {
                mapper.toDomainUnanswered(consultationFromStrapi)
            } else if (foundContenuApresReponse) {
                mapper.toDomainAnsweredOrEnded(consultationFromStrapi, LocalDateTime.now(clock))
            } else if (foundContenuAnalyseReponses) {
                mapper.toDomainAnalyseDesReponses(consultationFromStrapi)
            } else if (foundContenuReponseCommanditaire) {
                mapper.toDomainReponseDuCommanditaire(consultationFromStrapi)
            } else {
                val contenuAutre = consultationAttributes.consultationContenuAutres.data
                    .firstOrNull { it.id == consultationUpdateIdOrSlug || it.attributes.slug == consultationUpdateIdOrSlug }
                    ?: return null
                mapper.toDomainContenuAutre(consultationFromStrapi, contenuAutre)
            }
        }

        return null
    }

    override fun getConsultationUpdateBySlugOrId(
        consultationId: String,
        consultationUpdateIdOrSlug: String,
    ): ConsultationUpdateInfoV2? {
        return getConsultationUpdateBySlugOrIdWithUnpublished(consultationId, consultationUpdateIdOrSlug)
            ?.takeIf { it.isPublished }
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
                mapper.toDomainAnsweredOrEnded(consultation, LocalDateTime.now(clock))
            } else if (consultation.attributes.consultationContenuAnalyseDesReponses.data?.id == consultationUpdateId) {
                mapper.toDomainAnalyseDesReponses(consultation)
            } else if (consultation.attributes.consultationContenuReponseDuCommanditaire.data?.id == consultationUpdateId) {
                mapper.toDomainReponseDuCommanditaire(consultation)
            } else if (contenuAutre != null) {
                mapper.toDomainContenuAutre(consultation, contenuAutre)
            } else {
                null
            }
        }

        return null
    }
}
