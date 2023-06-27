package fr.social.gouv.agora.infrastructure.crons

import fr.social.gouv.agora.usecase.consultation.ConsultationCacheClearUseCase
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class DailyScheduler(private val consultationCacheClearUseCase: ConsultationCacheClearUseCase) {

    @Scheduled(cron = "0 0 0 * * *")
    fun deleteConsultationCaches() {
        consultationCacheClearUseCase.clearConsultationCaches()
    }

}