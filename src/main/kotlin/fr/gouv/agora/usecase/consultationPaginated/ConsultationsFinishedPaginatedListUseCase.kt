package fr.gouv.agora.usecase.consultationPaginated

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.Territoire
import fr.gouv.agora.usecase.consultation.ConsultationPreviewFinishedMapper
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationFinishedPaginatedListCacheRepository
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationPreviewFinishedRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service
import kotlin.math.ceil

@Service
class ConsultationsFinishedPaginatedListUseCase(
    private val consultationPreviewFinishedRepository: ConsultationPreviewFinishedRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val mapper: ConsultationPreviewFinishedMapper,
    private val cacheRepository: ConsultationFinishedPaginatedListCacheRepository,
) {
    companion object {
        private const val MAX_PAGE_LIST_SIZE = 100
    }

    fun getConsultationFinishedPaginatedList(pageNumber: Int, inputTerritory: String?): ConsultationFinishedPaginatedList? {
        if (pageNumber <= 0) return null
        val territory = inputTerritory?.let { Territoire.from(inputTerritory) }

        cacheRepository.getConsultationFinishedPage(pageNumber, territory)?.let { pageContent ->
            return pageContent
        }

        // TODO territory
        val consultationsCount = consultationPreviewFinishedRepository.getConsultationFinishedCount()
        val offset = (pageNumber - 1) * MAX_PAGE_LIST_SIZE
        if (offset > consultationsCount) return null

        val thematiques = thematiqueRepository.getThematiqueList()
        val consultationFinishedList = consultationPreviewFinishedRepository
            .getConsultationFinishedList(offset, MAX_PAGE_LIST_SIZE, territory)
            .let { consultationsInfo -> mapper.toConsultationPreviewFinished(consultationsInfo, thematiques) }

        val maxPageNumber = ceil(consultationsCount.toDouble() / MAX_PAGE_LIST_SIZE.toDouble()).toInt()

        return ConsultationFinishedPaginatedList(
            consultationFinishedList = consultationFinishedList,
            maxPageNumber = maxPageNumber,
        ).also { cacheRepository.initConsultationFinishedPage(pageNumber, it, territory) }
    }
}

data class ConsultationFinishedPaginatedList(
    val consultationFinishedList: List<ConsultationPreviewFinished>,
    val maxPageNumber: Int,
)
