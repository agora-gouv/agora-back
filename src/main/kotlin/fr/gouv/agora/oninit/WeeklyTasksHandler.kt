package fr.gouv.agora.oninit

import fr.gouv.agora.usecase.qagArchive.AnonymizeOldQagUseCase
import fr.gouv.agora.usecase.qagArchive.ArchiveOldQagUseCase
import fr.gouv.agora.usecase.qagSelection.SelectMostPopularQagUseCase
import fr.gouv.agora.usecase.themeHebdo.IsThemeHebdoTransitionUseCase
import org.springframework.stereotype.Component

@Component
class WeeklyTasksHandler(
    private val isThemeHebdoTransitionUseCase: IsThemeHebdoTransitionUseCase,
    private val selectMostPopularQagUseCase: SelectMostPopularQagUseCase,
    private val archiveOldQagUseCase: ArchiveOldQagUseCase,
    private val anonymizeOldQagUseCase: AnonymizeOldQagUseCase,
) : CustomCommandHandler {

    override fun handleTask(arguments: Map<String, String>?) {
        if (isThemeHebdoTransitionUseCase.isInTransition()) {
            selectMostPopularQagUseCase.putMostPopularQagInSelectedStatus()
            archiveOldQagUseCase.archiveOldQag()
        }
        anonymizeOldQagUseCase.anonymizeOldQag()
        // TODOs
        // - Remove supports from archived QaGs
    }

}
