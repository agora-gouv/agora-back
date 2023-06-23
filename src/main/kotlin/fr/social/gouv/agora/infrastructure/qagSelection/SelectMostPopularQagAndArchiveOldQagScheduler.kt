package fr.social.gouv.agora.infrastructure.qagSelection

import fr.social.gouv.agora.usecase.qagArchive.ArchiveOldQagUseCase
import fr.social.gouv.agora.usecase.qagSelection.SelectMostPopularQagUseCase
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class SelectMostPopularQagAndArchiveOldQagScheduler(
    private val selectMostPopularQagUseCase: SelectMostPopularQagUseCase,
    private val archiveOldQagUseCase: ArchiveOldQagUseCase,
) {

    @Scheduled(cron = "0 0 14 * * WED")
    fun selectMostPopularQagAndArchiveOldQag() {
        selectMostPopularQagUseCase.putMostPopularQagInSelectedStatus()
        archiveOldQagUseCase.archiveOldQag()
    }
}