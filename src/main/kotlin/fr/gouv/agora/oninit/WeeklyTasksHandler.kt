package fr.gouv.agora.oninit

import fr.gouv.agora.usecase.qagArchive.AnonymizeOldQagUseCase
import fr.gouv.agora.usecase.qagArchive.ArchiveOldQagUseCase
import fr.gouv.agora.usecase.qagSelection.SelectMostPopularQagUseCase
import org.springframework.stereotype.Component

@Component
class WeeklyTasksHandler(
    private val selectMostPopularQagUseCase: SelectMostPopularQagUseCase,
    private val archiveOldQagUseCase: ArchiveOldQagUseCase,
    private val anonymizeOldQagUseCase: AnonymizeOldQagUseCase,
) : CustomCommandHandler {

    override fun handleTask(arguments: Map<String, String>?) {
        selectMostPopularQagUseCase.putMostPopularQagInSelectedStatus()
        archiveOldQagUseCase.archiveOldQag()
        anonymizeOldQagUseCase.anonymizeOldQag()
        // TODOs
        // - Remove supports from archived QaGs
    }

}
