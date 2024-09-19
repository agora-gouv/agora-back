package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.domain.AgoraFeature.StrapiConsultations
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationPreviewAnsweredRepository
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class ConsultationPreviewAnsweredRepositoryImpl(
    private val strapiRepository: ConsultationStrapiRepository,
    private val databaseRepository: ConsultationDatabaseRepository,
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val mapper: ConsultationInfoMapper,
    private val clock: Clock,
) : ConsultationPreviewAnsweredRepository {

    override fun getConsultationAnsweredCount(userId: String): Int {
        return userId.toUuidOrNull()?.let { userUUID ->
            databaseRepository.getConsultationAnsweredCount(userUUID)
        } ?: 0
    }

    override fun getConsultationAnsweredList(userId: String, offset: Int): List<ConsultationWithUpdateInfo> {
        val userUUID = userId.toUuidOrNull() ?: return emptyList()

        if (!featureFlagsRepository.isFeatureEnabled(StrapiConsultations)) {
            return databaseRepository.getConsultationsAnsweredWithUpdateInfo(userUUID, offset)
                .map(mapper::toConsultationWithUpdateInfo)
        }

        val now = LocalDateTime.now(clock)
        val strapiConsultationsAnsweredIds = databaseRepository.getConsultationAnsweredIds(userUUID)
        val strapiConsultationsAnswered = strapiRepository.getConsultationsByIds(strapiConsultationsAnsweredIds).data
            .map { mapper.toConsultationWithUpdateInfo(it, now) }
        val databaseAnsweredConsultations = databaseRepository.getConsultationsAnsweredWithUpdateInfo(userUUID, offset)
            .map(mapper::toConsultationWithUpdateInfo)

        return strapiConsultationsAnswered + databaseAnsweredConsultations
    }
}
