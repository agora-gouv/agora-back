package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.domain.AgoraFeature.StrapiConsultations
import fr.gouv.agora.domain.Territoire
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

    override fun getConsultationFinishedList(offset: Int, pageSize: Int, territory: Territoire?): List<ConsultationWithUpdateInfo> {
        val databaseConsultationFinished = if (territory == null || territory == Territoire.Pays.FRANCE) {
            databaseRepository.getConsultationsFinishedWithUpdateInfo(offset, pageSize)
                .map(mapper::toConsultationWithUpdateInfo)
        } else emptyList()

        if (!featureFlagsRepository.isFeatureEnabled(StrapiConsultations)) {
            return databaseConsultationFinished
        }

        val now = LocalDateTime.now(clock)
        val strapiConsultationFinished = strapiRepository.getConsultationsFinished(now, territory).data
            .map { mapper.toConsultationWithUpdateInfo(it, now) }

        return databaseConsultationFinished + strapiConsultationFinished
    }
}
