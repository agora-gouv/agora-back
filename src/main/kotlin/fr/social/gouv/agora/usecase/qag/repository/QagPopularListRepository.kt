package fr.social.gouv.agora.usecase.qag.repository

import fr.social.gouv.agora.domain.QagInfo

interface QagPopularListRepository {
    fun getQagPopularList(): List<QagInfo>
}