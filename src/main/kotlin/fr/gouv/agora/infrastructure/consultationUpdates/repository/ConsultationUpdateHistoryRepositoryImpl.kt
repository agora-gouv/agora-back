package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.domain.ConsultationUpdateHistory
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateHistoryRepository
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class ConsultationUpdateHistoryRepositoryImpl(
    val databaseRepository: ConsultationUpdateHistoryDatabaseRepository,
    val mapper: ConsultationUpdateHistoryMapper,
) : ConsultationUpdateHistoryRepository {

    override fun getConsultationUpdateHistory(consultationId: String): List<ConsultationUpdateHistory> {
        return consultationId.toUuidOrNull()?.let { consultationUUID ->
            mapper.toDomain(databaseRepository.getConsultationUpdateHistory(consultationUUID))
        } ?: emptyList()
    }

}