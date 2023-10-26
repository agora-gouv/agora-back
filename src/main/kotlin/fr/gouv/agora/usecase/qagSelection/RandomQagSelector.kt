package fr.gouv.agora.usecase.qagSelection

import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import org.springframework.stereotype.Component

@Component
class RandomQagSelector {

    fun chooseRandom(qags: List<QagInfoWithSupportCount>): QagInfoWithSupportCount {
        return qags.random()
    }

}