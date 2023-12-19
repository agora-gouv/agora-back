package fr.gouv.agora.oninit

import fr.gouv.agora.AgoraCustomCommandHelper
import fr.gouv.agora.usecase.consultation.ConsultationCacheClearUseCase
import fr.gouv.agora.usecase.consultation.repository.ConsultationPreviewPageRepository
import fr.gouv.agora.usecase.qagArchive.ArchiveOldQagUseCase
import fr.gouv.agora.usecase.qagSelection.SelectMostPopularQagUseCase
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.SpringApplication
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class AgoraCustomCommandHandler(
    private val applicationContext: ApplicationContext,
    // Daily tasks
    private val consultationCacheClearUseCase: ConsultationCacheClearUseCase,
    private val consultationPreviewPageRepository: ConsultationPreviewPageRepository,
    // Weekly tasks
    private val selectMostPopularQagUseCase: SelectMostPopularQagUseCase,
    private val archiveOldQagUseCase: ArchiveOldQagUseCase,
) : InitializingBean {

    companion object {
        private const val DAILY_TASKS = "dailyTasks"
        private const val WEEKLY_TASKS = "weeklyTasks"
        private const val SELECT_MOST_POPULAR_QAG = "selectMostPopularQag"
        private const val ARCHIVE_OLD_QAGS = "archiveOldQags"
    }

    override fun afterPropertiesSet() {
        if (handleCustomCommand()) {
            SpringApplication.exit(applicationContext, { 0 })
            println("⚙️ Run custom command finished")
        }
    }

    private fun handleCustomCommand(): Boolean {
        return AgoraCustomCommandHelper.getStoredCustomCommandAndClear()?.let { customCommand ->
            println("⚙️ Run custom command = ${customCommand.command} / argument = ${customCommand.argument}")
            when (customCommand.command) {
                DAILY_TASKS -> {
                    performDailyTasks()
                    true
                }
                WEEKLY_TASKS -> {
                    performWeeklyTasks()
                    true
                }
                ARCHIVE_OLD_QAGS -> {
                    archiveOldQagUseCase.archiveOldQag()
                    true
                }
                SELECT_MOST_POPULAR_QAG -> {
                    selectMostPopularQagUseCase.putMostPopularQagInSelectedStatus()
                    true
                }
                else -> false
            }
        } ?: false
    }

    private fun performDailyTasks() {
        consultationCacheClearUseCase.clearConsultationCaches()
        consultationPreviewPageRepository.clear()
        // TODOs
        // - Consultation responses aggregation when finished
        // - Save consultations answered by users but remove link to responses
        // - Delete everything related to a user when last connection date is over 2 years (except QaG if status is SELECTED_FOR_RESPONSE)
        // - Remove feedback link to userId ?
    }

    private fun performWeeklyTasks() {
        selectMostPopularQagUseCase.putMostPopularQagInSelectedStatus()
        archiveOldQagUseCase.archiveOldQag()
        // TODOs
        // - Remove username from QaGs even when status is SELECTED_FOR_RESPONSE
        // - Remove supports from archived QaGs
    }

}