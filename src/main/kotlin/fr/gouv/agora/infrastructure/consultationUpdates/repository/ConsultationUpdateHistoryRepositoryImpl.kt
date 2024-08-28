package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.ConsultationUpdateHistory
import fr.gouv.agora.infrastructure.consultation.repository.ConsultationStrapiRepository
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateHistoryRepository
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import org.springframework.stereotype.Component

@Component
class ConsultationUpdateHistoryRepositoryImpl(
    val databaseRepository: ConsultationUpdateHistoryDatabaseRepository,
    val consultationStrapiRepository: ConsultationStrapiRepository,
    val featureFlagsRepository: FeatureFlagsRepository,
    val mapper: ConsultationUpdateHistoryMapper,
) : ConsultationUpdateHistoryRepository {

    override fun getConsultationUpdateHistory(consultationId: String): List<ConsultationUpdateHistory> {
        val consultationUUID = consultationId.toUuidOrNull()
        if (consultationUUID != null) {
            return mapper.toDomain(databaseRepository.getConsultationUpdateHistory(consultationUUID))
        }

        if (featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiConsultations)) {
            val consultation = consultationStrapiRepository.getConsultationById(consultationId)
                ?: return emptyList()

            return mapper.toDomain(consultation)
        }

        return emptyList()
    }
}
