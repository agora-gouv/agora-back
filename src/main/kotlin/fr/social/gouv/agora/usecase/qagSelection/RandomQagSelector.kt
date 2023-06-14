package fr.social.gouv.agora.usecase.qagSelection

import fr.social.gouv.agora.usecase.qag.QagInfoWithSupportAndThematique
import org.springframework.stereotype.Component

@Component
class RandomQagSelector {

    fun chooseRandom(qags: List<QagInfoWithSupportAndThematique>): QagInfoWithSupportAndThematique {
        return qags.random()
    }

}