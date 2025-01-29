package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.domain.ConsultationUpdateHistory
import fr.gouv.agora.infrastructure.consultation.repository.ConsultationStrapiRepository
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateHistoryRepository
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import org.springframework.stereotype.Component

@Component
class ConsultationUpdateHistoryRepositoryImpl(
    val consultationStrapiRepository: ConsultationStrapiRepository,
    val featureFlagsRepository: FeatureFlagsRepository,
    val mapper: ConsultationUpdateHistoryMapper,
) : ConsultationUpdateHistoryRepository {

    override fun getConsultationUpdateHistory(consultationId: String): List<ConsultationUpdateHistory> {
        val consultation = consultationStrapiRepository.getConsultationByIdWithUnpublished(consultationId)
            ?: return emptyList()

        return mapper.toDomain(consultation)
    }
}
