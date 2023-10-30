package fr.gouv.agora.usecase.qagUpdates.repository

import fr.gouv.agora.domain.QagInsertingUpdates
import fr.gouv.agora.domain.QagUpdates

interface QagUpdatesRepository {
    fun insertQagUpdates(qagInsertingUpdates: QagInsertingUpdates)
    fun getQagUpdates(qagIdList: List<String>): List<QagUpdates>
}

