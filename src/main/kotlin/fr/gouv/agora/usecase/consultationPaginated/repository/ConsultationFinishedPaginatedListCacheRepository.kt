package fr.gouv.agora.usecase.consultationPaginated.repository

import fr.gouv.agora.domain.Territoire
import fr.gouv.agora.usecase.consultationPaginated.ConsultationFinishedPaginatedList

interface ConsultationFinishedPaginatedListCacheRepository {
    fun getConsultationFinishedPage(pageNumber: Int, territory: Territoire?): ConsultationFinishedPaginatedList?
    fun initConsultationFinishedPage(
        pageNumber: Int,
        content: ConsultationFinishedPaginatedList?,
        territory: Territoire?,
    )
    fun clearCache()
}
