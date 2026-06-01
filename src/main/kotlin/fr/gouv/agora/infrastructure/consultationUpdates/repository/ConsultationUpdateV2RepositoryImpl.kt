package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.domain.ConsultationUpdateInfoV2
import fr.gouv.agora.infrastructure.consultation.dto.strapi.StrapiConsultationContenuAutre
import fr.gouv.agora.infrastructure.consultation.repository.ConsultationStrapiRepository
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateV2Repository
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class ConsultationUpdateV2RepositoryImpl(
    private val consultationStrapiRepository: ConsultationStrapiRepository,
    private val clock: Clock,
    private val mapper: ConsultationUpdateInfoV2Mapper,
) : ConsultationUpdateV2Repository {

    override fun getUnansweredUsersConsultationUpdateWithUnpublished(consultationId: String): ConsultationUpdateInfoV2? {
        val consultation = consultationStrapiRepository.getConsultationByIdWithUnpublished(consultationId)
            ?: return null

        return mapper.toDomainUnanswered(consultation)
    }

    override fun getLatestConsultationUpdate(consultationId: String): ConsultationUpdateInfoV2? {
        val now = LocalDateTime.now(clock)
        val consultation =
            consultationStrapiRepository.getConsultationByIdWithUnpublished(consultationId) ?: return null

        val latestOtherContent = consultation.consultationContenuAutres
            .filter { it.datetimePublication < now }
            .maxByOrNull { it.datetimePublication }
        val reponseDuCommanditaire = consultation.consultationContenuReponseDuCommanditaire
        val analyseDesReponses = consultation.consultationContenuAnalyseDesReponses

        return if (latestOtherContent != null) {
            mapper.toDomainContenuAutre(consultation, latestOtherContent)
        } else if (reponseDuCommanditaire != null && reponseDuCommanditaire.datetimePublication.isBefore(now)) {
            mapper.toDomainReponseDuCommanditaire(consultation)
        } else if (analyseDesReponses != null && analyseDesReponses.datetimePublication.isBefore(now)) {
            mapper.toDomainAnalyseDesReponses(consultation)
        } else {
            mapper.toDomainAnsweredOrEnded(consultation, now)
        }
    }


    override fun getConsultationUpdateBySlugOrIdWithUnpublished(
        consultationId: String,
        consultationUpdateIdOrSlug: String,
    ): ConsultationUpdateInfoV2? {
        val consultationFromStrapi = consultationStrapiRepository.getConsultationByIdWithUnpublished(consultationId)
            ?: return null

        val contenuAvantReponse = consultationFromStrapi.contenuAvantReponse
        val contenuApresReponse = consultationFromStrapi.contenuApresReponseOuTerminee
        val contenuAnalyseReponses = consultationFromStrapi.consultationContenuAnalyseDesReponses
        val contenuReponseCommanditaire = consultationFromStrapi.consultationContenuReponseDuCommanditaire

        val foundContenuAvantReponse =
            contenuAvantReponse.documentId == consultationUpdateIdOrSlug || contenuAvantReponse.slug == consultationUpdateIdOrSlug
        val foundContenuApresReponse =
            contenuApresReponse.documentId == consultationUpdateIdOrSlug || contenuApresReponse.slug == consultationUpdateIdOrSlug
        val foundContenuAnalyseReponses =
            contenuAnalyseReponses?.documentId == consultationUpdateIdOrSlug || contenuAnalyseReponses?.slug == consultationUpdateIdOrSlug
        val foundContenuReponseCommanditaire =
            contenuReponseCommanditaire?.documentId == consultationUpdateIdOrSlug || contenuReponseCommanditaire?.slug == consultationUpdateIdOrSlug

        return if (foundContenuAvantReponse) {
            mapper.toDomainUnanswered(consultationFromStrapi)
        } else if (foundContenuApresReponse) {
            mapper.toDomainAnsweredOrEnded(consultationFromStrapi, LocalDateTime.now(clock))
        } else if (foundContenuAnalyseReponses) {
            mapper.toDomainAnalyseDesReponses(consultationFromStrapi)
        } else if (foundContenuReponseCommanditaire) {
            mapper.toDomainReponseDuCommanditaire(consultationFromStrapi)
        } else {
            val contenuAutre = consultationFromStrapi.consultationContenuAutres
                .firstOrNull { it.documentId == consultationUpdateIdOrSlug || it.slug == consultationUpdateIdOrSlug }
                ?: return null
            mapper.toDomainContenuAutre(consultationFromStrapi, contenuAutre)
        }
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
        val consultation = consultationStrapiRepository.getConsultationById(consultationId) ?: return null
        val contenuAutre =
            consultation.consultationContenuAutres.firstOrNull { it.documentId == consultationUpdateId }

        return if (consultation.contenuAvantReponse.documentId == consultationUpdateId) {
            mapper.toDomainUnanswered(consultation)
        } else if (consultation.contenuApresReponseOuTerminee.documentId == consultationUpdateId) {
            mapper.toDomainAnsweredOrEnded(consultation, LocalDateTime.now(clock))
        } else if (consultation.consultationContenuAnalyseDesReponses?.documentId == consultationUpdateId) {
            mapper.toDomainAnalyseDesReponses(consultation)
        } else if (consultation.consultationContenuReponseDuCommanditaire?.documentId == consultationUpdateId) {
            mapper.toDomainReponseDuCommanditaire(consultation)
        } else if (contenuAutre != null) {
            mapper.toDomainContenuAutre(consultation, contenuAutre)
        } else {
            null
        }
    }
}
