package fr.gouv.agora.usecase.consultationPaginated.repository

import fr.gouv.agora.usecase.consultationPaginated.ConsultationAnsweredPaginatedList

interface ConsultationAnsweredPaginatedListCacheRepository {
    fun getConsultationAnsweredPage(userId: String, pageNumber: Int): ConsultationAnsweredPaginatedList?
    fun initConsultationAnsweredPage(
        userId: String,
        pageNumber: Int,
        content: ConsultationAnsweredPaginatedList?,
    )

    fun clearCache(userId: String)
}
