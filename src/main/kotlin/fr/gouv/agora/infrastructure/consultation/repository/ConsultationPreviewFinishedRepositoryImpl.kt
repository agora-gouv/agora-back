package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.domain.AgoraFeature.StrapiConsultations
import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationPreviewFinishedRepository
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class ConsultationPreviewFinishedRepositoryImpl(
    private val strapiRepository: ConsultationStrapiRepository,
    private val databaseRepository: ConsultationDatabaseRepository,
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val mapper: ConsultationInfoMapper,
    private val clock: Clock,
) : ConsultationPreviewFinishedRepository {

    override fun getConsultationFinishedCount(): Int {
        if (!featureFlagsRepository.isFeatureEnabled(StrapiConsultations)) {
            return databaseRepository.getConsultationFinishedCount()
        }

        val now = LocalDateTime.now(clock)
        return databaseRepository.getConsultationFinishedCount() +
                strapiRepository.countFinishedConsultations(now)
    }

    // TODO utiliser un enum pour les territoires
    override fun getConsultationFinishedList(offset: Int, pageSize: Int, territory: String?): List<ConsultationWithUpdateInfo> {
        val databaseConsultationFinished = if (territory == null || territory.lowercase() == "national") {
            databaseRepository.getConsultationsFinishedWithUpdateInfo(offset, pageSize)
                .map(mapper::toConsultationWithUpdateInfo)
        } else emptyList()

        if (!featureFlagsRepository.isFeatureEnabled(StrapiConsultations)) {
            return databaseConsultationFinished
        }

        val now = LocalDateTime.now(clock)
        val strapiConsultationFinished = if (territory == null) {
            strapiRepository.getConsultationsFinishedWithUnpublished(now).data
                .filter { it.attributes.isPublished() }
                .map { mapper.toConsultationWithUpdateInfo(it, now) }
        } else {
            strapiRepository.getConsultationsFinishedByTerritory(now, territory).data
                .map { mapper.toConsultationWithUpdateInfo(it, now) }
        }

        return databaseConsultationFinished + strapiConsultationFinished
    }
}
