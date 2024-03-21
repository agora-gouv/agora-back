package fr.gouv.agora.usecase.consultationUpdate

import fr.gouv.agora.domain.ConsultationStatus
import fr.gouv.agora.domain.ConsultationUpdate
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateRepository
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalDateTime

@Service
@Deprecated("Should use ConsultationUpdateV2UseCase instead")
@Suppress("DEPRECATION")
class ConsultationUpdateUseCase(
    private val repository: ConsultationUpdateRepository,
    private val clock: Clock,
) {
    companion object {
        private const val START_OF_DESCRIPTION = "<body>"
        private const val START_OF_DESCRIPTION_WITH_ADDED_PREFIX =
            "$START_OF_DESCRIPTION🗳 La consultation est terminée !<br/>Les résultats sont en cours d’analyse. Vous serez notifié(e) dès que la synthèse sera disponible.<br/><br/>—<br/><br/>"
    }

    fun getConsultationUpdate(consultationInfo: ConsultationInfo): ConsultationUpdate? {
        return getConsultationUpdate(
            consultationId = consultationInfo.id,
            startDate = consultationInfo.startDate.toLocalDateTime(),
            endDate = consultationInfo.endDate.toLocalDateTime(),
        )
    }

    private fun getConsultationUpdate(
        consultationId: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): ConsultationUpdate? {
        val currentDate = LocalDateTime.now(clock)
        return when {
            isAfterEndDate(currentDate = currentDate, endDate = endDate) -> repository.getFinishedConsultationUpdate(
                consultationId
            ) ?: generateTemporaryConsultationUpdate(
                repository.getOngoingConsultationUpdate(
                    consultationId
                )
            )

            isBetweenStartAndEndDate(
                currentDate = currentDate,
                startDate = startDate,
                endDate = endDate,
            ) -> repository.getOngoingConsultationUpdate(consultationId)

            else -> null
        }
    }

    private fun isBetweenStartAndEndDate(
        currentDate: LocalDateTime,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ) = (currentDate == startDate || currentDate.isAfter(startDate)) && currentDate.isBefore(endDate)

    private fun isAfterEndDate(currentDate: LocalDateTime, endDate: LocalDateTime) =
        currentDate == endDate || currentDate.isAfter(endDate)

    private fun generateTemporaryConsultationUpdate(consultationUpdate: ConsultationUpdate?): ConsultationUpdate? {
        return consultationUpdate?.copy(
            description = consultationUpdate.description.replace(
                START_OF_DESCRIPTION,
                START_OF_DESCRIPTION_WITH_ADDED_PREFIX
            ),
            status = ConsultationStatus.POLITICAL_COMMITMENT
        )
    }
}