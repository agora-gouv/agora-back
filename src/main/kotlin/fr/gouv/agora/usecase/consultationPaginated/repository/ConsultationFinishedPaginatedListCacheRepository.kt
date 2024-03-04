package fr.gouv.agora.usecase.consultationPaginated.repository

import fr.gouv.agora.usecase.consultationPaginated.ConsultationFinishedPaginatedList

interface ConsultationFinishedPaginatedListCacheRepository {
    fun getConsultationFinishedPage(pageNumber: Int): ConsultationFinishedPaginatedList?
    fun initConsultationFinishedPage(
        pageNumber: Int,
        content: ConsultationFinishedPaginatedList?,
    )
    fun clearCache()
}
