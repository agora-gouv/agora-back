package fr.gouv.agora.oninit

import fr.gouv.agora.usecase.qagArchive.ArchiveOldQagUseCase
import fr.gouv.agora.usecase.qagSelection.SelectMostPopularQagUseCase
import org.springframework.stereotype.Component

@Component
class WeeklyTasksHandler(
    private val selectMostPopularQagUseCase: SelectMostPopularQagUseCase,
    private val archiveOldQagUseCase: ArchiveOldQagUseCase,
) : CustomCommandHandler {

    override fun handleTask(arguments: Map<String, String>?) {
        selectMostPopularQagUseCase.putMostPopularQagInSelectedStatus()
        archiveOldQagUseCase.archiveOldQag()
        // TODOs
        // - Remove username from QaGs even when status is SELECTED_FOR_RESPONSE
        // - Remove supports from archived QaGs
    }

}