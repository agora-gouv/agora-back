package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.infrastructure.userAnsweredConsultation.repository.UserAnsweredConsultationDatabaseRepository
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationPreviewAnsweredRepository
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class ConsultationPreviewAnsweredRepositoryImpl(
    private val strapiRepository: ConsultationStrapiRepository,
    private val userAnsweredConsultationDatabaseRepository: UserAnsweredConsultationDatabaseRepository,
    private val mapper: ConsultationInfoMapper,
    private val clock: Clock,
) : ConsultationPreviewAnsweredRepository {

    override fun getConsultationAnsweredCount(userId: String): Int {
        return userId.toUuidOrNull()?.let { userUUID ->
            userAnsweredConsultationDatabaseRepository.getConsultationAnsweredCount(userUUID)
        } ?: 0
    }

    override fun getConsultationAnsweredList(userId: String, offset: Int): List<ConsultationWithUpdateInfo> {
        val userUUID = userId.toUuidOrNull() ?: return emptyList()

        val now = LocalDateTime.now(clock)
        val strapiConsultationsAnsweredIds = userAnsweredConsultationDatabaseRepository.getAnsweredConsultationIds(userUUID)
        val strapiConsultationsAnswered = strapiRepository.getConsultationsByIds(strapiConsultationsAnsweredIds).data
            .map { mapper.toConsultationWithUpdateInfo(it, now) }

        return strapiConsultationsAnswered
    }
}
