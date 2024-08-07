package fr.gouv.agora.infrastructure.userAnsweredConsultation.repository

import fr.gouv.agora.domain.UserAnsweredConsultation
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.consultationResponse.repository.UserAnsweredConsultationRepository
import fr.gouv.agora.usecase.consultationResponse.repository.UserAnsweredConsultationResult
import org.springframework.stereotype.Component

@Component
class UserAnsweredConsultationRepositoryImpl(
    private val databaseRepository: UserAnsweredConsultationDatabaseRepository,
    private val mapper: UserAnsweredConsultationMapper,
) : UserAnsweredConsultationRepository {

    override fun getParticipantCount(consultationId: String): Int {
        return databaseRepository.getParticipantCount(consultationId = consultationId)
    }

    override fun getAnsweredConsultationIds(userId: String): List<String> {
        return userId.toUuidOrNull()?.let { userUUID ->
            databaseRepository.getAnsweredConsultationIds(userUUID)
        } ?: emptyList()
    }

    override fun hasAnsweredConsultation(consultationId: String, userId: String): Boolean {
        return userId.toUuidOrNull()
            ?.let { userUUID ->
                databaseRepository.hasAnsweredConsultation(
                    consultationId = consultationId,
                    userId = userUUID,
                ) >= 1
            } ?: false
    }

    override fun hasAnsweredConsultations(consultationIds: List<String>, userId: String): Map<String, Boolean> {
        if (consultationIds.isEmpty()) return emptyMap()

        return userId.toUuidOrNull()?.let { userUUID ->
            val answeredConsultationList = databaseRepository.getAnsweredConsultations(
                consultationIds = consultationIds,
                userId = userUUID,
            )

            consultationIds.associateWith { consultationId -> answeredConsultationList.contains(consultationId) }
        } ?: emptyMap()
    }

    override fun getUsersAnsweredConsultation(consultationId: String): List<String> {
        return databaseRepository.getUsersAnsweredConsultation(consultationId).map { it.toString() }
    }

    override fun insertUserAnsweredConsultation(userAnsweredConsultation: UserAnsweredConsultation): UserAnsweredConsultationResult {
        return mapper.toDto(userAnsweredConsultation)?.let { userAnsweredConsultationDTO ->
            databaseRepository.save(userAnsweredConsultationDTO)
            UserAnsweredConsultationResult.SUCCESS
        } ?: UserAnsweredConsultationResult.FAILURE
    }
}
