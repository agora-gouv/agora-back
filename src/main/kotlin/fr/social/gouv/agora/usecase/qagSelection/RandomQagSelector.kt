package fr.social.gouv.agora.usecase.qagSelection

import fr.social.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import org.springframework.stereotype.Component

@Component
class RandomQagSelector {

    fun chooseRandom(qags: List<QagInfoWithSupportCount>): QagInfoWithSupportCount {
        return qags.random()
    }

}