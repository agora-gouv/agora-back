package fr.social.gouv.agora.infrastructure.crons

import fr.social.gouv.agora.usecase.qagArchive.ArchiveOldQagUseCase
import fr.social.gouv.agora.usecase.qagSelection.SelectMostPopularQagUseCase
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class WeeklyScheduler(
    private val selectMostPopularQagUseCase: SelectMostPopularQagUseCase,
    private val archiveOldQagUseCase: ArchiveOldQagUseCase,
) {

    @Scheduled(cron = "0 0 14 * * WED")
    fun selectMostPopularQagAndArchiveOldQag() {
        println("📆 Performing weekly tasks...")
        selectMostPopularQagUseCase.putMostPopularQagInSelectedStatus()
        archiveOldQagUseCase.archiveOldQag()
        println("📆 Performing weekly tasks... finished !")
    }
}