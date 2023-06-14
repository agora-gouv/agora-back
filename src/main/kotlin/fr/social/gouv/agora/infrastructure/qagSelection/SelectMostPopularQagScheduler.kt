package fr.social.gouv.agora.infrastructure.qagSelection

import fr.social.gouv.agora.usecase.qagSelection.SelectMostPopularQagUseCase
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class SelectMostPopularQagScheduler(private val useCase: SelectMostPopularQagUseCase) {

    @Scheduled(cron = "0 0 14 * * WED")
    fun selectMostPopularQag() {
        useCase.putMostPopularQagInSelectedStatus()
    }

}