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
        return consultationId.toUuidOrNull()?.let { consultationUUID ->
            databaseRepository.getParticipantCount(consultationId = consultationUUID)
        } ?: 0
    }

    override fun getAnsweredConsultationIds(userId: String): List<String> {
        return userId.toUuidOrNull()?.let { userUUID ->
            databaseRepository.getAnsweredConsultationIds(userUUID).map { it.toString() }
        } ?: emptyList()
    }

    override fun hasAnsweredConsultation(consultationId: String, userId: String): Boolean {
        return userId.toUuidOrNull()
            ?.let { userUUID ->
                consultationId.toUuidOrNull()
                    ?.let { consultationUUID ->
                        databaseRepository.hasAnsweredConsultation(
                            consultationId = consultationUUID,
                            userId = userUUID,
                        ) >= 1
                    } ?: false
            } ?: false
    }

    override fun hasAnsweredConsultations(consultationIds: List<String>, userId: String): Map<String, Boolean> {
        return userId.toUuidOrNull()?.let { userUUID ->
            consultationIds
                .mapNotNull { consultationId -> consultationId.toUuidOrNull() }
                .takeIf { it.isNotEmpty() }
                ?.let { consultationUUIDs ->
                    val answeredConsultationList = databaseRepository.getAnsweredConsultations(
                        consultationIds = consultationUUIDs,
                        userId = userUUID,
                    ).map { consultationUUID -> consultationUUID.toString() }

                    consultationIds.associateWith { consultationId -> answeredConsultationList.contains(consultationId) }
                }
        } ?: emptyMap()
    }

    override fun getUsersAnsweredConsultation(consultationId: String): List<String> {
        return consultationId.toUuidOrNull()?.let { consultationUUID ->
            databaseRepository.getUsersAnsweredConsultation(consultationUUID).map { it.toString() }
        } ?: emptyList()
    }

    override fun insertUserAnsweredConsultation(userAnsweredConsultation: UserAnsweredConsultation): UserAnsweredConsultationResult {
        return mapper.toDto(userAnsweredConsultation)?.let { userAnsweredConsultationDTO ->
            databaseRepository.save(userAnsweredConsultationDTO)
            UserAnsweredConsultationResult.SUCCESS
        } ?: UserAnsweredConsultationResult.FAILURE
    }
}