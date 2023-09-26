package fr.social.gouv.agora.infrastructure.crons

import fr.social.gouv.agora.usecase.consultation.ConsultationCacheClearUseCase
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewPageRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class DailyScheduler(
    private val consultationCacheClearUseCase: ConsultationCacheClearUseCase,
    private val consultationPreviewPageRepository: ConsultationPreviewPageRepository,
) {

    @Scheduled(cron = "0 0 0 * * *")
    fun deleteConsultationCaches() {
        println("🧹 Clearing consultation caches...")
        consultationCacheClearUseCase.clearConsultationCaches()
        consultationPreviewPageRepository.evictConsultationPreviewFinishedList()
        println("🧹 Clearing consultation caches... finished !")

        // TODOs
        // - Consultation responses aggregation when finished
        // - Save consultations answered by users but remove link to responses
        // - Delete everything related to a user when last connection date is over 2 years (except QaG if status is SELECTED_FOR_RESPONSE)
    }

}