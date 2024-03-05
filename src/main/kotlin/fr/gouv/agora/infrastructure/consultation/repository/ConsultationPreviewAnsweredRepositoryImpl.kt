package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationPreviewAnsweredRepository
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class ConsultationPreviewAnsweredRepositoryImpl(
    private val databaseRepository: ConsultationDatabaseRepository,
    private val mapper: ConsultationInfoMapper,
) : ConsultationPreviewAnsweredRepository {

    override fun getConsultationAnsweredCount(userId: String): Int {
        return userId.toUuidOrNull()?.let { userUUID ->
            databaseRepository.getConsultationAnsweredCount(userUUID)
        } ?: 0
    }

    override fun getConsultationAnsweredList(userId: String, offset: Int): List<ConsultationWithUpdateInfo> {
        return userId.toUuidOrNull()?.let { userUUID ->
            databaseRepository.getConsultationsAnsweredWithUpdateInfo(userUUID, offset).map(mapper::toDomain)
        } ?: emptyList()
    }

}