package fr.social.gouv.agora.usecase.consultationUpdate

import fr.social.gouv.agora.domain.ConsultationUpdate
import fr.social.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.social.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateRepository
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalDateTime

@Service
class ConsultationUpdateUseCase(
    private val repository: ConsultationUpdateRepository,
    private val clock: Clock,
) {

    fun getConsultationUpdate(consultationInfo: ConsultationInfo): ConsultationUpdate? {
        val currentDate = LocalDateTime.now(clock)
        val startDate = consultationInfo.startDate.toLocalDateTime()
        val endDate = consultationInfo.endDate.toLocalDateTime()
        return when {
            currentDate.equals(endDate) || currentDate.isAfter(endDate) -> repository.getFinishedConsultationUpdate(
                consultationInfo.id
            )
            (currentDate.isAfter(startDate) || currentDate.equals(startDate)) && currentDate.isBefore(endDate) -> repository.getOngoingConsultationUpdate(
                consultationInfo.id
            )
            else -> null
        }
    }

}